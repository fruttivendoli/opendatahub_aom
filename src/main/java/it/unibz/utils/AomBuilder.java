package it.unibz.utils;

import com.fasterxml.jackson.databind.node.ObjectNode;
import it.unibz.aom.Aom;
import it.unibz.aom.EntityType;

public class AomBuilder {

    public static Aom populateAOM(ObjectNode swagger) {
        String title = swagger.get("info").get("title").asText();
        String description = swagger.get("info").get("description").asText();

        Aom aom = new Aom(title, description);

        ObjectNode schemas = (ObjectNode) swagger.get("components").get("schemas");

        //Create Entity Types

        schemas.fieldNames().forEachRemaining(entityTypeName -> {
            ObjectNode entityType = (ObjectNode) schemas.get(entityTypeName);
            EntityType.parseEntityTypes(aom, entityTypeName, entityType, "properties");
        });

        return aom;
    }

}
