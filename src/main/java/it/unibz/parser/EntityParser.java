package it.unibz.parser;

import com.fasterxml.jackson.databind.node.ObjectNode;
import it.unibz.aom.accountability.AccountabilityType;
import it.unibz.aom.typesquare.EntityType;

public class EntityParser implements Parsable{

    Parser parser;

    public EntityParser(Parser parser) {
        this.parser = parser;
    }

    @Override
    public void parse(String name, ObjectNode jsonObj) {
        if (parser.getAom().getEntityType(name) != null) {
            //Already parsed. Add reference
            System.out.println("[1] Setting ref: " + parser.getParserStack().peek().getName() + " -> " + name);
            parser.getParserStack().peek().addAccountabilityType(
                    new AccountabilityType(
                            name,
                            parser.getAom().getEntityType(name)
                    )
            );
            return;
        }

        EntityType entityType = new EntityType(name);

        EntityType currentEntityType = parser.getParserStack().peek();
        parser.getParserStack().push(entityType);

        if (jsonObj.get("additionalProperties").isObject()) {
            if (jsonObj.get("additionalProperties").has("type")) {
                parser.parse("", (ObjectNode) jsonObj.get("additionalProperties"));
            } else if (jsonObj.get("additionalProperties").has("$ref")) {
                System.out.println("Parsing ref entity type");
                String ref = jsonObj.get("additionalProperties").get("$ref").asText();
                String[] refPath = ref.replaceFirst("#/components/schemas/", "").split("/");
                EntityType pathProsecuter = parser.getAom().getEntityType(refPath[0]);
                for (int i = 1; i < refPath.length - 1; i++) {
                    //TODO: handle not parsed yet
                    pathProsecuter = pathProsecuter.getAccountabilityType(refPath[i]).getAccountedType();
                }
                //Add accountability type
                System.out.println("[2] Setting ref: " + entityType.getName() + " -> " + refPath[refPath.length - 1] + " (labeled)");
                AccountabilityType accountabilityType = new AccountabilityType(refPath[refPath.length - 1], pathProsecuter);
                accountabilityType.addProperty("labeled");
                entityType.addAccountabilityType(accountabilityType);
            }
        }

        if (currentEntityType == null)
            parser.getAom().addEntityType(entityType);
        else{
            System.out.println("[3] Setting ref: " + currentEntityType.getName() + " -> " + name);
            currentEntityType.addAccountabilityType(
                    new AccountabilityType(
                            name,
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
            parser.parse(propertyName, property);
        });

        parser.getParserStack().pop();
    }
}
