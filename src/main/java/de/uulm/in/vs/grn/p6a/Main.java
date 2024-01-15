package de.uulm.in.vs.grn.p6a;

import java.io.IOException;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) throws IOException {
        Executors.newFixedThreadPool(1).execute(new Chatclient());
        //Executors.newFixedThreadPool(1).execute(new MessageReceiver(new Socket("vns.lxd-vs.uni-ulm.de",8123)));
    }
}
