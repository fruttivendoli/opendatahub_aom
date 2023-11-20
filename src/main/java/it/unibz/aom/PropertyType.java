package it.unibz.aom;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class PropertyType {
    private final String name;
    private final Class<?> type;

    private boolean nullable = false;
    private boolean readOnly = false;

    public PropertyType(String name, Class<?> type, boolean nullable, boolean readOnly) {
        this.name = name;
        this.type = type;
        this.nullable = nullable;
        this.readOnly = readOnly;
    }

    public String getName() {
        return name;
    }

    public Class<?> getSimpleType() {
        return type;
    }

    public boolean isNullable() {
        return nullable;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public boolean isCompatibleWith(Object value) {
        return type.isInstance(value);
    }


    public static PropertyType createPropertyType(String name, ObjectNode property) {
        if(property.get("type") == null) {
            //todo: wtf do we do here??
            return null;
        }
        String typeStr = property.get("type").asText();
        if("object".equals(typeStr)) {
            // todo: parse additionalProperties Fields
            return null;
        }
        if("array".equals(typeStr)) {
            //todo: handle arrays
            return null;
        }
        boolean nullable = property.has("nullable") && property.get("nullable").asBoolean();
        boolean readOnly = property.has("readOnly") && property.get("readOnly").asBoolean();

        Class<?> type = getSimpleType(typeStr, property.has("items") ? (ObjectNode) property.get("items") : null);

        return new PropertyType(name, type, nullable, readOnly);
    }


    private static Class<?> getSimpleType(String type, ObjectNode items) {
        return switch (type) {
            case "string" -> String.class;
            case "integer" -> Integer.class;
            case "boolean" -> Boolean.class;
            case "float" -> Float.class;
            case "double" -> Double.class;
            case "long" -> Long.class;
            case "short" -> Short.class;
            case "byte" -> Byte.class;
            case "char" -> Character.class;
            case "void" -> Void.class;
            case "array" -> {
                Class<?> elementType = getSimpleType(items.get("type").asText(), items.has("items") ? (ObjectNode) items.get("items") : null);
                yield java.lang.reflect.Array.newInstance(elementType, 0).getClass();
            }
            default -> null;
        };
    }
}
