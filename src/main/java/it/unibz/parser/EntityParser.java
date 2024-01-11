package it.unibz.parser;

import com.fasterxml.jackson.databind.node.ObjectNode;
import it.unibz.aom.accountability.AccountabilityType;
import it.unibz.aom.typesquare.EntityType;

public class EntityParser implements Parsable{

    Parser parser;

    public EntityParser(Parser parser) {
        this.parser = parser;
    }

    @Override
    public void parse(String name, ObjectNode jsonObj) {
        if (parser.getAom().getEntityType(name) != null) {
            addAccountabilityReference(name, parser.getAom().getEntityType(name));
            return;
        }

        EntityType entityType = new EntityType(name);
        EntityType currentEntityType = parser.getCurrentEntityType();

        if(jsonObj.get("additionalProperties").isObject() && jsonObj.get("additionalProperties").has("type")) {
            if(jsonObj.get("additionalProperties").get("type").equals("object")) {
                System.out.println("Parsing ref entity type");
                String ref = jsonObj.get("additionalProperties").get("$ref").asText();
                String refName = ref.split("/")[ref.split("/").length - 1];
                entityType = parser.getAom().getEntityType(refName);
            } else {

            }
            //TODO: maybe parse refEntityType first
        } else {
            entityType = new EntityType(name);
        }

        if (currentEntityType == null)
            parser.getAom().addEntityType(entityType);
        else
            addAccountabilityReference(name, entityType);
        parser.setCurrentEntityType(entityType);

        if (!jsonObj.has("properties")) {
            // todo: we need to fix in post
            return;
        }

        ObjectNode properties = (ObjectNode) jsonObj.get("properties");
        properties.fieldNames().forEachRemaining(propertyName -> {
            ObjectNode property = (ObjectNode) properties.get(propertyName);
            parser.parse(propertyName, property);
        });
    }

    private void addAccountabilityReference(String name, EntityType entityType) {
        parser.currentEntityType.addAccountabilityType(
                new AccountabilityType(
                        name,
                        entityType
                )
        );
    }
}
