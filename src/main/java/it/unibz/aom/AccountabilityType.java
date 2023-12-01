package it.unibz.aom;

public class AccountabilityType {

    EntityType accountedType;
    String name;

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
        return accountedType.equals(entity.getType());
    }

}
