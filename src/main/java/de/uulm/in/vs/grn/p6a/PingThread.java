package de.uulm.in.vs.grn.p6a;

import java.io.IOException;

public class PingThread implements Runnable {
    Chatclient client;
    public PingThread(Chatclient chatclient) {
        client = chatclient;
    }
    @Override
    public void run() {
        String ping = "PING VNSCP/1.0\r\nText: ping\r\n\r\n";
        while (client.loggedIn) {
            try {
                System.out.println("Pinging Server...");
                client.commandSocketWriter.write(ping);
                client.commandSocketWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
