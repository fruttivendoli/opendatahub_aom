package it.unibz.aom.accountability.implementations;

import it.unibz.aom.accountability.AccountabilityType;
import it.unibz.aom.accountability.AccountabilityKey;
import it.unibz.aom.accountability.AccountabilityKeyFactory;
import it.unibz.aom.typesquare.Entity;

import java.util.List;

public class LabeledAccountability extends Accountability {

    String label;

    public LabeledAccountability(AccountabilityType type, String label, List<Entity> accountedEntities) {
        super(type, accountedEntities);
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

}
