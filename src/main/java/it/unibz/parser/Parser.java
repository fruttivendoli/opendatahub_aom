package it.unibz.parser;

import com.fasterxml.jackson.databind.node.ObjectNode;
import it.unibz.aom.Aom;
import it.unibz.aom.typesquare.EntityType;

public class Parser implements Parsable{

    private final Aom aom;
    private ParserStack parserStack;
    ObjectNode schema;

    EntityParser entityParser;
    PropertyParser propertyParser;
    ArrayParser arrayParser;

    public Parser(ObjectNode swagger) {
        String title = swagger.get("info").get("title").asText();
        String description = swagger.get("info").get("description").asText();
        aom = new Aom(title, description);

        parserStack = new ParserStack();

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
            ParserStack _parserStack = parserStack;
            ParserStack simulatedStack = new ParserStack();
            parserStack = simulatedStack;

            String[] refParts = name.split("/");

            //Get ref node from json schema
            ObjectNode refNode = schema;
            for (int i = 0; i < refParts.length; i++) {
                refNode = (ObjectNode) refNode.get(refParts[i]);
            }

            //Prepare simulated stack //TODO: parse intermediate entities if necessary
            if (refParts.length > 1) {
                for (int i = 0; i < refParts.length; i++) {
                    if (i == 0)
                        simulatedStack.push(aom.getEntityType(refParts[i]));
                    else
                        simulatedStack.push(simulatedStack.peek().getAccountabilityType(refParts[i]).getAccountedType());
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
            String[] refParts = ref.split("/");
            String refName = refParts[refParts.length - 1];
            ObjectNode refNode = (ObjectNode) schema.get(refName);
            parse(name, refNode);
            return;
        }

        if (!objectNode.has("type"))
            return; //TODO: handle this case

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

    public ParserStack getParserStack() {
        return parserStack;
    }
}
