package it.unibz.parser;

import com.fasterxml.jackson.databind.node.ObjectNode;
import it.unibz.aom.AccountabilityType;
import it.unibz.aom.Aom;
import it.unibz.aom.Entity;
import it.unibz.aom.EntityType;

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

        if(currentEntityType == null)
            parser.getAom().addEntityType(entityType);
        else
            addAccountabilityReference(name, entityType);
        parser.setCurrentEntityType(entityType);

        parser.getAom().addEntityType(entityType);
        if(!jsonObj.has("properties")) {
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
