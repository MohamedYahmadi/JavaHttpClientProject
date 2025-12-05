package com.httpclient.benchmark;

import com.httpclient.util.HttpClientFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Performance benchmarks for HTTP/1.1 vs HTTP/2 and sync vs async
 */
public class PerformanceBenchmark {

    private static final String TEST_URL = "https://jsonplaceholder.typicode.com/posts/1";
    private static final int REQUEST_COUNT = 50;

    public static void main(String[] args) throws Exception {
        System.out.println("=== HTTP Client Performance Benchmarks ===\n");

        // Test 1: HTTP/1.1 vs HTTP/2 (Synchronous)
        System.out.println("📊 Test 1: HTTP/1.1 vs HTTP/2 (Sync)");
        benchmarkSync(HttpClientFactory.createHttp11Client(), "HTTP/1.1");
        benchmarkSync(HttpClientFactory.createHttp2Client(), "HTTP/2");

        // Test 2: Sync vs Async (HTTP/2)
        System.out.println("\n📊 Test 2: Sync vs Async (HTTP/2)");
        benchmarkSync(HttpClientFactory.createHttp2Client(), "Sync");
        benchmarkAsync(HttpClientFactory.createHttp2Client(), "Async");

        System.out.println("\n✅ All benchmarks completed!");
    }

    /**
     * Benchmark synchronous requests
     */
    private static void benchmarkSync(HttpClient client, String label) throws Exception {
        System.out.println("\n  Testing " + label + "...");

        Instant start = Instant.now();
        int successCount = 0;

        for (int i = 0; i < REQUEST_COUNT; i++) {
            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(TEST_URL))
                        .timeout(Duration.ofSeconds(10))
                        .GET()
                        .build();

                HttpResponse<String> response = client.send(request,
                        HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    successCount++;
                }
            } catch (Exception e) {
                // Count as failure
            }
        }

        Instant end = Instant.now();
        long duration = Duration.between(start, end).toMillis();

        printResults(label, duration, successCount);
    }

    /**
     * Benchmark asynchronous requests
     */
    private static void benchmarkAsync(HttpClient client, String label) throws Exception {
        System.out.println("\n  Testing " + label + "...");

        Instant start = Instant.now();
        List<CompletableFuture<HttpResponse<String>>> futures = new ArrayList<>();

        // Create all requests
        for (int i = 0; i < REQUEST_COUNT; i++) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(TEST_URL))
                    .timeout(Duration.ofSeconds(10))
                    .GET()
                    .build();

            futures.add(client.sendAsync(request, HttpResponse.BodyHandlers.ofString()));
        }

        // Wait for all to complete
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(
                futures.toArray(new CompletableFuture[0])
        );

        allFutures.join(); // Wait for completion

        // Count successes
        int successCount = 0;
        for (CompletableFuture<HttpResponse<String>> future : futures) {
            try {
                HttpResponse<String> response = future.get();
                if (response.statusCode() == 200) {
                    successCount++;
                }
            } catch (Exception e) {
                // Count as failure
            }
        }

        Instant end = Instant.now();
        long duration = Duration.between(start, end).toMillis();

        printResults(label, duration, successCount);
    }

    /**
     * Print formatted results
     */
    private static void printResults(String label, long durationMs, int successCount) {
        double requestsPerSecond = (REQUEST_COUNT * 1000.0) / durationMs;
        double avgTimePerRequest = durationMs / (double) REQUEST_COUNT;
        double successRate = (successCount * 100.0) / REQUEST_COUNT;

        System.out.println("    " + label + " Results:");
        System.out.println("      Total Time: " + durationMs + " ms");
        System.out.println("      Requests/sec: " + String.format("%.2f", requestsPerSecond));
        System.out.println("      Avg Time/Request: " + String.format("%.2f", avgTimePerRequest) + " ms");
        System.out.println("      Success Rate: " + String.format("%.1f", successRate) + "%");
        System.out.println("      Successful: " + successCount + "/" + REQUEST_COUNT);
    }
}