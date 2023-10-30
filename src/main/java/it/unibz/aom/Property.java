package it.unibz.aom;

public class Property {

    private Object value;
    private String name;

    public Property(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
