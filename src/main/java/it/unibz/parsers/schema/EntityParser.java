package it.unibz.parsers.schema;

import com.fasterxml.jackson.databind.node.ObjectNode;
import it.unibz.aom.accountability.AccountabilityType;
import it.unibz.aom.typesquare.EntityType;

import static it.unibz.utils.NameScoper.getRawName;

public class EntityParser implements Parsable{

    Parser parser;

    public EntityParser(Parser parser) {
        this.parser = parser;
    }

    @Override
    public void parse(String name, ObjectNode jsonObj) {
        System.out.println("Parsing entity type: " + name);

        if (parser.getAom().getEntityType(name) != null && parser.getParserStack().peek() != null) { //TODO: fix when entity has been parsed out-of-scope previously
            //Already parsed. Add reference
            System.out.println("[1] Setting ref: " + parser.getParserStack().peek().getName() + " -> " + name);
            parser.getParserStack().peek().addAccountabilityType(
                    new AccountabilityType(
                            getRawName(name),
                            parser.getAom().getEntityType(name)
                    )
            );
            return;
        }

        EntityType entityType = new EntityType(name);
        EntityType currentEntityType = parser.getParserStack().peek();
        parser.getParserStack().push(entityType);

        if (jsonObj.get("additionalProperties").isObject()) {
            //Skip intermediate EntityType creation and link directly with label property
            if (jsonObj.get("additionalProperties").has("type")) {
                parser.parse(name + "/_", (ObjectNode) jsonObj.get("additionalProperties"));
                System.out.println("[4] Setting ref: " + currentEntityType.getName() + " -> " + entityType.getName());
                AccountabilityType accountabilityType = new AccountabilityType(getRawName(name), entityType);
                accountabilityType.addProperty("labeled");
                currentEntityType.addAccountabilityType(accountabilityType);
            } else if (jsonObj.get("additionalProperties").has("$ref")) {
                String ref = jsonObj.get("additionalProperties").get("$ref").asText();
                String refName = ref.replaceFirst("#/components/schemas/", "");
                EntityType refEntityType = parser.getAom().getEntityType(refName);
                //Parse ref objects first
                if (refEntityType == null) {
                    parser.parse(refName, null);
                    refEntityType = parser.getAom().getEntityType(refName);
                }
                //Add accountability type
                System.out.println("[2] Setting ref: " + currentEntityType.getName() + " -> " + refEntityType.getName() + " (labeled)");
                AccountabilityType accountabilityType = new AccountabilityType(getRawName(refEntityType.getName()), refEntityType);
                accountabilityType.addProperty("labeled");
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
