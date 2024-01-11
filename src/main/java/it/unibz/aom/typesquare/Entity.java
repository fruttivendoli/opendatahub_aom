package it.unibz.aom.typesquare;

import it.unibz.aom.AOMException;
import it.unibz.aom.accountability.AccountabilityKey;
import it.unibz.aom.accountability.AccountabilityKeyFactory;
import it.unibz.aom.accountability.implementations.LabeledAccountability;
import it.unibz.aom.accountability.implementations.Accountability;
import it.unibz.aom.accountability.AccountabilityType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Entity {
    private final EntityType type;
    private final Map<String, Property> properties;
    public final Map<AccountabilityKey, Accountability> accountabilities;

    public Entity(EntityType type) {
        this.type = type;
        this.properties = new HashMap<>();
        this.accountabilities = new HashMap<>();

    }

    public void setProperty(String name, Object value) throws AOMException {
        if (!type.hasPropertyType(name)) {
            throw new AOMException("Property \"" + name + "\" does not exist in entity type \"" + type.getName() + "\"");
        }
        PropertyType _type = type.getPropertyType(name);
        if (!_type.isCompatibleWith(value)) {
            throw new AOMException("Property \"" + name + "\" is not of type \"" + _type.getSimpleType().getName() + "\"");
        }
        if(properties.containsKey(name))
            properties.get(name).setValue(value);
        else {
            PropertyType propertyType = type.getPropertyType(name);
            if (propertyType == null)
                throw new AOMException("Property \"" + name + "\" does not exist in entity type \"" + type.getName() + "\"");
            properties.put(name, new Property(propertyType, value));
        }

    }

    private void _setAccountability(String name, String label, Entity ...accountability) throws AOMException {
        if (!type.hasAccountabilityType(name)) {
            throw new AOMException("Accountability \"" + name + "\" does not exist in entity type \"" + type.getName() + "\"");
        }
        AccountabilityType _type = type.getAccountabilityType(name);

        for (Entity e : accountability) {
            if (!_type.isCompatibleWith(e)) {
                throw new AOMException("Accountability \"" + name + "\" is not of type \"" + _type.getAccountedType().getName() + "\"");
            }
        }

        AccountabilityKey accountabilityKey;
        if (label != null)
            accountabilityKey = AccountabilityKeyFactory.create().add("name", name).add("label", label).build();
        else
            accountabilityKey = AccountabilityKeyFactory.create().add("name", name).build();

        if(accountabilities.containsKey(accountabilityKey))
            accountabilities.get(accountabilityKey).setAccountedEntities(Arrays.asList(accountability));
        else {
            AccountabilityType accountabilityType = type.getAccountabilityType(name);
            if (accountabilityType != null) {
                Accountability newAccountability;
                if (label != null)
                    newAccountability = new LabeledAccountability(_type, label, Arrays.asList(accountability));
                else
                    newAccountability = new Accountability(_type, Arrays.asList(accountability));
                accountabilities.put(accountabilityKey, newAccountability);
            }
        }
    }

    public void setAccountability(String name, Entity ...accountability) throws AOMException {
        _setAccountability(name, null, accountability);
    }

    public void setAccountability(String name, String label, Entity ...accountability) throws AOMException {
        _setAccountability(name, label, accountability);
    }

    public Accountability getAccountability(String name) {
        return accountabilities.get(
                AccountabilityKeyFactory.create().add("name", name).build()
        );
    }

    public Entity getAccountedEntity(String name) throws AOMException {
        Accountability accountability = this.getAccountability(name);
        if(accountability == null)
            return null;
        return accountability.getAccountedEntity();
    }

    public Entity getAccountedEntity(String name, String label) throws AOMException {
        Accountability accountability = this.getAccountability(name, label);
        if(accountability == null)
            return null;
        return accountability.getAccountedEntity();
    }

    public List<Entity> getAccountedEntities(String name) throws AOMException {
        Accountability accountability = this.getAccountability(name);
        if(accountability == null)
            return null;
        return accountability.getAccountedEntities();
    }

    public LabeledAccountability getAccountability(String name, String label) {
        return (LabeledAccountability) accountabilities.get(
                AccountabilityKeyFactory.create().add("name", name).add("label", label).build()
        );
    }

    public Object getProperty(String name) {
        if(properties.containsKey(name))
            return properties.get(name).getValue();
        return null;
    }

    public EntityType getType() {
        return type;
    }
}
