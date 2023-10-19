package de.uulm.in.vs.vns.p1a;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NumberGuessingGameThreadedServer {
    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(5555);
        final int MAX_PLAYERS = 4;
        ExecutorService threadPool = Executors.newFixedThreadPool(MAX_PLAYERS);
        while (true) {
            /*System.out.println("Waiting for connection");
            Socket connectionSocket = serverSocket.accept();
            System.out.println("Connection accepted");
            NumberGuessingGameRequestHandler handler = new NumberGuessingGameRequestHandler(connectionSocket);
            new Thread(handler).start();*/
            System.out.println("Waiting for connection");
            Socket connectionSocket = serverSocket.accept();
            System.out.println("Connection accepted");
            threadPool.execute(new NumberGuessingGameRequestHandler(connectionSocket));
        }
    }
}
