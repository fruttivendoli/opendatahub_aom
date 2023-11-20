package it.unibz.aom;

import java.util.LinkedList;

public class EntityRelationship {

    private final LinkedList<EntityType> from = new LinkedList<>();
    private final LinkedList<EntityType> to = new LinkedList<>();

    public EntityRelationship() {
    }

    public EntityRelationship(EntityType from, EntityType to) {
        this.from.add(from);
        this.to.add(to);
    }

    public EntityRelationship(LinkedList<EntityType> from, LinkedList<EntityType> to) {
        this.from.addAll(from);
        this.to.addAll(to);
    }

    public void addFrom(EntityType entityType) {
        from.add(entityType);
    }

    public void addTo(EntityType entityType) {
        to.add(entityType);
    }

    public LinkedList<EntityType> getFrom() {
        return from;
    }

    public LinkedList<EntityType> getTo() {
        return to;
    }

}
