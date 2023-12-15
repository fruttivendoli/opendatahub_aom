package it.unibz.aom;

import java.util.List;

public class LabeledAccountability extends Accountability{
    private String label;
    public LabeledAccountability(AccountabilityType type, String label, List<Entity> accountedEntities) {
        super(type, accountedEntities);
        this.label = label;
    }
}
