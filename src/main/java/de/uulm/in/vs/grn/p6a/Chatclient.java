package de.uulm.in.vs.grn.p6a;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.Executors;


public class Chatclient implements Runnable{

    Socket commandSocket;
    Socket pubSubSocket;
    BufferedWriter commandSocketWriter;
    BufferedReader commandSocketReader;
    public boolean loggedIn;

    public Chatclient() throws IOException{
        commandSocket = new Socket("vns.lxd-vs.uni-ulm.de",8122);
        pubSubSocket = new Socket("vns.lxd-vs.uni-ulm.de",8123);
        commandSocketWriter = new BufferedWriter(new PrintWriter(commandSocket.getOutputStream()));
        commandSocketReader = new BufferedReader(new InputStreamReader(commandSocket.getInputStream()));
        loggedIn = false;
    }
    @Override
    public void run() {
        System.out.println("Welcome to the VNS Chat Client!");
        while (!loggedIn) {
            try {
                login();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        while(loggedIn) {
            try {
                sendMessages();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            commandSocket.close();
            pubSubSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void login() throws IOException{
        String userName = "";
        BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Please enter a user name");
        String enteredName;
        boolean userNameValid = false;
        while (!userNameValid) {
            enteredName = userInput.readLine();
            if(enteredName.length() >= 3 && enteredName.length() <= 15 && enteredName.matches("[A-Za-z0-9]+")) {
                userNameValid = true;
                userName = enteredName;
            } else {
                System.out.println("User name needs to be 3-15 characters long and alphanumeric!");
            }
        }
        String loginMessage = "LOGIN VNSCP/1.0\r\n" +
                "Username: " + userName + "\r\n" +
                "\r\n";
        commandSocketWriter.write(loginMessage);
        commandSocketWriter.flush();
        String responseType = commandSocketReader.readLine().split(" ")[1];
        switch (responseType) {
            case "LOGGEDIN":
                loggedIn = true;
                System.out.println("You are now logged in. Happy chatting!");
                Executors.newFixedThreadPool(1).execute(new MessageReceiver(pubSubSocket, this));
                break;
            case "ERROR":
                System.out.println("Try another user name");
                break;
        }
    }
    public void sendMessages() throws IOException{
        while(true){
            String message = new BufferedReader(new InputStreamReader(System.in)).readLine();
            if(message.matches("/exit")) {
                logout();
            }
        }
    }
    public void logout() throws IOException {
        String message = "BYE VNSCP/1.0\r\n\r\n";
        commandSocketWriter.write(message);
        commandSocketWriter.flush();
        String responseType = commandSocketReader.readLine().split(" ")[1];
        if(responseType == "BYEBYE") {
            loggedIn = false;

        }
    }
}
