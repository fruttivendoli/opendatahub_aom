import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import it.unibz.aom.Aom;
import it.unibz.utils.AomBuilder;
import org.junit.Test;

import java.io.File;

public class AomBuilderTest {

    @Test
    public void testBuildWithJson() throws Exception{
        ObjectNode swagger = new ObjectMapper().readValue(
                AomBuilderTest.class.getResourceAsStream("/swaggerTypesExample.json"),
                ObjectNode.class
        );
        Aom aom = AomBuilder.populateAOM(swagger);
    }

}
