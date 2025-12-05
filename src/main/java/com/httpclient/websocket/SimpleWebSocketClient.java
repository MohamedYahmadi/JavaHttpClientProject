package com.httpclient.websocket;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Simple WebSocket client that connects to an echo server
 */
public class SimpleWebSocketClient {

    private WebSocket webSocket;
    private final CountDownLatch latch = new CountDownLatch(1);
    private StringBuilder receivedMessages = new StringBuilder();

    /**
     * Connect to a WebSocket server
     */
    public void connect(String url) throws Exception {
        System.out.println("🔗 Connecting to WebSocket: " + url);

        // Create WebSocket listener
        WebSocket.Listener listener = new WebSocket.Listener() {

            @Override
            public void onOpen(WebSocket webSocket) {
                System.out.println("✅ WebSocket connection opened");
                webSocket.request(1); // Request first message
            }

            @Override
            public CompletionStage<?> onText(WebSocket webSocket,
                                             CharSequence data,
                                             boolean last) {
                System.out.println("📨 Received: " + data);
                receivedMessages.append(data).append("\n");
                webSocket.request(1); // Request next message
                return CompletableFuture.completedFuture(null);
            }

            @Override
            public CompletionStage<?> onClose(WebSocket webSocket,
                                              int statusCode,
                                              String reason) {
                System.out.println("🔌 WebSocket closed: " + statusCode + " - " + reason);
                latch.countDown(); // Signal that connection is closed
                return CompletableFuture.completedFuture(null);
            }

            @Override
            public void onError(WebSocket webSocket, Throwable error) {
                System.err.println("❌ WebSocket error: " + error.getMessage());
                latch.countDown();
            }
        };

        // Build and connect
        CompletableFuture<WebSocket> wsFuture = HttpClient.newHttpClient()
                .newWebSocketBuilder()
                .buildAsync(URI.create(url), listener);

        this.webSocket = wsFuture.join();
        System.out.println("✅ WebSocket client ready");
    }

    /**
     * Send a message to the WebSocket server
     */
    public void sendMessage(String message) {
        if (webSocket != null && !webSocket.isOutputClosed()) {
            System.out.println("📤 Sending: " + message);
            webSocket.sendText(message, true);
        } else {
            System.err.println("❌ Cannot send - WebSocket not connected");
        }
    }

    /**
     * Wait for messages (blocks until connection closes)
     */
    public void waitForCompletion() throws InterruptedException {
        latch.await(30, TimeUnit.SECONDS);
    }

    /**
     * Close the WebSocket connection
     */
    public void close() {
        if (webSocket != null) {
            webSocket.sendClose(WebSocket.NORMAL_CLOSURE, "Goodbye");
        }
    }

    /**
     * Get all received messages
     */
    public String getReceivedMessages() {
        return receivedMessages.toString();
    }
}