package it.unibz.parsers.schema;

import com.fasterxml.jackson.databind.node.ObjectNode;

public interface SchemaParsable {

    void parse(String name, ObjectNode jsonObj);

}
