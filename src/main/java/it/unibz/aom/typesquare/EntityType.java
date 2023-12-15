package it.unibz.aom.typesquare;

import it.unibz.aom.accountability.AccountabilityType;

import java.util.HashMap;
import java.util.Map;

public class EntityType {
    private final Map<String, PropertyType> propertyTypes;
    private final Map<String, AccountabilityType> accountabilityTypes;

    private final String name;

    public EntityType(String name) {
        this.propertyTypes = new HashMap<>();
        this.accountabilityTypes = new HashMap<>();
        this.name = name;
    }

    public void addPropertyType(PropertyType type) {
        propertyTypes.put(type.getName(), type);
    }

    public void addAccountabilityType(AccountabilityType type) {
        accountabilityTypes.put(type.getName(), type);
    }

    public Entity create() {
        return new Entity(this);
    }

    public boolean hasPropertyType(String name) {
        return propertyTypes.containsKey(name);
    }

    public boolean hasAccountabilityType(String name) {
        return accountabilityTypes.containsKey(name);
    }

    public PropertyType getPropertyType(String name) {
        return propertyTypes.get(name);
    }

    public AccountabilityType getAccountabilityType(String name) {
        return accountabilityTypes.get(name);
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof EntityType) {
            return ((EntityType) obj).getName().equals(getName());
        }
        return false;
    }
}
