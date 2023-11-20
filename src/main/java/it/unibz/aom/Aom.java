package it.unibz.aom;

import java.util.HashMap;
import java.util.LinkedList;

public class Aom {

    public String title, description;
    public HashMap<String, EntityType> entityTypes = new HashMap<>();
    public LinkedList<EntityRelationship> entityRelationships = new LinkedList<>();

    public Aom(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public void addEntityType(EntityType entityType) {
        entityTypes.put(entityType.getName(), entityType);
    }

    public void addEntityRelationship(EntityRelationship entityRelationship) {
        entityRelationships.add(entityRelationship);
    }

}
