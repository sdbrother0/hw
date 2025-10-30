package org.example.hw06;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicLong;

public class CustomWebServer {

    private static final Map<String, byte[]> staticResources = new ConcurrentHashMap<>();
    private static final byte[] APPLICATION_JSON = "application/json; charset=UTF-8".getBytes(StandardCharsets.UTF_8);

    public static final Map<String, byte[]> HEADER_MAP = Map.of(
        "", "text/html; charset=UTF-8".getBytes(StandardCharsets.UTF_8),
        ".html","text/html; charset=UTF-8".getBytes(StandardCharsets.UTF_8),
        ".css", "text/css".getBytes(StandardCharsets.UTF_8),
        ".js", "application/javascript".getBytes(StandardCharsets.UTF_8),
        ".jpg", "image/jpeg".getBytes(StandardCharsets.UTF_8),
        ".jpeg", "image/jpeg".getBytes(StandardCharsets.UTF_8),
        ".png", "image/png".getBytes(StandardCharsets.UTF_8),
        ".gif", "image/gif".getBytes(StandardCharsets.UTF_8),
        ".ico", "image/x-icon".getBytes(StandardCharsets.UTF_8));

    private final int port;
    private final ExecutorService executorService;
    private volatile boolean running = false;
    private Thread serverMainThread;
    private AtomicLong requestCount = new AtomicLong(0);

    public CustomWebServer(int port, int threadPoolSize, boolean useVirtualThreads) {
        executorService = new CustomExecutorService(threadPoolSize, useVirtualThreads);
        this.port = port;
    }

    public void start() {
        if (running) {
            System.out.println("Server is already running");
            return;
        }
        running = true;
        serverMainThread = new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(this.port)) {
                while (running) {
                    Socket socket = serverSocket.accept();
                    executorService.submit(() -> handleClient(socket));
                }
            } catch (IOException e) {
                if (running) {
                    System.err.println("Server error: " + e.getMessage());
                }
            } finally {
                System.out.println("Server stopped");
            }
        });
        serverMainThread.setDaemon(true);
        serverMainThread.start();
        System.out.println("Server starting on http://localhost:" + port);
    }

    public void stop() {
        running = false;
        executorService.shutdown();
    }

    private void handleClient(Socket clientSocket) {
        requestCount.incrementAndGet();
        try (InputStream inputStream = clientSocket.getInputStream();
             OutputStream outputStream = clientSocket.getOutputStream();
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            int currChar, i = 0;
            int[] last4 = new int[4];
            while ((currChar = inputStream.read()) != -1) {
                byteArrayOutputStream.write(currChar);
                if (byteArrayOutputStream.size() >= 4) {
                    last4[0] = last4[1];
                    last4[1] = last4[2];
                    last4[2] = last4[3];
                    last4[3] = currChar;
                    if (last4[0] == '\r' &&  last4[1] == '\n' && last4[2] == '\r' && last4[3] == '\n') {
                        break;
                    }
                }
            }

            String headers = byteArrayOutputStream.toString(StandardCharsets.UTF_8);

            int contentLength = 0;
            String[] requestHeaders = headers.split("\r\n");
            for (String line : requestHeaders) {
                if (line.toLowerCase().startsWith("content-length:")) {
                    contentLength = Integer.parseInt(line.split(":", 2)[1].trim());
                    break;
                }
            }

            byte[] requestBody = null;
            if (contentLength > 0) {
                requestBody = inputStream.readNBytes(contentLength);
            }
            processRequest(requestHeaders, requestBody, outputStream);
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }

    private void processRequest(String[] requestHeaders, byte[] requestBody, OutputStream response) throws IOException {
        //Method Request-URI HTTP-Version
        String startRequestHeader = requestHeaders[0];
        if (startRequestHeader != null) {
            String[] parts = startRequestHeader.split(" ", 3);

            String method = parts[0];
            String requestUri = parts[1];

            if (method.equals("GET")) {
                if (requestUri.equals("/api/time")) {
                    writeResponse(
                        response,
                        String.format("""
                                {
                                    "currentServerTime": "%s"
                                }
                                """,
                            LocalDateTime.now(ZoneOffset.UTC)).getBytes(StandardCharsets.UTF_8),
                        "application/json".getBytes()
                    );
                    return;
                }
                if (requestUri.equals("/api/stats")) {
                    RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
                    Runtime runtime = Runtime.getRuntime();
                    writeResponse(response, String.format("""
                                {
                                    "requestCount": "%s",
                                    "memoryUsed (MB)": %s,
                                    "pid": %s,
                                    "uptime (sec)": %s,
                                }
                                """,
                            requestCount.get(),
                            ((runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024),
                            runtimeMXBean.getPid(),
                            runtimeMXBean.getUptime() / 1000).getBytes(StandardCharsets.UTF_8),
                        "application/json".getBytes());
                    return;
                }

                byte[] data = getStatic(requestUriToResourceName(requestUri));
                if (data != null) {
                    String extension = getExtension(requestUri);
                    writeResponse(response, data, HEADER_MAP.get(extension));
                    return;
                }
            }

            if (method.equals("POST")) {
                if (requestUri.equals("/api/echo")) {
                    writeResponse(response, requestBody, APPLICATION_JSON);
                    return;
                }
            }

            response.write("HTTP/1.1 404 Not Found\r\n\r\n".getBytes());
            response.flush();
        }
    }

    private void writeResponse(OutputStream response, byte[] data, byte[] contentTypeHeader) throws IOException {
        response.write("HTTP/1.1 200 OK\r\n".getBytes());
        response.write(("Content-Length: " + data.length + "\r\n").getBytes());
        response.write(("Content-Type: ").getBytes());
        response.write(contentTypeHeader);
        response.write("\r\n\r\n".getBytes());
        response.write(data);
        response.flush();
    }

    private byte[] getStatic(String resource) {
        if (resource == null) {
            return null;
        }
        byte[] data = staticResources.get(resource);
        if (data == null) {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resource);
            try {
                if (inputStream != null) {
                    data = inputStream.readAllBytes();
                    staticResources.put(resource, data);
                }
            } catch (IOException e) {
                data = null;
            }
        }
        return data;
    }

    private String requestUriToResourceName(String requestUri) {
        if (requestUri.equals("/")) {
            return "static/index.html";
        }
        if (requestUri.startsWith("/static/")) {
            return requestUri.substring(1);
        }
        return requestUri;
    }

    public static String getExtension(String filename) {
        if (filename == null) {
            return "";
        }
        int lastDot = filename.lastIndexOf('.');
        if (lastDot == -1 || lastDot == filename.length() - 1) {
            return "";
        }
        return filename.substring(lastDot);
    }

}