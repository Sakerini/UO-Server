package com.noetic.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.noetic.server.domain.connections.WorldConnection;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameServer {

    private Server world;
    private ConsoleGUI consoleGUI;

    public GameServer () throws IOException {
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "%4$s: %5$s [%1$tc]%n");

        world = new Server() {
            protected Connection newConnection() {
                return new WorldConnection();
            }
        };
        world.start();

        consoleGUI = new ConsoleGUI(this);
    }

    /**
     * Stop the server and exit cleanly.
     */
    public synchronized void stop() {
        System.exit(0);
    }
}
