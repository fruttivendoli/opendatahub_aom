package it.unibz.parsers.schema;

import com.fasterxml.jackson.databind.node.ObjectNode;
import it.unibz.aom.Aom;
import it.unibz.aom.typesquare.EntityType;

public class SchemaParser implements Parsable{

    private final Aom aom;
    private SchemaParserStack parserStack;
    ObjectNode schema;

    EntityParser entityParser;
    PropertyParser propertyParser;
    ArrayParser arrayParser;

    public SchemaParser(ObjectNode swagger) {
        String title = swagger.get("info").get("title").asText();
        String description = swagger.get("info").get("description").asText();
        aom = new Aom(title, description);

        parserStack = new SchemaParserStack();

        schema = (ObjectNode) swagger.get("components").get("schemas");

        entityParser = new EntityParser(this);
        propertyParser = new PropertyParser(this);
        arrayParser = new ArrayParser(this);

        schema.fieldNames().forEachRemaining(entityTypeName -> {
            ObjectNode entityType = (ObjectNode) schema.get(entityTypeName);
            this.parse(entityTypeName, entityType);
        });
    }

    @Override
    public void parse(String name, ObjectNode objectNode) {

        if (objectNode == null) { //Only parse by name in a possibly completely different scope
            System.out.println("Starting out-of-scope parsing of " + name);

            //Simulate Stack for out-of-scope parsing
            SchemaParserStack _parserStack = parserStack;
            SchemaParserStack simulatedStack = new SchemaParserStack();
            parserStack = simulatedStack;

            String[] refParts = name.split("/");

            //Get ref node from json schema
            ObjectNode refNode = schema;
            for (int i = 0; i < refParts.length; i++) {
                refNode = (ObjectNode) refNode.get(refParts[i]);
            }

            //Prepare simulated stack
            if (refParts.length > 1) {
                StringBuilder currentScope = new StringBuilder();
                for (int i = 0; i < refParts.length; i++) {
                    if (i > 0)
                        currentScope.append("/" + refParts[i]);
                    else
                        currentScope.append(refParts[i]);

                    if (i == 0) {
                        EntityType root = aom.getEntityType(refParts[i]);
                        if (root == null) {
                            parse(refParts[i], null);
                            root = aom.getEntityType(refParts[i]);
                        }
                        simulatedStack.push(root);
                    } else {
                        EntityType current = aom.getEntityType(currentScope.toString());
                        if (current == null) {
                            parse(currentScope.toString(), null);
                            current = aom.getEntityType(currentScope.toString());
                        }
                        simulatedStack.push(simulatedStack.peek().getAccountabilityType(refParts[i]).getAccountedType());
                    }
                }
            }
            System.out.print("Simulated stack: ");
            simulatedStack.debug();

            //Parse
            parse(name, refNode);

            //Restore stack
            parserStack = _parserStack;

            return;
        }

        if (objectNode.has("$ref")) {
            //Parse ref objects first
            String ref = objectNode.get("$ref").asText();
            String refName = ref.replaceFirst("#/components/schemas/", "");
            parse(refName, null); //Parse ref in a possibly out of scope scenario
            return;
        }

        if (!objectNode.has("type"))
            return;

        if(objectNode.get("type").asText().equals("object")) {
            entityParser.parse(name, objectNode);
        } else if(objectNode.get("type").asText().equals("array")) {
            arrayParser.parse(name, objectNode);
        } else {
            propertyParser.parse(name, objectNode);
        }
    }

    public Aom getAom() {
        return aom;
    }

    public SchemaParserStack getParserStack() {
        return parserStack;
    }
}
