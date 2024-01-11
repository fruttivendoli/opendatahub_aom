package it.unibz.aom.accountability;

import it.unibz.aom.typesquare.Entity;
import it.unibz.aom.typesquare.EntityType;

import java.util.LinkedList;

public class AccountabilityType {

    EntityType accountedType;
    String name;
    LinkedList<String> properties;

    public AccountabilityType(String name, EntityType accountedType) {
        this.properties = new LinkedList<>();
        this.accountedType = accountedType;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public EntityType getAccountedType() {
        return accountedType;
    }

    public boolean isCompatibleWith(Entity entity) {
        return accountedType.equals(entity.getType());
    }

    public void addProperty(String propertyName) {
        properties.add(propertyName);
    }

    public boolean hasProperty(String propertyName) {
        return properties.contains(propertyName);
    }
}
