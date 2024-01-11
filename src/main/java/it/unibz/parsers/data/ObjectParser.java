package it.unibz.parsers.data;

import com.fasterxml.jackson.databind.JsonNode;
import it.unibz.aom.AOMException;
import it.unibz.aom.Aom;
import it.unibz.aom.accountability.AccountabilityType;
import it.unibz.aom.typesquare.Entity;
import it.unibz.aom.typesquare.EntityType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ObjectParser {

    private Aom aom;
    private PropertyParser propertyParser;

    public ObjectParser(Aom aom) {
        this.aom = aom;
        propertyParser = new PropertyParser(aom);
    }

    public void parse(Entity parent, JsonNode objectNode) {
        objectNode.fields().forEachRemaining(field -> {
            try {
                EntityType entityType = parent.getType();
                if (entityType.hasAccountabilityType(field.getKey())) {
                    this.parseAccountability(parent, entityType, field);
                } else if(!field.getValue().isObject()) {
                    propertyParser.parseForEntityType(parent, field.getKey(), field.getValue());
                }
            } catch (AOMException e) {
                throw new RuntimeException(e);
            }
        });

    }

    private void parseAccountability(Entity parent, EntityType entityType, Map.Entry<String, JsonNode> field) throws AOMException {
        AccountabilityType accountabilityType = entityType.getAccountabilityType(field.getKey());
        EntityType childType = entityType.getAccountabilityType(field.getKey()).getAccountedType();
        if(accountabilityType.isLabeled()) {
            this.parseLabeledAccountability(parent, childType, field);
        } if(field.getValue().isObject() || field.getValue().isNull()) {
                this.parseSimpleAccountability(parent, childType, field);
            } else if(field.getValue().isArray()) {
                this.parseArrayAccountability(parent, childType, field);
        }
    }
    private void parseSimpleAccountability(Entity parent, EntityType childType, Map.Entry<String, JsonNode> field) throws AOMException {
        Entity child = null;
        if(!field.getValue().isNull() && !field.getValue().isEmpty()) {
            child = childType.create();
            this.parse(child, field.getValue());
        }
        parent.setAccountability(field.getKey(), child);
    }

    private void parseArrayAccountability(Entity parent, EntityType childType, Map.Entry<String, JsonNode> field) throws AOMException {
        List<Entity> children = new ArrayList<>();
        field.getValue().forEach(arrayItem -> {
            Entity child = childType.create();
            this.parse(child, arrayItem);
            children.add(child);
        });
        Entity[] childrenArray = new Entity[children.size()];
        children.toArray(childrenArray);
        parent.setAccountability(field.getKey(), childrenArray);

    }

    private void parseLabeledAccountability(Entity parent, EntityType childType, Map.Entry<String, JsonNode> field) {
        field.getValue().fields().forEachRemaining(labelledField -> {
            Entity child = childType.create();
            try {
                if (labelledField.getValue().isObject()) {
                    this.parse(child, labelledField.getValue());
                } else {
                    propertyParser.parseLabeledAccountability(child, labelledField.getKey(), labelledField.getValue()); //todo: make this better
                }
                parent.setAccountability(field.getKey(), labelledField.getKey(), child);
            } catch (AOMException ex) {
                // skip
            }
        });
    }

}
