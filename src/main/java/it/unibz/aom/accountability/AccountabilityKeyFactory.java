package it.unibz.aom.accountability;

import java.util.HashMap;
import java.util.Map;

public class AccountabilityKeyFactory {

    private HashMap<String, Object> filter;

    public AccountabilityKeyFactory() {
        this.filter = new HashMap<>();
    }

    public AccountabilityKeyFactory add(String key, Object value) {
        this.filter.put(key, value);
        return this;
    }

    public AccountabilityKey build() {
        Object[] items = filter.entrySet().stream()
                .sorted(HashMap.Entry.comparingByKey())
                .map(Map.Entry::getValue)
                .toArray();
        return new AccountabilityKey(items);
    }

    public static AccountabilityKeyFactory create() {
        return new AccountabilityKeyFactory();
    }

}
