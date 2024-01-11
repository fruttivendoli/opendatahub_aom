package it.unibz.parser;

import com.fasterxml.jackson.databind.node.ObjectNode;
import it.unibz.aom.typesquare.PropertyType;
import it.unibz.utils.SimpleTypeMapper;

public class ArrayParser implements Parsable{

    Parser parser;

    public ArrayParser(Parser parser) {
        this.parser = parser;
    }

    @Override
    public void parse(String name, ObjectNode jsonObj) {
        ObjectNode items = (ObjectNode) jsonObj.get("items");
        if(items.has("$ref")) {
            // todo: parse nested objects
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
