package it.unibz.aom;

import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.HashMap;
import java.util.Map;

public class EntityType {
    private final Map<String, PropertyType> propertyTypes;
    private final String name;

    public EntityType(String name) {
        this.propertyTypes = new HashMap<>();
        this.name = name;
    }

    public void addPropertyType(PropertyType type) {
        propertyTypes.put(type.getName(), type);
    }

    public Entity create() {
        return new Entity(this);
    }

    public boolean hasPropertyType(String name) {
        return propertyTypes.containsKey(name);
    }

    public PropertyType getPropertyType(String name) {
        return propertyTypes.get(name);
    }

    public String getName() {
        return name;
    }

    public static void parseEntityTypes(Aom aom, String name, ObjectNode entityType, String objName) {
        EntityType _entityType = new EntityType(name);
        ObjectNode properties = (ObjectNode) entityType.get(objName);
        System.out.println("Parsing entity type: " + name);
        aom.addEntityType(_entityType);
        properties.fieldNames().forEachRemaining(propertyName -> {
            System.out.println("    Parsing property: " + propertyName);
            ObjectNode property = (ObjectNode) properties.get(propertyName);
            if (property.get("type").asText().equals("object")) {
                parseEntityTypes(aom, propertyName, property, "additionalProperties");
                aom.addEntityRelationship(new EntityRelationship(
                        _entityType,
                        aom.entityTypes.get(propertyName)
                ));
            } else {
                PropertyType propertyType = PropertyType.createPropertyType(propertyName, property);
                _entityType.addPropertyType(propertyType);
            }
        });
    }
}
