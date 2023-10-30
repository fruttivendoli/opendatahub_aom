package it.unibz.aom;

import java.util.HashMap;
import java.util.Map;

public class Entity {
    private EntityType type;
    private Map<String, Property> properties;

    public Entity(EntityType type) {
        this.type = type;
        this.properties = new HashMap<>();
    }

    public void setProperty(String name, Object value) throws AOMException {
        if (!type.hasPropertyType(name)) {
            throw new AOMException("Property \"" + name + "\" does not exist in entity type \"" + type.getName() + "\"");
        }
        PropertyType _type = type.getPropertyType(name);
        if (!_type.isCompatibleWith(value)) {
            throw new AOMException("Property \"" + name + "\" is not of type \"" + _type.getType().getName() + "\"");
        }
        if(properties.containsKey(name))
            properties.get(name).setValue(value);
        else
            properties.put(name,  new Property(name, _type));

    }

    public Object getProperty(String name) {
        return properties.get(name).getValue();
    }
}
