package org.example;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        // Start HTTP server on port 8000
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

        // Serve index.html at root "/"
        server.createContext("/", exchange -> {
            byte[] response = Files.readAllBytes(Paths.get("web/index.html"));
            exchange.getResponseHeaders().set("Content-Type", "text/html");
            exchange.sendResponseHeaders(200, response.length);
            OutputStream os = exchange.getResponseBody();
            os.write(response);
            os.close();
        });

        // Serve CSS file
        server.createContext("/style.css", exchange -> {
            byte[] response = Files.readAllBytes(Paths.get("web/style.css"));
            exchange.getResponseHeaders().set("Content-Type", "text/css");
            exchange.sendResponseHeaders(200, response.length);
            OutputStream os = exchange.getResponseBody();
            os.write(response);
            os.close();
        });

        // Handle form submission
        server.createContext("/submit", new FormHandler());

        System.out.println("Server started on http://localhost:8000");
        server.start();
    }

    static class FormHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equals(exchange.getRequestMethod())) {
                // Read form data
                InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), "utf-8");
                BufferedReader br = new BufferedReader(isr);
                String formData = br.readLine();

                // Convert form data into a map
                Map<String, String> answers = new HashMap<>();
                String[] pairs = formData.split("&");
                for (String pair : pairs) {
                    String[] keyValue = pair.split("=");
                    answers.put(keyValue[0], keyValue[1].replace("+", " "));
                }

                // Run compliance check
                String report = ComplianceChecker.checkCompliance(answers);

                // Send report back
                exchange.getResponseHeaders().set("Content-Type", "text/html");
                exchange.sendResponseHeaders(200, report.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(report.getBytes());
                os.close();
            }
        }
    }
}
