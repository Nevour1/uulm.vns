package de.uulm.in.vs.grn.p4a;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//import java.net.
public class keyValueServer {


    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(3211);

        Map<String, String> map = new HashMap<>();
        final int MAX_CONNECTIONS = 4;
        ExecutorService threadPool = Executors.newFixedThreadPool(MAX_CONNECTIONS);

        while(true){

            System.out.println("Waiting for connection");
            Socket connectionSocket = serverSocket.accept();
            System.out.println("Connection accepted");
            threadPool.execute(new KeyValueHandler(connectionSocket, map));
            //BufferedReader reader = new BufferedReader(new InputStreamReader(connectionsSocket.getInputStream()));
            //BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connectionsSocket.getOutputStream()));



        }




    }


}
