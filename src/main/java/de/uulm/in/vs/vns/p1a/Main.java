package de.uulm.in.vs.vns.p1a;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ThreadLocalRandom;

public class Main {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(5555);

        while (true) {
            System.out.println("waiting for connection");
            Socket connectionSocket = serverSocket.accept();
            System.out.println("connection accepted");

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connectionSocket.getInputStream())
            );

            PrintWriter writer = new PrintWriter(
                    new OutputStreamWriter(connectionSocket.getOutputStream())
            );

            writer.println("hey client, guess my secret number!");
            writer.flush();

            int secret = ThreadLocalRandom.current().nextInt(50) + 1;
            int tries = 6;

            String input;
            while ((input = reader.readLine()) != null) {
                try {
                    int guess = Integer.parseInt(input);
                    if (guess == secret) {
                        writer.println("you've won");
                        break;
                    } else if (guess > secret) {
                        writer.println("your guess is too high");
                    } else {
                        writer.println("your guess is too low");
                    }
                    tries -= 1;
                    if (tries == 0) {
                        writer.println("You loose!");
                        break;
                    }
                } catch (NumberFormatException e) {
                    writer.println("Please enter a valid number!");
                } finally {
                    writer.flush();
                }
            }

            connectionSocket.close();
        }
    }
}