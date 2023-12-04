package de.uulm.in.vs.grn.p4a;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

//import java.net.
public class keyValueServer {


    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(3211);

        Map<String, String> map = new HashMap<>();


        while(true){

            System.out.println("Waiting for connection");
            Socket connectionsSocket = serverSocket.accept();
            System.out.println("Connection accepted");
            BufferedReader reader = new BufferedReader(new InputStreamReader(connectionsSocket.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connectionsSocket.getOutputStream()));
            String input;
            try {
                input = reader.readLine();
            } catch (IOException e){
                throw new RuntimeException(e);
            }
            outer:
            while(input != null){

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
                            }
                            else {
                                writer.write("ERR: Unknown Key!\n");
                                break;
                            }
                        case "PUT":
                            if (inputs_a.length != 3){
                                writer.write("ERR: Unknown Command!\n");
                            }
                            else {
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
                } catch (IOException e){
                    throw new RuntimeException();
                }

            }

            connectionsSocket.close();


        }




    }


}
