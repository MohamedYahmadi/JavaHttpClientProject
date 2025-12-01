package com.httpclient.util;

/**
 * Configuration constants for testing APIs
 * We'll use public APIs that don't require authentication
 */
public class Config {

    // REST API endpoints for testing
    public static final String JSON_PLACEHOLDER_URL = "https://jsonplaceholder.typicode.com";
    public static final String GET_POSTS_URL = JSON_PLACEHOLDER_URL + "/posts";
    public static final String GET_POST_URL = JSON_PLACEHOLDER_URL + "/posts/1";
    public static final String CREATE_POST_URL = JSON_PLACEHOLDER_URL + "/posts";

    // HTTP Bin for various HTTP methods testing
    public static final String HTTP_BIN_URL = "https://httpbin.org";
    public static final String HTTP_BIN_GET = HTTP_BIN_URL + "/get";
    public static final String HTTP_BIN_POST = HTTP_BIN_URL + "/post";
    public static final String HTTP_BIN_PUT = HTTP_BIN_URL + "/put";
    public static final String HTTP_BIN_DELETE = HTTP_BIN_URL + "/delete";

    // WebSocket test endpoints
    public static final String WEBSOCKET_ECHO_URL = "wss://echo.websocket.org";
    public static final String WEBSOCKET_TEST_URL = "wss://ws.postman-echo.com/raw";

    // Timeout configurations
    public static final int CONNECT_TIMEOUT_SECONDS = 10;
    public static final int REQUEST_TIMEOUT_SECONDS = 30;

    // Performance testing
    public static final int CONCURRENT_REQUESTS = 10;
    public static final int TOTAL_REQUESTS = 100;
}