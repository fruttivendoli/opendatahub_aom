package it.unibz;

import com.fasterxml.jackson.databind.node.ObjectNode;
import it.unibz.aom.typesquare.Entity;
import it.unibz.parsers.data.DataParser;
import it.unibz.parsers.schema.SchemaParser;
import it.unibz.utils.JsonFetcher;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(System.in);

        System.out.println("Enter the URL of the swagger endpoint: ");
        String swaggerUrl = in.nextLine();

        //https://drive.google.com/uc?id=17mfXsNxgHs-dDg9uzhGMAon1i5ymEDoi&export=download

        //https://tourism.opendatahub.com/swagger/v1/swagger.json
        if (swaggerUrl.isEmpty())
            swaggerUrl = "https://tourism.opendatahub.com/swagger/v1/swagger.json";

        System.out.println("Fetching swagger from " + swaggerUrl + "...");
        ObjectNode swagger = new JsonFetcher().fetchJson(swaggerUrl);
        System.out.println("Swagger fetched successfully!");

        System.out.println("Parsing swagger...");
        SchemaParser schemaParser = new SchemaParser(swagger);
        System.out.println("Swagger parsed successfully!");

        System.out.println("Enter the URL of the data endpoint: ");
        String dataUrl = in.nextLine();

        //https://tourism.opendatahub.com/v1/Event
        if (dataUrl.isEmpty())
            dataUrl = "https://tourism.opendatahub.com/v1/Event";

        System.out.println("Fetching data from " + dataUrl + "...");
        ObjectNode data = new JsonFetcher().fetchJson(dataUrl);
        System.out.println("Data fetched successfully!");

        String rootEntityType = "EventLinked";
        DataParser dataParser = new DataParser(schemaParser.getAom(), data, rootEntityType);
        System.out.println("Data parsed successfully!");

        System.out.println("\n\n\n----DATA----\n\n\n");
        for (Entity entity: dataParser.getEntities()) {
            entity.print(entity.getType().getName(), "");
        }
    }
}