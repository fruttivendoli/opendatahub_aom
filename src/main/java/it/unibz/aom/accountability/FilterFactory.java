package it.unibz.aom.accountability;

import java.util.HashMap;
import java.util.Map;

public class FilterFactory {

    private HashMap<String, Object> filter;

    public FilterFactory() {
        this.filter = new HashMap<>();
    }

    public FilterFactory add(String key, Object value) {
        this.filter.put(key, value);
        return this;
    }

    public Filter build() {
        Object[] items = filter.entrySet().stream()
                .sorted(HashMap.Entry.comparingByKey())
                .map(Map.Entry::getValue)
                .toArray();
        return new Filter(items);
    }

    public static FilterFactory create() {
        return new FilterFactory();
    }

}
