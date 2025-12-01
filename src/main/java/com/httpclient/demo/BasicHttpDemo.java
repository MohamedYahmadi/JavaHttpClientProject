package com.httpclient.demo;

import com.httpclient.util.Config;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * Basic demonstration of Java HTTP Client
 * Step 1: Simple synchronous GET request
 */
public class BasicHttpDemo {

    public static void main(String[] args) {
        System.out.println("=== Starting Basic HTTP Client Demo ===\n");

        try {
            // 1. Create HttpClient with default configuration
            HttpClient client = HttpClient.newHttpClient();

            // 2. Create HttpRequest
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(Config.GET_POST_URL))
                    .timeout(Duration.ofSeconds(Config.REQUEST_TIMEOUT_SECONDS))
                    .header("User-Agent", "Java 25 HTTP Client Demo")
                    .GET()  // This is optional, GET is default
                    .build();

            System.out.println("Making GET request to: " + Config.GET_POST_URL);

            // 3. Send request and get response (synchronous)
            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());

            // 4. Process response
            System.out.println("\n=== Response Details ===");
            System.out.println("Status Code: " + response.statusCode());
            System.out.println("Response Headers:");
            response.headers().map().forEach((k, v) ->
                    System.out.println("  " + k + ": " + v));

            System.out.println("\nResponse Body (first 500 chars):");
            String body = response.body();
            System.out.println(body.substring(0, Math.min(body.length(), 500)) +
                    (body.length() > 500 ? "..." : ""));

            // 5. Check if request was successful
            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                System.out.println("\n✅ Request successful!");
            } else {
                System.out.println("\n❌ Request failed with status: " + response.statusCode());
            }

        } catch (Exception e) {
            System.err.println("❌ Error during HTTP request: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\n=== Demo Complete ===");
    }

    /**
     * Helper method to make a simple GET request
     */
    public static String makeSimpleGetRequest(String url) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        return response.body();
    }
}