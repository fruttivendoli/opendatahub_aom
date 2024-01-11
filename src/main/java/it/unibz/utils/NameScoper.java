package it.unibz.utils;

public class NameScoper {

    public static String getRawName(String name) {
        if (name.contains("/"))
            return name.substring(name.lastIndexOf("/") + 1);
        return name;
    }

}
