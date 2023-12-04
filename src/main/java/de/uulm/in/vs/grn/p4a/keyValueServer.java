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
                            }
                            else {
                                writer.write("RES: Unknown Key!\n");
                            }

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


        }




    }


}
