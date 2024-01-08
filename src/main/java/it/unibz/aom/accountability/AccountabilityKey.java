package it.unibz.aom.accountability;

public class AccountabilityKey {

    private Object[] items;

    public AccountabilityKey(Object[] items) {
        this.items = items;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AccountabilityKey other) {
            if (items.length != other.items.length) {
                return false;
            }
            for (int i = 0; i < items.length; i++) {
                if (!items[i].equals(other.items[i])) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        for (Object item : items) {
            hash += item.hashCode();
        }
        return hash;
    }

}
