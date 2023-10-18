package de.uulm.in.vs.vns.p1a;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class NumberGuessingGameThreadedServer {
    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(5555);
        final int MAX_PLAYERS = 3;
        int currentPlayers = 0;
        while (true) {
            if(currentPlayers < MAX_PLAYERS) {
                System.out.println("Waiting for connection");
                Socket connectionSocket = serverSocket.accept();
                System.out.println("Connection accepted");
                NumberGuessingGameRequestHandler handler = new NumberGuessingGameRequestHandler(connectionSocket);
                new Thread(handler).start();
                currentPlayers++;
            }
        }
    }
}
