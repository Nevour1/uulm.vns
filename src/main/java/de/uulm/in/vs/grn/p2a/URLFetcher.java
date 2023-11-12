package de.uulm.in.vs.grn.p2a;

import java.io.*;
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
            int port =  80;
            String path = url.getPath().isEmpty() ? "/" : url.getPath();

            Socket socket = new Socket(host, port);

            String request = "GET " + path + " HTTP/1.1\r\n" +
                    "Host: " + host + "\r\n" +
                    "Connection: close\r\n" +
                    // "Transfer-Encoding: identity\r\n" +
                    "\r\n";
            /*a1: verpflichtende Header-Felder:
                    -Methodenaufruf
                    -Pfad/ HTTP version
                    -host
                    hier noch zusätzlich Connection Close
            */
            OutputStream outputStream = socket.getOutputStream();
            InputStream inputStream = new BufferedInputStream(socket.getInputStream());
            outputStream.write(request.getBytes());
            outputStream.flush();

            
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            
            // Statuscode aus der ersten Zeile der Antwort lesen
            inputStream.mark(30000);
            String firstLine = reader.readLine();
            int statusCode = Integer.parseInt(firstLine.split(" ")[1]);
            String[] pathSegments = path.split("/");
            String lastPathSegment = pathSegments[pathSegments.length -1];
            String line;
            int linesread = firstLine.length()+2;
            
            do {
                line = reader.readLine();
                linesread += line.length()+2;
            } while (!line.isEmpty());
            System.out.println(linesread);
            inputStream.reset();
            inputStream.skip(linesread);
                       
            
            if (statusCode == 200) {
                // Überprüfen und URL basierend Dateinamen Festlegen
                
                // Datei speichern
                File outputFile = new File(lastPathSegment);
                FileOutputStream fileOutputStream = new FileOutputStream(outputFile);

                    // Binärinhalt als Bytestrom lesen
                    byte[] buffer = new byte[2^17];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        fileOutputStream.write(buffer, 0, bytesRead);
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
