package it.unibz.aom;

public class PropertyType {
    private String name;
    private Class<?> type;

    public PropertyType(String name, Class<?> type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Class<?> getType() {
        return type;
    }

    public boolean isCompatibleWith(Object value) {
        return type.isInstance(value);
    }
}
