package it.unibz;

import com.fasterxml.jackson.databind.node.ObjectNode;
import it.unibz.aom.Aom;
import it.unibz.parsers.schema.SchemaParser;
import it.unibz.utils.JsonFetcher;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(System.in);

        System.out.println("Enter the URL of the swagger endpoint: ");
        String swaggerUrl = in.nextLine();

        //https://tourism.opendatahub.com/swagger/v1/swagger.json

        System.out.println("Fetching swagger from " + swaggerUrl + "...");
        ObjectNode swagger = new JsonFetcher().fetchSwagger(swaggerUrl);
        System.out.println("Swagger fetched successfully!");

        System.out.println("Parsing swagger...");
        SchemaParser schemaParser = new SchemaParser(swagger);
        System.out.println("Swagger parsed successfully!");

        System.out.println("Enter the URL of the data endpoint: ");
        String dataUrl = in.nextLine();
    }
}