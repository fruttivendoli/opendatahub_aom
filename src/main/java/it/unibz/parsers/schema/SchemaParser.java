package it.unibz.parsers.schema;

import com.fasterxml.jackson.databind.node.ObjectNode;
import it.unibz.aom.Aom;
import it.unibz.aom.accountability.AccountabilityType;
import it.unibz.aom.typesquare.EntityType;

import static it.unibz.utils.NameScoper.getRawName;

public class SchemaParser implements SchemaParsable {

    private final Aom aom;
    private SchemaParserStack parserStack;
    ObjectNode schema;

    EntityParser entityParser;
    PropertyParser propertyParser;
    ArrayParser arrayParser;
    RefParser refParser;

    public SchemaParser(ObjectNode swagger) {
        String title = swagger.get("info").get("title").asText();
        String description = swagger.get("info").get("description").asText();
        aom = new Aom(title, description);

        parserStack = new SchemaParserStack();

        schema = (ObjectNode) swagger.get("components").get("schemas");

        entityParser = new EntityParser(this);
        propertyParser = new PropertyParser(this);
        arrayParser = new ArrayParser(this);
        refParser = new RefParser(this);

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


        if (objectNode.has("$ref")) { //TODO: duplicated(1)
            System.out.println("Parsing ref: " + name);
            refParser.parse(name, objectNode);
        } else if(objectNode.has("type") && objectNode.get("type").asText().equals("object")) {
            entityParser.parse(name, objectNode);
        } else if(objectNode.has("type") && objectNode.get("type").asText().equals("array")) {
            arrayParser.parse(name, objectNode);
        } else if (objectNode.has("type")) {
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
