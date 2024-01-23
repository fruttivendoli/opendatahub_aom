package it.unibz.parsers.schema;

import com.fasterxml.jackson.databind.node.ObjectNode;
import it.unibz.aom.accountability.AccountabilityType;
import it.unibz.aom.typesquare.EntityType;

import static it.unibz.utils.NameScoper.getRawName;

public class EntityParser implements SchemaParsable {

    SchemaParser parser;

    public EntityParser(SchemaParser parser) {
        this.parser = parser;
    }

    @Override
    public void parse(String name, ObjectNode jsonObj) {
        System.out.println("Parsing entity type: " + name);

        EntityType entityType = new EntityType(name);
        EntityType currentEntityType = parser.getParserStack().peek();
        parser.getParserStack().push(entityType);

        if (jsonObj.get("additionalProperties").isObject()) {

            if (jsonObj.get("additionalProperties").has("type")) {
                if (jsonObj.get("additionalProperties").get("type").asText().equals("object")) {
                    parser.parse(name + "/_", (ObjectNode) jsonObj.get("additionalProperties"));
                    System.out.println("[4] Setting ref: " + currentEntityType.getName() + " -> " + entityType.getName());
                    AccountabilityType accountabilityType = new AccountabilityType(getRawName(name), entityType);
                    accountabilityType.setLabeled(true);
                    currentEntityType.addAccountabilityType(accountabilityType);
                } else {
                    parser.parse(name + "/_", (ObjectNode) jsonObj.get("additionalProperties"));

                    System.out.println("[4] Setting ref: " + currentEntityType.getName() + " -> " + entityType.getName() + " (labeled)");
                    AccountabilityType accountabilityType = new AccountabilityType(getRawName(name), entityType);
                    accountabilityType.setLabeled(true);
                    currentEntityType.addAccountabilityType(accountabilityType);

                    parser.getAom().addEntityType(entityType);
                }
            } else if (jsonObj.get("additionalProperties").has("$ref")) { //TODO: duplicated (1)
                String ref = jsonObj.get("additionalProperties").get("$ref").asText();
                String refName = ref.replaceFirst("#/components/schemas/", "");
                EntityType refEntityType = parser.getAom().getEntityType(refName);
                //Parse ref objects first
                if (refEntityType == null) {
                    parser.parseOutOfScope(refName);
                    refEntityType = parser.getAom().getEntityType(refName);
                }
                //Add accountability type
                System.out.println("[2] Setting ref: " + currentEntityType.getName() + " -> " + refEntityType.getName() + " (labeled)");
                AccountabilityType accountabilityType = new AccountabilityType(getRawName(name), refEntityType);
                accountabilityType.setLabeled(true);
                currentEntityType.addAccountabilityType(accountabilityType);
            }

            parser.getParserStack().pop();
            return;
        }

        if (currentEntityType == null)
            parser.getAom().addEntityType(entityType);
        else{
            System.out.println("[3] Setting ref: " + currentEntityType.getName() + " -> " + name);
            currentEntityType.addAccountabilityType(
                    new AccountabilityType(
                            getRawName(name),
                            entityType
                    )
            );
        }

        if (!jsonObj.has("properties")) {
            // todo: we need to fix in post
            parser.getParserStack().pop();
            return;
        }

        ObjectNode properties = (ObjectNode) jsonObj.get("properties");

        properties.fieldNames().forEachRemaining(propertyName -> {
            ObjectNode property = (ObjectNode) properties.get(propertyName);
            parser.parse(buildScope(propertyName), property);
        });

        parser.getParserStack().pop();
    }

    private String buildScope(String name) {
        return parser.getParserStack().getScope() + "/" + name;
    }
}
