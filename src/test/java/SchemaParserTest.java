import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import it.unibz.aom.Aom;
import it.unibz.aom.accountability.AccountabilityType;
import it.unibz.aom.typesquare.EntityType;
import it.unibz.aom.typesquare.PropertyType;
import it.unibz.parsers.schema.SchemaParser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

public class SchemaParserTest {

    static Aom aom;

    @BeforeAll
    public static void setUp() {
        File file = new File(SchemaParserTest.class.getResource("/swaggerTypesExample.json").getFile());
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
        assertEquals("Id", accoBadge.getPropertyType("Id").getName());
        PropertyType id = accoBadge.getPropertyType("Id");
        assertEquals(String.class, id.getSimpleType());
        assertTrue(accoBadge.hasPropertyType("Self"));
        assertEquals("Self", accoBadge.getPropertyType("Self").getName());
        PropertyType self = accoBadge.getPropertyType("Self");
        assertEquals(String.class, self.getSimpleType());
    }

    @Test
    public void testArrayParser() {
        EntityType accoFeature = aom.getEntityType("AccoFeature");
        assertEquals("AccoFeature", accoFeature.getName());
        assertTrue(accoFeature.hasPropertyType("RoomAmenityCodes"));
        assertEquals("RoomAmenityCodes", accoFeature.getPropertyType("RoomAmenityCodes").getName());
        PropertyType roomAmenityCodes = accoFeature.getPropertyType("RoomAmenityCodes");
        assertEquals(Integer[].class, roomAmenityCodes.getSimpleType());
    }

    @Test
    public void testAccountability() {
        EntityType accommodationRoomLinked = aom.getEntityType("AccommodationRoomLinked");
        assertNotNull(accommodationRoomLinked.getAccountabilityType("LicenseInfo").getAccountedType());
        assertEquals(accommodationRoomLinked.getAccountabilityType("LicenseInfo").getAccountedType().getName(), "LicenseInfo");
        assertNotNull(accommodationRoomLinked.getAccountabilityType("_Meta"));
    }

    @Test
    public void testLabeledAccountabilityWithRef() {
        EntityType accommodationLinked = aom.getEntityType("AccommodationLinked");
        assertNotNull(accommodationLinked.getAccountabilityType("AccoDetail").getAccountedType());
        AccountabilityType accoDetail = accommodationLinked.getAccountabilityType("AccoDetail");
        assertNotNull(accoDetail.getAccountedType());
        assertTrue(accoDetail.isLabeled());
        assertEquals("AccoDetail", accoDetail.getAccountedType().getName());
    }
//
//    @Test
//    public void testLabeledAccountabilityForNestedAdditionalProperties() {
//        EntityType accommodationLinked = aom.getEntityType("AccommodationLinked");
//        assertNotNull(accommodationLinked.getAccountabilityType("Mapping").getAccountedType());
//        AccountabilityType mapping = accommodationLinked.getAccountabilityType("Mapping");
//        assertNotNull(mapping.getAccountedType());
//        assertTrue(mapping.isLabeled());
//        AccountabilityType mappingNested = mapping.getAccountedType().getAccountabilityType("_");
//        assertNotNull(mappingNested.getAccountedType());
//        assertTrue(mappingNested.isLabeled());
//        PropertyType property = mappingNested.getAccountedType().getPropertyType("_");
//        assertNotNull(property);
//        assertEquals(String.class, property.getSimpleType());
//    }

    @Test
    public void testDifferentObjectNameThanReference() {
        EntityType accommodationLinked = aom.getEntityType("AccommodationLinked");
        assertNotNull(accommodationLinked.getAccountabilityType("LocationInfo").getAccountedType());
        AccountabilityType locationInfo = accommodationLinked.getAccountabilityType("LocationInfo");
        assertNotNull(locationInfo.getAccountedType());
        assertEquals("LocationInfoLinked", locationInfo.getAccountedType().getName());
    }

    @Test
    public void testGetSimpleAdditionalProperty() {
        EntityType tvInfoLinked = aom.getEntityType("TvInfoLinked");
        assertNotNull(tvInfoLinked.getAccountabilityType("Name").getAccountedType());
        AccountabilityType name = tvInfoLinked.getAccountabilityType("Name");
        assertNotNull(name);
        assertTrue(name.isLabeled());
    }

}
