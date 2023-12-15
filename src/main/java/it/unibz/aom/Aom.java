package it.unibz.aom;

import it.unibz.aom.typesquare.EntityType;

import java.util.HashMap;

public class Aom {

    public String title, description;
    public HashMap<String, EntityType> entityTypes = new HashMap<>();

    public Aom(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public void addEntityType(EntityType entityType) {
        entityTypes.put(entityType.getName(), entityType);
    }

    public boolean hasEntityType(String name) {
        return entityTypes.containsKey(name);
    }

    public EntityType getEntityType(String name) {
        return entityTypes.get(name);
    }

}
