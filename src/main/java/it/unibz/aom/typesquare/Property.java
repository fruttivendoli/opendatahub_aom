package it.unibz.aom.typesquare;

import it.unibz.aom.AOMException;

public class Property {

    private Object value;
    private final PropertyType propertyType;

    public Property(PropertyType propertyType, Object value) {
        this.propertyType = propertyType;
        this.value = value;
    }

    public void setValue(Object value) throws AOMException {
        if (propertyType.isReadOnly()) {
            throw new AOMException("Property \"" + propertyType.getName() + "\" is read only");
        }
        if (value == null && !propertyType.isNullable()) {
            throw new AOMException("Property \"" + propertyType.getName() + "\" is not nullable");
        }
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    public PropertyType getPropertyType() {
        return propertyType;
    }
}
