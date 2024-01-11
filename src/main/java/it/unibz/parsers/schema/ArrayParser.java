package it.unibz.parsers.schema;

import com.fasterxml.jackson.databind.node.ObjectNode;
import it.unibz.aom.accountability.AccountabilityType;
import it.unibz.aom.typesquare.EntityType;
import it.unibz.aom.typesquare.PropertyType;
import it.unibz.utils.SimpleTypeMapper;

import static it.unibz.utils.NameScoper.getRawName;

public class ArrayParser implements SchemaParsable {

    SchemaParser parser;

    public ArrayParser(SchemaParser parser) {
        this.parser = parser;
    }

    @Override
    public void parse(String name, ObjectNode jsonObj) {
        name = getRawName(name);

        ObjectNode items = (ObjectNode) jsonObj.get("items");
        if(items.has("$ref")) {
            EntityType currentEntityType = parser.getParserStack().peek();
            String ref = items.get("$ref").asText();
            String refName = ref.replaceFirst("#/components/schemas/", "");
            EntityType refEntityType = parser.getAom().getEntityType(refName);
            if (refEntityType == null) {
                parser.parse(refName, null);
                refEntityType = parser.getAom().getEntityType(refName);
            }
            AccountabilityType accountabilityType = new AccountabilityType(name, refEntityType);
            currentEntityType.addAccountabilityType(accountabilityType);
            return;
        }

        Class<?> elementType = SimpleTypeMapper.getSimpleType(items.get("type").asText());
        parser.getParserStack().peek().addPropertyType(
                new PropertyType(
                        name,
                        java.lang.reflect.Array.newInstance(elementType, 0).getClass(),
                        jsonObj.has("nullable") && jsonObj.get("nullable").asBoolean(),
                        jsonObj.has("readOnly") && jsonObj.get("readOnly").asBoolean()
                )
        );
    }

}
