package it.unibz.parser;

import com.fasterxml.jackson.databind.node.ObjectNode;
import it.unibz.aom.Aom;
import it.unibz.aom.EntityType;

public class Parser implements Parsable{

    private Aom aom;
    ObjectNode schema;

    EntityParser entityParser;
    PropertyParser propertyParser;
    AccoutabilityParser accoutabilityParser;
    ArrayParser arrayParser;

    EntityType currentEntityType;

    public Parser(ObjectNode swagger) {
        String title = swagger.get("info").get("title").asText();
        String description = swagger.get("info").get("description").asText();
        aom = new Aom(title, description);

        schema = (ObjectNode) swagger.get("components").get("schemas");

        entityParser = new EntityParser(this);
        propertyParser = new PropertyParser(this);
        accoutabilityParser = new AccoutabilityParser(this);
        arrayParser = new ArrayParser(this);

        schema.fieldNames().forEachRemaining(entityTypeName -> {
            ObjectNode entityType = (ObjectNode) schema.get(entityTypeName);
            this.parse(entityTypeName, entityType);
            this.setCurrentEntityType(null);
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

    public void setCurrentEntityType(EntityType entityType) {
        currentEntityType = entityType;
    }

    public EntityType getCurrentEntityType() {
        return currentEntityType;
    }

}
