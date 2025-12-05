package com.httpclient.websocket;

import com.httpclient.util.Config;

import java.util.concurrent.TimeUnit;

/**
 * Demo WebSocket functionality with echo server
 */
public class WebSocketDemo {

    public static void main(String[] args) {
        System.out.println("=== WebSocket Demo ===\n");

        SimpleWebSocketClient client = new SimpleWebSocketClient();

        try {
            // 1. Connect to WebSocket echo server
            client.connect(Config.WEBSOCKET_ECHO_URL);

            // Small delay to ensure connection is ready
            TimeUnit.SECONDS.sleep(1);

            // 2. Send multiple test messages
            System.out.println("\n--- Sending Test Messages ---");

            client.sendMessage("Hello WebSocket!");
            TimeUnit.SECONDS.sleep(1);

            client.sendMessage("This is Java 25 HTTP Client");
            TimeUnit.SECONDS.sleep(1);

            client.sendMessage("Testing real-time communication");
            TimeUnit.SECONDS.sleep(1);

            // 3. Wait for all responses
            System.out.println("\n--- Waiting for responses ---");
            client.waitForCompletion();

            // 4. Show received messages
            System.out.println("\n--- All Received Messages ---");
            System.out.println(client.getReceivedMessages());

        } catch (Exception e) {
            System.err.println("❌ Error in WebSocket demo: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // 5. Cleanup
            client.close();
            System.out.println("\n✅ WebSocket demo completed");
        }
    }

    /**
     * Simple echo test - connect, send one message, receive echo
     */
    public static void quickEchoTest() throws Exception {
        SimpleWebSocketClient client = new SimpleWebSocketClient();
        client.connect(Config.WEBSOCKET_ECHO_URL);

        TimeUnit.SECONDS.sleep(1);
        client.sendMessage("Quick test message");

        TimeUnit.SECONDS.sleep(2);
        System.out.println("Received: " + client.getReceivedMessages());

        client.close();
    }
}