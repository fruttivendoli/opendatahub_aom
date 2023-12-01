package it.unibz.aom;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Entity {
    private final EntityType type;
    private final Map<String, Property> properties;
    private final Map<String, Accountability> accountabilities;

    public Entity(EntityType type) {
        this.type = type;
        this.properties = new HashMap<>();
        this.accountabilities = new HashMap<>();
    }

    public void setProperty(String name, Object value) throws AOMException {
        if (!type.hasPropertyType(name)) {
            throw new AOMException("Property \"" + name + "\" does not exist in entity type \"" + type.getName() + "\"");
        }
        PropertyType _type = type.getPropertyType(name);
        if (!_type.isCompatibleWith(value)) {
            throw new AOMException("Property \"" + name + "\" is not of type \"" + _type.getSimpleType().getName() + "\"");
        }
        if(properties.containsKey(name))
            properties.get(name).setValue(value);
        else {
            PropertyType propertyType = type.getPropertyType(name);
            if (propertyType == null)
                throw new AOMException("Property \"" + name + "\" does not exist in entity type \"" + type.getName() + "\"");
            properties.put(name, new Property(propertyType, value));
        }

    }

    public void setAccountability(String name, Entity ...accountability) throws AOMException {
        if (!type.hasAccountabilityType(name)) {
            throw new AOMException("Accountability \"" + name + "\" does not exist in entity type \"" + type.getName() + "\"");
        }
        AccountabilityType _type = type.getAccountabilityType(name);

        for (Entity e : accountability) {
            if (!_type.isCompatibleWith(e)) {
                throw new AOMException("Accountability \"" + name + "\" is not of type \"" + _type.getAccountedType().getName() + "\"");
            }
        }

        if(accountabilities.containsKey(name))
            accountabilities.get(name).setAccountedEntities(Arrays.asList(accountability));
        else {
            AccountabilityType accountabilityType = type.getAccountabilityType(name);
            if (accountabilityType == null)
                throw new AOMException("Accountability \"" + name + "\" does not exist in entity type \"" + type.getName() + "\"");
            accountabilities.put(name, new Accountability(_type, Arrays.asList(accountability)));
        }
    }

    public Accountability getAccountability(String name) {
        return accountabilities.get(name);
    }

    public Object getProperty(String name) {
        return properties.get(name).getValue();
    }
    public EntityType getType() {
        return type;
    }
}
