package it.unibz.parsers.data;

import com.fasterxml.jackson.databind.node.ObjectNode;
import it.unibz.aom.Aom;
import it.unibz.aom.typesquare.Entity;
import it.unibz.aom.typesquare.EntityType;

public class DataParser {

    private Aom aom;
    private ObjectNode data;
    private String rootComponent;

    ObjectParser objectParser;
    PropertyParser propertyParser;

    public DataParser(Aom aom, ObjectNode data, String rootComponent) {
        this.aom = aom;
        this.data = data;

        if (rootComponent.startsWith("#/components/schemas/"))
            rootComponent = rootComponent.substring("#/components/schemas/".length());
        this.rootComponent = rootComponent;

        objectParser = new ObjectParser(aom);
        propertyParser = new PropertyParser(aom);
    }

    public Entity parse() {
        EntityType rootEntityType = aom.getEntityType(rootComponent);
        Entity rootEntity = rootEntityType.create();

        data.fields().forEachRemaining(field -> {
            //Check if field is a reference to a component
            if (rootEntityType.getAccountabilityType(field.getKey()) != null) {
                //Parse the object
                objectParser.parse(rootEntity, field.getKey(), (ObjectNode) field.getValue());
            } else {
                //Parse the property
                propertyParser.parse(rootEntity, field.getKey(), field.getValue());
            }
        });

        return rootEntity;
    }

}
