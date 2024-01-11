package it.unibz.aom.accountability;

import it.unibz.aom.typesquare.Entity;
import it.unibz.aom.typesquare.EntityType;

public class AccountabilityType {

    EntityType accountedType;
    String name;
    boolean isLabeled;

    public AccountabilityType(String name, EntityType accountedType) {
        this.accountedType = accountedType;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public EntityType getAccountedType() {
        return accountedType;
    }

    public boolean isCompatibleWith(Entity entity) {
        return entity == null || accountedType.equals(entity.getType());
    }

    public boolean isLabeled() {
        return isLabeled;
    }

    public void setLabeled(boolean isLabeled) {
        this.isLabeled = isLabeled;
    }

    public boolean isLabelled() {
        return isLabeled;
    }
}
