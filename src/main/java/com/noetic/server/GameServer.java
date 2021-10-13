package com.noetic.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.noetic.server.domain.connections.WorldConnection;
import com.noetic.server.enums.LogType;
import com.noetic.server.utils.Configuration;

import java.io.Console;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameServer {

    public static final String SALT = "chupakabra";


    private Server world;
    private static ConsoleGUI consoleGUI;

    public GameServer () throws IOException {
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "%4$s: %5$s [%1$tc]%n");

        consoleGUI = new ConsoleGUI(this);

        world = new Server() {
            protected Connection newConnection() {
                return new WorldConnection();
            }
        };
        world.start();
        world.bind(Integer.parseInt(Configuration.worldServerPort), Integer.parseInt(Configuration.worldServerPort) + 1);

        world.addListener(new Listener() {
            public void received(Connection connection, Object object) {
                //TODO
            }

            // TODO: Save player data, send to all other players, etc,.
            public void disconnected(Connection connection) {

            }
        });

        if (world != null) {
            String msg = "World Server started on ports:"
                    + Configuration.worldServerPort +", " + Configuration.worldServerPort + 1;
            Logger.getLogger("server").log(Level.INFO, msg);
            consoleGUI.writeMessage(LogType.Server, msg);
        } else
            stop();
    }

    public static ConsoleGUI getServerConsole() {
        return consoleGUI;
    }

    /**
     * Stop the server and exit cleanly.
     */
    public synchronized void stop() {
        System.exit(0);
    }
}
