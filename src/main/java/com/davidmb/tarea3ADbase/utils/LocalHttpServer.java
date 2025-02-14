package com.davidmb.tarea3ADbase.utils;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class LocalHttpServer {

    public static void startServer(String pdfPath) throws IOException {
     
        HttpServer server = HttpServer.create(new InetSocketAddress(50000), 0);
        
        // Definir un manejador para las solicitudes entrantes
        server.createContext("/pdf", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
            	
                File pdfFile = new File(pdfPath);
                if (!pdfFile.exists()) {
                    exchange.sendResponseHeaders(404, 0);
                    return;
                }

                // Establecer los encabezados de respuesta para PDF
                exchange.getResponseHeaders().set("Content-Type", "application/pdf");
                exchange.sendResponseHeaders(200, pdfFile.length());

                // Leer el archivo PDF y enviarlo como respuesta
                try (FileInputStream fis = new FileInputStream(pdfFile);
                     OutputStream os = exchange.getResponseBody()) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = fis.read(buffer)) != -1) {
                        os.write(buffer, 0, bytesRead);
                    }
                }
            }
        });

        // Iniciar el servidor
        server.start();
    }
}
