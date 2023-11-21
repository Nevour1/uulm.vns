package de.uulm.in.vs.grn.p3a;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;



// args[0] should be a filepath args[1] a filter setting
public class Sockagram {
    public static void main(String[] args) throws IOException {
        if (args.length != 2){
            System.out.println("Bitte Filepath von PNG/JPEG und filter angeben");
            // System.exit(1); // für später fals gui
        }
        String path = args[0];


        //URL url = new URL("vns.lxd-vs.uni-ulm.de");
        Socket socket = new Socket("vns.lxd-vs.uni-ulm.de",7777);
        OutputStream socketOutputStream = socket.getOutputStream();
        InputStream inputStream = new BufferedInputStream(socket.getInputStream());
        ByteArrayOutputStream responseBuffer = new ByteArrayOutputStream();

        File outputFile = new File("test.png");
        FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
        File file = new File(path);
        FileInputStream fileInputStream = (new FileInputStream(file));

        int size = (int) file.length();
        byte filter = Byte.parseByte(args[1]);
        byte[] header = ByteBuffer.allocate(5).put(filter).putInt(size).array();
        byte[] buffer = new byte[2048];
        socketOutputStream.write(header);
        int bytesReadout ;

        while ((bytesReadout = fileInputStream.read(buffer)) != -1) {
            socketOutputStream.write(buffer, 0,bytesReadout);
        }
        fileInputStream.close();

        int readin;
        byte[] data = new byte[5];
        byte[] firstfive = readFirstNBytes(inputStream, 5);

        while ((readin = inputStream.read(data, 0, data.length)) != -1){
            responseBuffer.write(data,0,readin);
        }
        responseBuffer.flush();

        byte[] response = responseBuffer.toByteArray();
        int status = firstfive[0];


        fileOutputStream.write(response);
        fileOutputStream.close();
        socket.close();





    }

    public static byte[] readFirstNBytes(InputStream inputStream, int n) throws IOException {
        byte[] buffer = new byte[n];
        int bytesRead = 0;

        while (bytesRead < n) {
            int count = inputStream.read(buffer, bytesRead, n - bytesRead);

            if (count == -1) {
                // End of stream reached before reading N bytes
                break;
            }

            bytesRead += count;
        }

        return buffer;
    }
}
