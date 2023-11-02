import com.fasterxml.jackson.databind.node.ObjectNode;
import it.unibz.utils.JsonFetcher;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class JsonFetcherTest {

    @Test
    public void testFetchSwagger() throws IOException {
        JsonFetcher jsonFetcher = new JsonFetcher();

        ObjectNode swagger = jsonFetcher.fetchSwagger("https://petstore.swagger.io/v2/swagger.json");

        assertEquals("2.0", swagger.get("swagger").asText());
    }

}
