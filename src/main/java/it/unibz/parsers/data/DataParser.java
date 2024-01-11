package it.unibz.parsers.data;

import com.fasterxml.jackson.databind.node.ObjectNode;
import it.unibz.aom.AOMException;
import it.unibz.aom.Aom;
import it.unibz.aom.typesquare.Entity;
import it.unibz.aom.typesquare.EntityType;

import java.util.ArrayList;
import java.util.List;

public class DataParser {

    private Aom aom;
    private ObjectNode data;
    private EntityType rootEntityType;

    ObjectParser objectParser;
    PropertyParser propertyParser;

    public DataParser(Aom aom, ObjectNode data, String entityType) {
        this.aom = aom;
        this.data = data;

        if (entityType.startsWith("#/components/schemas/"))
            entityType = entityType.substring("#/components/schemas/".length());
        this.rootEntityType = aom.getEntityType(entityType);

        objectParser = new ObjectParser(aom);
        propertyParser = new PropertyParser(aom);
    }

    public List<Entity> parse() { // todo: what if the root entity is not a list?
        List<Entity> entities = new ArrayList<>();
        if(data.has("Items")) {
            data.get("Items").forEach(item -> {
                Entity rootEntity = rootEntityType.create();
                item.fields().forEachRemaining(field -> {
                    try {
                    if (rootEntityType.getAccountabilityType(field.getKey()) != null) {
                        // objectParser.parse(rootEntity, field.getKey(), (ObjectNode) field.getValue());
                    } else {
                        propertyParser.parse(rootEntity, field.getKey(), field.getValue());
                    }
                    } catch (AOMException e) {
                        throw new RuntimeException(e);
                    }
                });
                entities.add(rootEntity);
            });
        }

        return entities;
    }

}
