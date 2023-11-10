package de.uulm.in.vs.vns.p2a;

import java.io.*;
import java.net.Socket;
import java.net.URL;

public class URLFetcher {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Bitte geben Sie die URL als Argument an.");
            return;
        }
        // pdf test url http://www.pdf995.com/samples/pdf.pdf

        String urlStr = args[0];

        try {
            URL url = new URL(urlStr);
            String host = url.getHost();
            int port = url.getPort() != -1 ? url.getPort() : 80;
            String path = url.getPath().isEmpty() ? "/" : url.getPath();

            Socket socket = new Socket(host, port);

            String request = "GET " + path + " HTTP/1.1\r\n" +
                    "Host: " + host + "\r\n" +
                    "Connection: close\r\n" +
                    // "Transfer-Encoding: identity\r\n" +
                    "\r\n";

            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(request.getBytes());
            outputStream.flush();

            InputStream inputStream = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            
            // Statuscode aus der ersten Zeile der Antwort lesen
            String firstLine = reader.readLine();
            int statusCode = Integer.parseInt(firstLine.split(" ")[1]);

            // Content-Type-Header aus der Antwort lesen
            String contentType = null;
            String line;
            while ((line = reader.readLine()) != null && !line.isEmpty()) {
                if (line.startsWith("Content-Type:")) {
                    contentType = line.substring("Content-Type:".length()).trim();
                    break;
                }
            }

            if (statusCode == 200) {
                // Überprüfen und URL basierend Dateinamen Festlegen
                String[] pathSegments = path.split("/");
                String lastPathSegment = pathSegments[pathSegments.length -1];
                // Datei speichern
                File outputFile = new File(lastPathSegment);
                FileOutputStream fileOutputStream = new FileOutputStream(outputFile);

                // Body-Inhalt lesen und in die Datei schreiben
                if (contentType != null && (contentType.startsWith("text/") || contentType.contains("html"))) {
                    // Textinhalt als Zeichenstrom lesen
                    BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
                    String lineRead;
                    while ((lineRead = reader.readLine()) != null) {
                        writer.write(lineRead);
                        writer.newLine();
                    }
                    writer.close();
                } else {
                    // Binärinhalt als Bytestrom lesen
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        fileOutputStream.write(buffer, 0, bytesRead);
                    }
                }

                fileOutputStream.close();
                System.out.println("Datei heruntergeladen und gespeichert: " + outputFile.getName());
            } else {
                System.out.println("HTTP Request fehlgeschlagen. Response Code: " + statusCode);
            }

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
