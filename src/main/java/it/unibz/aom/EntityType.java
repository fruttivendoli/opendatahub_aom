package it.unibz.aom;

import java.util.HashMap;
import java.util.Map;

public class EntityType {
    private Map<String, PropertyType> propertyTypes;
    private String name;

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
}
