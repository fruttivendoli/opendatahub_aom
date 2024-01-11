package it.unibz.parser;

import com.fasterxml.jackson.databind.node.ObjectNode;
import it.unibz.aom.typesquare.EntityType;
import it.unibz.aom.typesquare.PropertyType;
import it.unibz.utils.SimpleTypeMapper;

import static it.unibz.utils.NameScoper.getRawName;

public class PropertyParser implements Parsable{

    Parser parser;

    public PropertyParser(Parser parser) {
        this.parser = parser;
    }

    @Override
    public void parse(String name, ObjectNode jsonObj) {
        name = getRawName(name);

        EntityType entityType = this.parser.getParserStack().peek();
        boolean nullable = jsonObj.has("nullable") && jsonObj.get("nullable").asBoolean();
        boolean readOnly = jsonObj.has("readOnly") && jsonObj.get("readOnly").asBoolean();

        Class<?> type = SimpleTypeMapper.getSimpleType(jsonObj.get("type").asText());

        PropertyType _type = new PropertyType(name, type, nullable, readOnly);
        entityType.addPropertyType(_type);
    }

}
