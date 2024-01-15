package de.uulm.in.vs.grn.p6a;


import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class MessageReceiver implements Runnable {
    BufferedReader in;
    ClientGUI gui;
    Chatclient client;
    public MessageReceiver(Socket socket, ClientGUI gui, Chatclient client) throws IOException {
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.gui = gui;
        this.client = client;
    }
    @Override
    public void run(){
        while(client.loggedIn){
            //gui.chat.add(new JLabel(in.readLine()));
            //gui.chat.revalidate();
            String message = readMessage();
            gui.chat.setText("");
            gui.chat.setText(message);
            gui.chat.revalidate();
        }
    }

    public String readMessage() {
        try {
            String type = in.readLine().split(" ")[1];
            switch (type) {
                case "MESSAGE":
                    in.readLine();
                    in.readLine();
                    String userName = in.readLine().split(" ")[1];
                    String message = in.readLine().split(":")[1];
                    in.readLine();
                    return userName + ": " + message;
                case "EVENT":
                    in.readLine();
                    in.readLine();
                    String eventMessage = in.readLine();
                    in.readLine();
                    return eventMessage;
                default:
                    return "";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
