package com.noetic.server;

import com.noetic.server.enums.LogType;
import com.noetic.server.handlers.command.AccountCommandHandler;
import com.noetic.server.handlers.command.CommandHandler;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class ConsoleGUI {

    private GameServer gameServer;
    private final List<CommandHandler> commands = new ArrayList<>();

    private final JFrame frame = new JFrame();
    private JTextField textField;
    private JTextArea textArea;

    public void initialize(GameServer server) {
        this.gameServer = server;
        frame.setResizable(false);
        frame.setTitle("University Online - Game Server");
        frame.setBounds(100, 100, 476, 288);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setLayout(null);

        textField = new JTextField();
        textField.setBounds(10, 232, 453, 20);
        frame.getContentPane().add(textField);
        textField.setColumns(10);
        textField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    parseCommand(textField.getText());
                    textField.setText("");
                }
            }
        });

        textArea = new JTextArea();
        textArea.setEditable(false);

        JScrollPane scroller = new JScrollPane();
        scroller.setViewportView(textArea);
        scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroller.setBounds(10, 10, 453, 215);

        frame.getContentPane().add(scroller);
        frame.setVisible(true);
        commands.add(new AccountCommandHandler());
    }

    private void parseCommand(String cmd) {
        if (cmd.equalsIgnoreCase("stop"))
            gameServer.stop();

        for (CommandHandler command : commands) {
            if (cmd.startsWith(command.getPrefix())) {
                command.handleCommand(cmd.split(" "));
            }
        }
    }

    public void writeMessage(LogType logType, String message) {
        String type = String.format("[%s]", logType.name());
        textArea.append(type + " " + message + "\n");
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }
}
