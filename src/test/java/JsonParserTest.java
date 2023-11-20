import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import it.unibz.aom.Aom;
import it.unibz.aom.EntityType;
import it.unibz.utils.AomBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

public class JsonParserTest {

    static Aom aom;

    @BeforeAll
    public static void setUp() {
        File file = new File(JsonParserTest.class.getResource("/swaggerTypesExample.json").getFile());
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
System.out.println(swagger);
        aom = AomBuilder.populateAOM(swagger);
    }

    @Test
    public void testMetadataParser() {
        assertEquals("Open Data Hub Tourism Api", aom.title);
        assertEquals("Open Data Hub Tourism Api based on .Net Core with PostgreSQL", aom.description);
    }

    @Test
    public void testSimpleEntityParser() {
        EntityType accoBadge = aom.getEntityType("AccoBadges");
        assertEquals("AccoBadges", accoBadge.getName());
        assertTrue(accoBadge.hasPropertyType("Id"));
        assertTrue(accoBadge.hasPropertyType("Self"));
    }

}
