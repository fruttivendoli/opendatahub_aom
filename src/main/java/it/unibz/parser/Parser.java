package it.unibz.parser;

import com.fasterxml.jackson.databind.node.ObjectNode;
import it.unibz.aom.Aom;
import it.unibz.aom.typesquare.EntityType;

public class Parser implements Parsable{

    private final Aom aom;
    private final ParserStack parserStack;
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
            this.getParserStack().debug();
        });
    }

    @Override
    public void parse(String name, ObjectNode objectNode) {
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
