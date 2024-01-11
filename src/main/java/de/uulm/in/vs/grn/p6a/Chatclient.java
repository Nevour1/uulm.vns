package de.uulm.in.vs.grn.p6a;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Chatclient {
    public static void main(String[] args) throws IOException {
        Socket commandSocket = new Socket("vns.lxd-vs.uni-ulm.de",8122);
        Socket pubSubSocket = new Socket("vns.lxd-vs.uni-ulm.de",8123);

        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.execute(new MessageReceiver(pubSubSocket));
    }
}
