package de.uulm.in.vs.grn.p4a;

import java.io.*;
import java.net.Socket;
import java.util.Map;

public class KeyValueHandler implements Runnable {


    private Map<String,String> map;
    Socket connectionSocket;

    BufferedReader reader;
    BufferedWriter writer;
    public KeyValueHandler(Socket connectionSocket,Map<String,String> sharedMap) throws IOException{
        this.connectionSocket = connectionSocket;
        reader = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
        writer = new BufferedWriter(new OutputStreamWriter(connectionSocket.getOutputStream()));
        this.map = sharedMap;
    }

    @Override
    public void run() {
        try {
            String input;
            try {
                input = reader.readLine();
            } catch (
                    IOException e) {
                throw new RuntimeException(e);
            }
            outer:
            while (input != null) {

                try {
                    String[] inputs_a = input.split(" ");

                    String Command = inputs_a[0];
                    switch (Command) {
                        case "GET":
                            if (inputs_a.length != 2) {
                                writer.write("ERR: Unknown Command!\n");
                                break;
                            }
                            if (map.containsKey(inputs_a[1])) {
                                writer.write("RES: " + map.get(inputs_a[1]));
                                writer.newLine();
                                break;
                            } else {
                                writer.write("ERR: Unknown Key!\n");
                                break;
                            }
                        case "PUT":
                            if (inputs_a.length != 3) {
                                writer.write("ERR: Unknown Command!\n");
                            } else {
                                writer.write("RES: OK\n");
                                map.put(inputs_a[1], inputs_a[2]);

                            }
                            break;
                        case "EXIT":
                            writer.write("RES: BYE!\n");
                            break outer;
                        default:
                            writer.write("ERR: Unknown Command!\n");
                    }


                } finally {
                    writer.flush();

                }
                try {
                    input = reader.readLine();
                } catch (IOException e) {
                    throw new RuntimeException();
                }

            }
        } catch (IOException e){
            throw new RuntimeException(e);
        }

        try {
            connectionSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
