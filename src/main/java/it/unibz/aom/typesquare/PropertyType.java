package it.unibz.aom.typesquare;

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

}
