package it.unibz.parsers.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import it.unibz.aom.AOMException;
import it.unibz.aom.Aom;
import it.unibz.aom.typesquare.Entity;
import it.unibz.aom.typesquare.PropertyType;

import java.lang.reflect.Array;

public class PropertyParser {

    private Aom aom;

    public PropertyParser(Aom aom) {
        this.aom = aom;
    }

    public void parse(Entity parent, String key, JsonNode value) throws AOMException {
        PropertyType propertyType = parent.getType().getPropertyType(key);
        if(propertyType == null)
            // todo: see if this still exists later
            return;
        parent.setProperty(key, parseJsonNodeToType(propertyType, value));
    }

    private Object parseJsonNodeToType(PropertyType propertyType, JsonNode value) throws AOMException {


        if(value.isNull())
            return null;
        if(value.isArray()) {
            Class<?> type = propertyType.getSimpleType().getComponentType();
            Object array = Array.newInstance(type, value.size());
            for (int i = 0; i < value.size(); i++) {
                Array.set(array, i, parseJsonNodeToType(propertyType, value.get(i)));
            }
            return array;
        }
        if(value.isInt())
            return value.asInt();
        if (value.isNumber())
            return value.asDouble();
        if (value.isBoolean())
            return value.asBoolean();
        if (value.isTextual())
            return value.asText();
        else {
            throw new RuntimeException("Unknown type: " + value.getNodeType() + " for value: " + value + " of property type: " + propertyType.getName());
        }
    }


}
