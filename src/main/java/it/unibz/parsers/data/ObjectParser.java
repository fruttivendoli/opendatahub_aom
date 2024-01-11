package it.unibz.parsers.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import it.unibz.aom.Aom;
import it.unibz.aom.typesquare.Entity;

public class ObjectParser {

    private Aom aom;

    public ObjectParser(Aom aom) {
        this.aom = aom;
    }

    public void parse(Entity parent, String key, JsonNode objectNode) {
        System.out.println(key);
        System.out.println(objectNode);

    }

}
