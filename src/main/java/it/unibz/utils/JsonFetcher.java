package it.unibz.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;

public class JsonFetcher extends Fetcher {

    public ObjectNode fetchJson(String url) throws IOException {
        //Fetch Swagger JSON from endpoint
        String jsonString = super.fetch(url);

        //Parse JSON
        return new ObjectMapper().readValue(jsonString, ObjectNode.class);
    }

}
