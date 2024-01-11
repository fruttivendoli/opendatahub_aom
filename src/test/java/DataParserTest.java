import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import it.unibz.aom.Aom;
import it.unibz.aom.typesquare.Entity;
import it.unibz.parsers.data.DataParser;
import it.unibz.parsers.schema.SchemaParser;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;

import java.io.File;

public class DataParserTest {

    private static Aom aom;
    private static ObjectNode data;

    @BeforeAll
    public static void setUp() {
        //Build Schema
        File file = new File(DataParserTest.class.getResource("/exampleResponse1.json").getFile());
        String jsonString = null;
        try {
            jsonString = new String(java.nio.file.Files.readAllBytes(file.toPath()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        ObjectNode swagger = null;
        try {
            swagger = new ObjectMapper().readValue(jsonString, ObjectNode.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        aom = new SchemaParser(swagger).getAom();

        //Example response
        File responseFile = new File(DataParserTest.class.getResource("/exampleResponse1.json").getFile());
        String responseStr = null;
        try {
            responseStr = new String(java.nio.file.Files.readAllBytes(responseFile.toPath()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            data = new ObjectMapper().readValue(responseStr, ObjectNode.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    @Test
    public void parseFirstLevel() {
        DataParser dataParser = new DataParser(aom, data, "#/components/schemas/EventLinkedJsonResult");
        Entity events = dataParser.parse();
    }

}
