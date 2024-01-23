package it.unibz.parsers.schema;

import com.fasterxml.jackson.databind.node.ObjectNode;
import it.unibz.aom.accountability.AccountabilityType;
import it.unibz.aom.typesquare.EntityType;

import static it.unibz.utils.NameScoper.getRawName;

public class RefParser implements SchemaParsable{

    SchemaParser parser;

    public RefParser(SchemaParser parser) {
        this.parser = parser;
    }

    @Override
    public void parse(String name, ObjectNode jsonObj) {
        //Parse ref objects first
        String ref = jsonObj.get("$ref").asText();
        String refName = ref.replaceFirst("#/components/schemas/", "");
        parser.parseOutOfScope(refName); //Parse ref in a possibly out of scope scenario
        //Set ref
        EntityType entityType = parser.getAom().getEntityType(refName);
        System.out.println("[3] Setting ref: " + parser.getParserStack().peek().getName() + " -> " + refName);
        parser.getParserStack().peek().addAccountabilityType(
                new AccountabilityType(
                        getRawName(name),
                        entityType
                )
        );
    }
}
