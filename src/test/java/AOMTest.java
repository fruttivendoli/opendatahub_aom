import it.unibz.aom.*;
import it.unibz.aom.accountability.AccountabilityType;
import it.unibz.aom.typesquare.Entity;
import it.unibz.aom.typesquare.EntityType;
import it.unibz.aom.typesquare.PropertyType;
import org.junit.Test;
import static org.junit.Assert.*;

public class AOMTest {
    @Test
    public void basicAomStructure() throws AOMException {
        EntityType person = new EntityType("person");
        person.addPropertyType(new PropertyType("name", String.class, true, true));
        person.addPropertyType(new PropertyType("age", Integer.class, true, true));

        Entity jeff = person.create();

        jeff.setProperty("name", "Jeff");
        jeff.setProperty("age", 42);

        assertEquals("Jeff", jeff.getProperty("name"));
        assertEquals(42, jeff.getProperty("age"));

    }

    @Test
    public void accountabilityStructure() throws AOMException {
        //Create Types

        EntityType house = new EntityType("house");
        house.addPropertyType(new PropertyType("owner", String.class, true, true));
        house.addPropertyType(new PropertyType("area", Double.class, true, true));

        EntityType address = new EntityType("address");
        address.addPropertyType(new PropertyType("street", String.class, true, true));
        address.addPropertyType(new PropertyType("number", Integer.class, true, true));
        address.addPropertyType(new PropertyType("city", String.class, true, true));
        address.addPropertyType(new PropertyType("country", String.class, true, true));

        house.addAccountabilityType(new AccountabilityType("address", address));

        //Test with data

        Entity house1 = house.create();
        house1.setProperty("owner", "Jeff");
        house1.setProperty("area", 100.0);

        Entity address1 = address.create();
        address1.setProperty("street", "Via dei Mille");
        address1.setProperty("number", 1);
        address1.setProperty("city", "Bolzano");
        address1.setProperty("country", "Italy");

        house1.setAccountability("address", address1);

        assertEquals("Via dei Mille", house1.getAccountability("address").getAccountedEntity().getProperty("street"));
        assertEquals(1, house1.getAccountability("address").getAccountedEntity().getProperty("number"));
        assertEquals("Bolzano", house1.getAccountability("address").getAccountedEntity().getProperty("city"));
        assertEquals("Italy", house1.getAccountability("address").getAccountedEntity().getProperty("country"));
    }

    @Test
    public void test1ToNAccountability() throws AOMException {
        // Create Types

        EntityType room = new EntityType("car");
        room.addPropertyType(new PropertyType("owner", String.class, true, true));

        EntityType student = new EntityType("student");
        student.addPropertyType(new PropertyType("name", String.class, true, true));
        student.addPropertyType(new PropertyType("age", Integer.class, true, true));

        room.addAccountabilityType(new AccountabilityType("student", student));

        // Test with data

        Entity room1 = room.create();
        room1.setProperty("owner", "Jeff");

        Entity student1 = student.create();
        student1.setProperty("name", "Jeff");
        student1.setProperty("age", 42);

        Entity student2 = student.create();
        student2.setProperty("name", "John");
        student2.setProperty("age", 43);

        Entity student3 = student.create();
        student3.setProperty("name", "Jack");
        student3.setProperty("age", 44);

        room1.setAccountability("student", student1, student2, student3);

        // Asserts

        assertEquals("Jeff", room1.getAccountability("student").getAccountedEntity(0).getProperty("name"));
        assertEquals(42, room1.getAccountability("student").getAccountedEntity(0).getProperty("age"));
        assertEquals("John", room1.getAccountability("student").getAccountedEntity(1).getProperty("name"));
        assertEquals(43, room1.getAccountability("student").getAccountedEntity(1).getProperty("age"));
        assertEquals("Jack", room1.getAccountability("student").getAccountedEntity(2).getProperty("name"));
        assertEquals(44, room1.getAccountability("student").getAccountedEntity(2).getProperty("age"));
    }

    @Test
    public void testSetNonExisting() {
        EntityType person = new EntityType("Person");
        person.addPropertyType(new PropertyType("name", String.class, true, true));
        person.addPropertyType(new PropertyType("age", Integer.class, true, true));

        Entity jeff = person.create();
        assertThrows(AOMException.class , () -> jeff.setProperty("height", 1.8));
    }

    @Test
    public void testSetWrongType() {
        EntityType person = new EntityType("Person");
        person.addPropertyType(new PropertyType("name", String.class, true, true));
        person.addPropertyType(new PropertyType("age", Integer.class, true, true));

        Entity jeff = person.create();
        assertThrows(AOMException.class , () -> jeff.setProperty("age", "Jeff"));
    }

    @Test
    public void testMapAccountability() throws AOMException {
        //Define Schema

        EntityType person = new EntityType("person");
        person.addPropertyType(new PropertyType("name", String.class, true, true));
        person.addPropertyType(new PropertyType("age", Integer.class, true, true));

        EntityType house = new EntityType("house");
        house.addPropertyType(new PropertyType("area", Double.class, true, true));

        house.addAccountabilityType(new AccountabilityType("person", person)); //todo introduce parameter to require a label

        //Populate with data

        Entity jeff = person.create();
        jeff.setProperty("name", "Jeff");
        jeff.setProperty("age", 42);

        Entity john = person.create();
        john.setProperty("name", "John");
        john.setProperty("age", 12);

        Entity villa = house.create();
        villa.setProperty("area", 100.0);

        villa.setAccountability("person", "adults", jeff);
        villa.setAccountability("person", "children", john);

        //Asserts

        assertEquals("Jeff", villa.getAccountability("person", "adults").getAccountedEntity().getProperty("name"));
        assertEquals(42, villa.getAccountability("person", "adults").getAccountedEntity().getProperty("age"));
        assertEquals("John", villa.getAccountability("person", "children").getAccountedEntity().getProperty("name"));
        assertEquals(12, villa.getAccountability("person", "children").getAccountedEntity().getProperty("age"));
    }

    @Test
    public void testHashmap() throws AOMException {
        //Define Schema
        EntityType mapping = new EntityType("Name");
        EntityType mappingNested = new EntityType("mappingNested");
        mappingNested.addPropertyType(new PropertyType("_", String.class, true, true));

        mapping.addAccountabilityType(new AccountabilityType("_", mappingNested));

        //Populate with data

        Entity mapping1 = mapping.create();

        //Create map

        //First entry
        Entity mappingNested1 = mappingNested.create();
        mappingNested1.setProperty("_", "value1");
        mapping1.setAccountability("_", "key1", mappingNested1);

        //Second entry
        Entity mappingNested2 = mappingNested.create();
        mappingNested2.setProperty("_", "value2");
        mapping1.setAccountability("_", "key2", mappingNested2);

        //Asserts


    }

}
