package it.unibz.aom;

import java.util.List;

public class Accountability {

    List<Entity> accountedEntities;
    AccountabilityType type;

    public Accountability(AccountabilityType type, List<Entity> accountedEntities) {
        this.accountedEntities = accountedEntities;
        this.type = type;
    }

    public List<Entity> getAccountedEntities() {
        return accountedEntities;
    }

    public Entity getAccountedEntity(int index) {
        return accountedEntities.get(index);
    }

    public Entity getAccountedEntity() throws AOMException {
       if(accountedEntities.size() == 1)
           return accountedEntities.get(0);
       throw new AOMException("Accountability has more than one entity");
    }

    public void setAccountedEntities(List<Entity> accountedEntities) {
        this.accountedEntities = accountedEntities;
    }

}
