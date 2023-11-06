package de.uulm.in.vs.vns.p2a;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;


public class URLFetcher {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Bitte geben Sie die URL als Argument an.");
            return;
        }

        String urlStr = args[0];

        try {
            URL url = new URL(urlStr);
            String host = url.getHost();
            int port = 80; // Standard-HTTP-Port ist 80
            String path = url.getPath().isEmpty() ? "/" : url.getPath();

            // Verbindung zum Server herstellen
            Socket socket = new Socket(host, port);

            // HTTP-Anfrage erstellen
            String request = "GET " + path + " HTTP/1.1\r\n" +
                    "Host: " + host + "\r\n" +
                    "Connection: close\r\n" +
                    "\r\n";

            // Anfrage senden
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(request.getBytes());
            outputStream.flush();

            // Antwort lesen
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                response.append(line).append("\n");
            }

            // HTTP-Antwort ausgeben
            System.out.println("HTTP Response:");
            System.out.print(response.toString());

            // Verbindung schließen
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}