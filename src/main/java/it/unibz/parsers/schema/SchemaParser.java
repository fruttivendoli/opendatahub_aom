package it.unibz.parsers.schema;

import com.fasterxml.jackson.databind.node.ObjectNode;
import it.unibz.aom.Aom;
import it.unibz.aom.accountability.AccountabilityType;
import it.unibz.aom.typesquare.EntityType;

import static it.unibz.utils.NameScoper.getRawName;

public class SchemaParser implements SchemaParsable {

    private final Aom aom;
    private SchemaParserStack parserStack;
    ObjectNode schema;

    EntityParser entityParser;
    PropertyParser propertyParser;
    ArrayParser arrayParser;
    RefParser refParser;

    public SchemaParser(ObjectNode swagger) {
        String title = swagger.get("info").get("title").asText();
        String description = swagger.get("info").get("description").asText();
        aom = new Aom(title, description);

        parserStack = new SchemaParserStack();

        schema = (ObjectNode) swagger.get("components").get("schemas");

        entityParser = new EntityParser(this);
        propertyParser = new PropertyParser(this);
        arrayParser = new ArrayParser(this);
        refParser = new RefParser(this);

        schema.fieldNames().forEachRemaining(entityTypeName -> {
            ObjectNode entityType = (ObjectNode) schema.get(entityTypeName);
            this.parse(entityTypeName, entityType);
        });
    }

    @Override
    public void parse(String name, ObjectNode objectNode) {

        if (objectNode.has("$ref")) {
            System.out.println("Parsing ref: " + name);
            refParser.parse(name, objectNode);
        } else if(objectNode.has("type") && objectNode.get("type").asText().equals("object")) {
            entityParser.parse(name, objectNode);
        } else if(objectNode.has("type") && objectNode.get("type").asText().equals("array")) {
            arrayParser.parse(name, objectNode);
        } else if (objectNode.has("type")) {
            propertyParser.parse(name, objectNode);
        } else {
            System.out.println("Failed to parse " + name + " (unknown type)");
        }

    }


    /**
     * Parse a schema component that is out of scope.
     * That means that the component may not be parsed yet.
     * It may also be the case that some parent components are not parsed yet.
     * @param name Name of the component to parse
     */
    public void parseOutOfScope(String name) {
        System.out.println("Starting out-of-scope parsing of " + name);

        //Simulate Stack for out-of-scope parsing. Backup current stack and restore it after parsing
        SchemaParserStack _parserStack = parserStack;
        SchemaParserStack simulatedStack = new SchemaParserStack();
        parserStack = simulatedStack;

        String[] nameParts = name.split("/");
        if (nameParts.length == 0) nameParts = new String[]{name};

        //Parse parent schema components first until getting to the ref node

        ObjectNode currentNode = schema;
        StringBuilder currentScope = new StringBuilder();


        for (int i = 0; i < nameParts.length; i++) {
            //Set currentNode and currentScope
            currentNode = (ObjectNode) currentNode.get(nameParts[i]);
            if (i > 0) currentScope.append("/" + nameParts[i]);
            else currentScope.append(nameParts[i]);

            //Parse currentNode
            EntityType current = aom.getEntityType(currentScope.toString());
            if (current == null) {
                parse(currentScope.toString(), currentNode);
                current = aom.getEntityType(currentScope.toString());
            }

            if (i == 0) simulatedStack.push(current);
            else simulatedStack.push(simulatedStack.peek().getAccountabilityType(nameParts[i]).getAccountedType());
        }

        //Restore stack
        parserStack = _parserStack;
    }


    public Aom getAom() {
        return aom;
    }

    public SchemaParserStack getParserStack() {
        return parserStack;
    }
}
