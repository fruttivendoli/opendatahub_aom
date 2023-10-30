import it.unibz.aom.AOMException;
import it.unibz.aom.Entity;
import it.unibz.aom.EntityType;
import it.unibz.aom.PropertyType;
import org.junit.Test;
import static org.junit.Assert.*;

public class AOMTest {
    @Test
    public void basicAomStructure() throws AOMException {
        EntityType person = new EntityType("person");
        person.addPropertyType(new PropertyType("name", String.class));
        person.addPropertyType(new PropertyType("age", Integer.class));

        Entity jeff = person.create();

        jeff.setProperty("name", "Jeff");
        jeff.setProperty("age", 42);

        assertEquals("Jeff", jeff.getProperty("name"));
        assertEquals(42, jeff.getProperty("age"));

    }

    @Test
    public void testSetNonExisting() {
        EntityType person = new EntityType("Person");
        person.addPropertyType(new PropertyType("name", String.class));
        person.addPropertyType(new PropertyType("age", Integer.class));

        Entity jeff = person.create();
        assertThrows(AOMException.class , () -> jeff.setProperty("height", 1.8));
    }

    @Test
    public void testSetWrongType() {
        EntityType person = new EntityType("Person");
        person.addPropertyType(new PropertyType("name", String.class));
        person.addPropertyType(new PropertyType("age", Integer.class));

        Entity jeff = person.create();
        assertThrows(AOMException.class , () -> jeff.setProperty("age", "Jeff"));
    }

}
