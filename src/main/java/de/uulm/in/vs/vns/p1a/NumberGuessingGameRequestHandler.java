package de.uulm.in.vs.vns.p1a;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ThreadLocalRandom;

public class NumberGuessingGameRequestHandler implements Runnable {

    Socket connectionSocket;
    BufferedReader reader;
    PrintWriter writer;

    public NumberGuessingGameRequestHandler(Socket connectionSocket) throws IOException {
        this.connectionSocket = connectionSocket;
        reader = new BufferedReader(
                new InputStreamReader(connectionSocket.getInputStream())
        );
        writer = new PrintWriter(
                new OutputStreamWriter(connectionSocket.getOutputStream())
        );
        //initialize reader and writer for communication with client through connectionSocket
    }

    @Override
    public void run() {
        writer.println("Hello Player, guess my secret Number between 1 and 50! You have 6 tries");
        writer.flush();

        int secret = ThreadLocalRandom.current().nextInt(50)+1;
        int tries = 6;
        String input;
        try {
            input = reader.readLine(); //read the first guess
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        while(input != null) {
            try {
                int guess = Integer.parseInt(input);
                tries--;
                if (guess == secret) {
                    writer.println("You guessed correctly, congratulations!");
                    break;
                } else if (guess < secret) {
                    writer.println("Your guess is too low. You have " + tries + " remaining tries");
                } else {
                    writer.println("Your guess is too high. You have " + tries + " remaining tries");
                }
                if (tries <= 0) {
                    writer.println("You have used all your tries! You lose");
                    break;
                }
            } catch (NumberFormatException e){
                writer.println("Please enter a valid number");
            } finally {
                writer.flush();
            }
            try {
                input = reader.readLine(); //read next guess
            } catch (IOException e) {
                throw new RuntimeException();
            }
        }
        try {
            connectionSocket.close(); //close connection once the game is finished
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}