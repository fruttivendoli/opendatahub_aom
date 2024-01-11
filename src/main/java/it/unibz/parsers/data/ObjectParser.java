package it.unibz.parsers.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import it.unibz.aom.AOMException;
import it.unibz.aom.Aom;
import it.unibz.aom.accountability.AccountabilityType;
import it.unibz.aom.typesquare.Entity;
import it.unibz.aom.typesquare.EntityType;

public class ObjectParser {

    private Aom aom;
    private PropertyParser propertyParser;

    public ObjectParser(Aom aom) {
        this.aom = aom;
        propertyParser = new PropertyParser(aom);
    }

    public void parse(Entity parent, String key, JsonNode objectNode) {
        objectNode.fields().forEachRemaining(field -> {
            try {
                EntityType entityType = parent.getType();
                if (entityType.getAccountabilityType(field.getKey()) != null) {
                    AccountabilityType accountabilityType = entityType.getAccountabilityType(field.getKey());
                    EntityType childType = entityType.getAccountabilityType(field.getKey()).getAccountedType();
                    if(accountabilityType.isLabelled()) {
                        field.getValue().fields().forEachRemaining(labelledField -> {
                            Entity child = childType.create();
                            try {
                                parent.setAccountability(field.getKey(), labelledField.getKey(), child);
                            } catch (AOMException e) {
                                throw new RuntimeException(e);
                            }
                            this.parse(child, labelledField.getKey(), labelledField.getValue());
                        });

                    } else {
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
