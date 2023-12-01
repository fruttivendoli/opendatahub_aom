package it.unibz.utils;

public class SimpleTypeMapper {

    public static Class<?> getSimpleType(String type) {
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
            default -> null;
        };
    }

}
