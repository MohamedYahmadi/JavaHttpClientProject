package com.httpclient.util;

import java.net.http.HttpClient;
import java.time.Duration;

/**
 * Factory for creating configured HttpClient instances
 */
public class    HttpClientFactory {

    /**
     * Create a basic HTTP/2 enabled client
     */
    public static HttpClient createHttp2Client() {
        return HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)  // Force HTTP/2
                .connectTimeout(Duration.ofSeconds(Config.CONNECT_TIMEOUT_SECONDS))
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
    }

    /**
     * Create an HTTP/1.1 client for comparison
     */
    public static HttpClient createHttp11Client() {
        return HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)  // Force HTTP/1.1
                .connectTimeout(Duration.ofSeconds(Config.CONNECT_TIMEOUT_SECONDS))
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
    }

    /**
     * Create a client with custom timeout
     */
    public static HttpClient createClientWithTimeout(int timeoutSeconds) {
        return HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(timeoutSeconds))
                .build();
    }
}