package de.uulm.in.vs.grn.p5a;

import java.awt.image.BufferedImage;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.Color;
import javax.swing.Timer;
import javax.swing.JFrame;
import javax.swing.JColorChooser;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

public class Pixelflut {

    private final JFrame frame;
    private final BufferedImage image;
    private Color color;
    private DatagramSocket socket;
    private InetAddress address;
    public Pixelflut(String host, int port) throws IOException {
        image = new BufferedImage(128, 128, BufferedImage.TYPE_INT_RGB);
        color = new Color((int)(Math.random() * 255), (int)(Math.random() * 255), (int)(Math.random() * 255));

        // todo set up udp socket

        socket = new DatagramSocket(9999);

        address = InetAddress.getByName(host);

        // todo receive update packets


        new Thread(this::recieveUpdates).start();

        // pixels can be set using the following method, the Byte.toUnsignedInt()
        // function is used to prevent java to interpret the most significant bit
        // of a byte as the sign of the resulting integer
        // image.setRGB(x, y, Byte.toUnsignedInt(r), Byte.toUnsignedInt(g), Byte.toUnsignedInt(b));

        // simple pixelflut gui, you do not have to change this for the task,
        // but feel free to make improvements if you want to :)
        frame = new JFrame("VNS Pixelflut");
        frame.setSize(1024, 1024);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                byte x = (byte) Math.round(e.getX()/8);
                byte y = (byte) Math.round(e.getY()/8);
                switch (e.getButton()) {
                    case MouseEvent.BUTTON1:
                        sendUpdate(x, y, color);
                        break;

                    case MouseEvent.BUTTON2:
                        // middle click: copy color of selected pixel
                        color = new Color(image.getRGB(x, y));
                        break;

                    case MouseEvent.BUTTON3:
                        // right click: select new color with picker
                        Color c = JColorChooser.showDialog(frame, "Choose color", color);
                        if (c != null) {
                            color = c;
                        }
                        break;
                }
            }
        });

        // redraw canvas with a fixed frame rate of ~30fps
        Timer timer = new Timer(33, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.getGraphics().drawImage(image, 0, 0, 1024, 1024, frame);
            }
        });
        timer.start();

        // send a first update to say hello, otherwise we won't receive updates by the server
        sendUpdate((byte) 0, (byte) 0, color);
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("Usage: java Pixelflut HOST PORT");
            System.exit(1);
        }

        new Pixelflut(args[0], Integer.parseInt(args[1]));
    }

    public void sendUpdate(byte x, byte y, Color color) {
        // todo send update datagram to server

        try{
        byte[] data = {x,y,(byte) color.getRed(), (byte) color.getGreen(), (byte)color.getBlue()};
        DatagramPacket update = new DatagramPacket(data, data.length,address,9999);
        socket.send(update);
        }catch (IOException e){
            e.printStackTrace();
        }

        // todo remove direct pixel update
        //image.setRGB(x, y, color.getRGB());
    }

    private void recieveUpdates(){
        try{
            byte[] rbuff = new byte[1024];
            while (true){
                DatagramPacket recievePacket = new DatagramPacket(rbuff, rbuff.length);
                socket.receive((recievePacket));

                if (recievePacket.getAddress().equals(address)) {
                    byte x = recievePacket.getData()[0];
                    byte y = recievePacket.getData()[1];
                    int red = recievePacket.getData()[2] & 0xFF;
                    int green = recievePacket.getData()[3] & 0xFF;
                    int blue = recievePacket.getData()[4] & 0xFF;

                    Color rPixel = new Color(red,green,blue);
                    if (Byte.toUnsignedInt(x) <= image.getWidth() && Byte.toUnsignedInt(y) <= image.getHeight()) {
                        image.setRGB(x, y, rPixel.getRGB());
                    }
                    else {
                        System.out.println("discarded invalid coords " + Arrays.toString(recievePacket.getData()));
                    }

                }
                else {
                    System.out.println("Unexpected Host" + recievePacket.getAddress());
                }
            }
        } catch (IOException e){
            e.printStackTrace();

        }

    }
}
