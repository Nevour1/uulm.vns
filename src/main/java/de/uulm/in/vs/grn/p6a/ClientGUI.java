package de.uulm.in.vs.grn.p6a;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class ClientGUI extends JFrame {

    Chatclient client;
    JLabel chat;
    public ClientGUI(Chatclient client) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        panel.setAlignmentX(SwingConstants.LEFT);
        this.client = client;
        setSize(500,500);
        setTitle("VNSCP");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JButton loginButton = new JButton("Login");
        loginButton.setHorizontalAlignment(SwingConstants.LEFT);
        JButton quitButton = new JButton("Quit chat");
        quitButton.setHorizontalAlignment(SwingConstants.LEFT);
        JTextField usernameInput = new JTextField();
        JTextField messageInput = new JTextField();
        JButton sendButton = new JButton("Send");
        sendButton.setHorizontalAlignment(SwingConstants.LEFT);
        chat = new JLabel("");
        chat.setPreferredSize(new Dimension(500,300));

        quitButton.addActionListener(e -> {
            try {
                client.logout();
                dispatchEvent(new WindowEvent(this,WindowEvent.WINDOW_CLOSING));
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        });

        loginButton.addActionListener(e -> {
            String username = usernameInput.getText();
            try {
                client.login(username);
                usernameInput.setText("");
            } catch (Exception exception) {
                usernameInput.setText(exception.getMessage());
            }
        });

        sendButton.addActionListener(e -> {
            try {
                String message = messageInput.getText();
                client.sendMessage(message);
                messageInput.setText("");
            } catch (Exception exception) {
                messageInput.setText(exception.getMessage());
            }
        });
        panel.add(usernameInput);
        panel.add(loginButton);
        panel.add(chat);
        panel.add(messageInput);
        panel.add(sendButton);
        panel.add(quitButton);
        add(panel);
        setVisible(true);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    client.logout();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });
    }
}
