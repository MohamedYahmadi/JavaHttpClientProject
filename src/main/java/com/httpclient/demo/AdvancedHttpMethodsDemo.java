package com.httpclient.demo;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import com.google.gson.Gson;
import com.httpclient.util.User;

import java.util.concurrent.CompletableFuture;

public class AdvancedHttpMethodsDemo {

    private static final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .version(HttpClient.Version.HTTP_2)
            .build();

    private static final Gson gson = new Gson();

    // ------------ GET + JSON PARSING ------------
    public static void getUserById(int userId) throws Exception {
        System.out.println("--- GET USER (JSON) ---");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://jsonplaceholder.typicode.com/users/" + userId))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Status: " + response.statusCode());
        System.out.println("Raw JSON: " + response.body());

        User user = gson.fromJson(response.body(), User.class);
        System.out.println("Parsed User: " + user);
    }

    // ------------ POST RAW BODY ------------
    public static void postRaw() throws Exception {
        System.out.println("--- POST RAW DATA ---");

        String body = "title=Hello&body=World&userId=1";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://jsonplaceholder.typicode.com/posts"))
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Status: " + response.statusCode());
        System.out.println("Response: " + response.body());
    }

    // ------------ POST JSON ------------
    public static void postJson() throws Exception {
        System.out.println("--- POST JSON ---");

        User user = new User(1, "Skander", "skander@example.com");
        String json = gson.toJson(user);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://jsonplaceholder.typicode.com/users"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Status: " + response.statusCode());
        System.out.println("Sent JSON: " + json);
        System.out.println("Response: " + response.body());
    }

    // ------------ PUT JSON ------------
    public static void putJson(int userId) throws Exception {
        System.out.println("--- PUT JSON ---");

        User updated = new User(userId, "Updated Name", "updated@example.com");
        String json = gson.toJson(updated);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://jsonplaceholder.typicode.com/users/" + userId))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Status: " + response.statusCode());
        System.out.println("Updated JSON: " + json);
        System.out.println("Response: " + response.body());
    }

    // ------------ DELETE ------------
    public static void deleteUser(int userId) throws Exception {
        System.out.println("--- DELETE USER ---");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://jsonplaceholder.typicode.com/users/" + userId))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Status: " + response.statusCode());
        System.out.println("Response: " + response.body());
    }


    // ------------ ASYNC POST JSON ------------
    public static void postJsonAsync() {
        System.out.println("--- ASYNC POST JSON ---");

        User user = new User(99, "Async Test", "async@example.com");
        String json = gson.toJson(user);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://jsonplaceholder.typicode.com/users"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        CompletableFuture<HttpResponse<String>> future =
                client.sendAsync(request, HttpResponse.BodyHandlers.ofString());

        future.thenApply(HttpResponse::body)
                .thenAccept(body -> {
                    System.out.println("Async Response: " + body);
                })
                .join();
    }


    public static void main(String[] args) throws Exception {

        getUserById(1);
        System.out.println();

        postRaw();
        System.out.println();

        postJson();
        System.out.println();

        putJson(1);
        System.out.println();

        deleteUser(1);
        System.out.println();

        postJsonAsync();
    }
}
