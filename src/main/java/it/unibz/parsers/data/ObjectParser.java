package it.unibz.parsers.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import it.unibz.aom.AOMException;
import it.unibz.aom.Aom;
import it.unibz.aom.accountability.AccountabilityType;
import it.unibz.aom.typesquare.Entity;
import it.unibz.aom.typesquare.EntityType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
                if (entityType.getAccountabilityType(field.getKey()) != null) {
                    AccountabilityType accountabilityType = entityType.getAccountabilityType(field.getKey());
                    EntityType childType = entityType.getAccountabilityType(field.getKey()).getAccountedType();
                    if(accountabilityType.isLabeled()) {
                        System.out.println("Is Labeled " + field.getKey());
                        field.getValue().fields().forEachRemaining(labelledField -> {
                            Entity child = childType.create();
                            try {
                                parent.setAccountability(field.getKey(), labelledField.getKey(), child); // todo: array hashmap ???
                            } catch (AOMException e) {
                                throw new RuntimeException(e);
                            }
                            this.parse(child, labelledField.getValue());
                        });

                    } else {
                        if(field.getValue().isObject() || field.getValue().isNull()) {
                            Entity child = childType.create();
                            this.parse(child, field.getValue());
                            try {
                                parent.setAccountability(field.getKey(), child);
                            } catch (AOMException e) {
                                throw new RuntimeException(e);
                            }
                        } else if(field.getValue().isArray()) {
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
                    }
                } else {
                    propertyParser.parse(parent, field.getKey(), field.getValue());
                }
            } catch (AOMException e) {
                throw new RuntimeException(e);
            }
        });

    }

}
