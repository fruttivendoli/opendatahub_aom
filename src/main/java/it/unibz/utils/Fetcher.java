package it.unibz.utils;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Fetcher {

    public static String fetch(String url) throws IOException {
        //Fetch Swagger JSON from endpoint
        URL _url = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) _url.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder cache = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            cache.append(line);
            cache.append("\n");
        }
        in.close();

        return cache.toString();
    }

}
