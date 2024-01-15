package de.uulm.in.vs.grn.p6a;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.concurrent.Executors;


public class Chatclient implements Runnable{

    Socket commandSocket;
    Socket pubSubSocket;
    BufferedWriter commandSocketWriter;
    BufferedReader commandSocketReader;
    ClientGUI gui;
    MessageReceiver receiver;
    public boolean loggedIn;

    public Chatclient() throws IOException{
        commandSocket = new Socket("vns.lxd-vs.uni-ulm.de",8122);
        pubSubSocket = new Socket("vns.lxd-vs.uni-ulm.de",8123);
        commandSocketWriter = new BufferedWriter(new PrintWriter(commandSocket.getOutputStream()));
        commandSocketReader = new BufferedReader(new InputStreamReader(commandSocket.getInputStream()));
        gui = new ClientGUI(this);
        receiver = new MessageReceiver(pubSubSocket, gui, this);
        loggedIn = false;
    }
    @Override
    public void run() {
        Thread pings = new Thread(new PingThread(this));
        pings.start();
        while (loggedIn) {
            try {
                pings.sleep(60000);
            } catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    public void login(String enteredName) throws Exception{
        String userName = "";
        boolean userNameValid = false;
        while (!userNameValid) {
            if(enteredName.length() >= 3 && enteredName.length() <= 15 && enteredName.matches("[A-Za-z0-9]+")) {
                userNameValid = true;
                userName = enteredName;
            } else {
                throw new Exception("Username must be alphanumerical and 3-15 characters long");
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
                Executors.newFixedThreadPool(1).execute(receiver);
                readNLines(3, commandSocketReader);
                break;
            case "ERROR":
                System.out.println("Try another user name");
                readNLines(3, commandSocketReader);
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
        String response = commandSocketReader.readLine();
        String responseCode = response.split(" ")[1];
        if(responseCode.equals("BYEBYE")) {
            loggedIn = false;
            commandSocket.close();
            pubSubSocket.close();
        }
    }
    public void sendMessage(String text) throws Exception{
        if(text.getBytes().length <= 512) {
            String message = "SEND VNSCP/1.0\r\n" +
                    "Text: " + text + "\r\n" +
                    "\r\n";
            commandSocketWriter.write(message);
            commandSocketWriter.flush();
        } else {
            throw new Exception("Message too long!");
        }
    }
    public void readNLines(int n, BufferedReader reader) throws IOException{
        while(n > 0) {
            n--;
            reader.readLine();
        }
    }
}
