package it.unibz;

import com.fasterxml.jackson.databind.node.ObjectNode;
import it.unibz.aom.Aom;
import it.unibz.utils.JsonFetcher;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(System.in);

        System.out.println("Enter the URL of the swagger endpoint: ");
        String url = in.nextLine();

        //https://tourism.opendatahub.com/swagger/v1/swagger.json

        System.out.println("Fetching swagger from " + url + "...");
        ObjectNode swagger = new JsonFetcher().fetchSwagger(url);
        System.out.println("Swagger fetched successfully!");

    }
}