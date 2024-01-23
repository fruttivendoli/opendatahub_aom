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
        //Check if entity type already exists
        if (parser.getAom().getEntityType(name) != null) {
            System.out.println("Entity type " + name + " already exists");
            return;
        }

        System.out.println("Parsing entity type: " + name);

        EntityType newEntityType = new EntityType(name);
        EntityType recentEntityType = parser.getParserStack().peek();
        parser.getParserStack().push(newEntityType);


        //Parse properties
        if (jsonObj.has("properties")) {
            ObjectNode properties = (ObjectNode) jsonObj.get("properties");

            properties.fieldNames().forEachRemaining(propertyName -> {
                ObjectNode property = (ObjectNode) properties.get(propertyName);
                parser.parse(buildScope(propertyName), property);
            });
        }


        //Parse additional properties
        if (jsonObj.get("additionalProperties").isObject()) {

            if (jsonObj.get("additionalProperties").has("type")) {
                if (jsonObj.get("additionalProperties").get("type").asText().equals("object")) {
                    parser.parse(name + "/_", (ObjectNode) jsonObj.get("additionalProperties"));
                    System.out.println("[4] Setting ref: " + recentEntityType.getName() + " -> " + newEntityType.getName());
                    AccountabilityType accountabilityType = new AccountabilityType(getRawName(name), newEntityType);
                    accountabilityType.setLabeled(true);
                    recentEntityType.addAccountabilityType(accountabilityType);

                } else {
                    parser.parse(name + "/_", (ObjectNode) jsonObj.get("additionalProperties"));

                    System.out.println("[4] Setting ref: " + recentEntityType.getName() + " -> " + newEntityType.getName() + " (labeled)");
                    AccountabilityType accountabilityType = new AccountabilityType(getRawName(name), newEntityType);
                    accountabilityType.setLabeled(true);
                    recentEntityType.addAccountabilityType(accountabilityType);

                    parser.getAom().addEntityType(newEntityType);
                }

            } else if (jsonObj.get("additionalProperties").has("$ref")) {
                String ref = jsonObj.get("additionalProperties").get("$ref").asText();
                String refName = ref.replaceFirst("#/components/schemas/", "");
                EntityType refEntityType = parser.getAom().getEntityType(refName);
                //Parse ref objects first
                if (refEntityType == null) {
                    parser.parseOutOfScope(refName);
                    refEntityType = parser.getAom().getEntityType(refName);
                }
                //Add accountability type
                System.out.println("[2] Setting ref: " + recentEntityType.getName() + " -> " + refEntityType.getName() + " (labeled)");
                AccountabilityType accountabilityType = new AccountabilityType(getRawName(name), refEntityType);
                accountabilityType.setLabeled(true);
                recentEntityType.addAccountabilityType(accountabilityType);
            }

            parser.getParserStack().pop();
            return;
        }


        //Add or link entity type
        if (recentEntityType == null) //Entity type is at root level
            parser.getAom().addEntityType(newEntityType);
        else{
            System.out.println("[3] Setting ref: " + recentEntityType.getName() + " -> " + name);
            recentEntityType.addAccountabilityType(
                    new AccountabilityType(
                            getRawName(name),
                            newEntityType
                    )
            );
        }



        parser.getParserStack().pop();
    }

    private String buildScope(String name) {
        return parser.getParserStack().getScope() + "/" + name;
    }
}
