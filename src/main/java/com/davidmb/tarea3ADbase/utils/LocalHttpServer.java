package com.davidmb.tarea3ADbase.utils;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class LocalHttpServer {

    public static void startServer(String pdfPath) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/pdf", new PdfHandler(pdfPath));
        server.setExecutor(null); // Usar el executor por defecto
        server.start();
        System.out.println("Servidor HTTP iniciado en http://localhost:8080/pdf");
    }

    static class PdfHandler implements HttpHandler {
        private final String pdfPath;

        public PdfHandler(String pdfPath) {
            this.pdfPath = pdfPath;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            File file = new File(pdfPath);
            if (!file.exists()) {
                byte[] response = "Archivo no encontrado".getBytes();
                exchange.sendResponseHeaders(404, response.length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response.length);
                }
                return;
            }

            // Leer el archivo PDF y enviarlo como respuesta
            byte[] pdfBytes = Files.readAllBytes(Paths.get(pdfPath));
            exchange.sendResponseHeaders(200, pdfBytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(pdfBytes);
            }
        }
    }
}