package it.unibz.parsers.data;

import com.fasterxml.jackson.databind.node.ObjectNode;
import it.unibz.aom.Aom;
import it.unibz.aom.typesquare.Entity;

public class DataParser {

    private Aom aom;
    private ObjectNode data;
    private String rootComponent;

    public DataParser(Aom aom, ObjectNode data, String rootComponent) {
        this.aom = aom;
        this.data = data;

        if (rootComponent.startsWith("#/components/schemas/"))
            rootComponent = rootComponent.substring("#/components/schemas/".length());
        this.rootComponent = rootComponent;
    }

    public Entity parse() {
        return null;
    }

}
