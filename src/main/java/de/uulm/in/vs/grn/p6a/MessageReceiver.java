package de.uulm.in.vs.grn.p6a;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class MessageReceiver implements Runnable {
    BufferedReader in;
    Chatclient chatClient;

    public MessageReceiver(Socket socket, Chatclient chatClient) throws IOException {
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.chatClient = chatClient;
    }
    @Override
    public void run(){
        while(chatClient.loggedIn){
            try{
                System.out.println(in.readLine());
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
