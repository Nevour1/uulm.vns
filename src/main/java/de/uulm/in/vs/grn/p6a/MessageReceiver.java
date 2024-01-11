package de.uulm.in.vs.grn.p6a;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class MessageReceiver implements Runnable {
    BufferedReader in;

    public MessageReceiver(Socket socket) throws IOException {
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }
    @Override
    public void run(){
        while(true){
            try{
                System.out.println(in.readLine());
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
