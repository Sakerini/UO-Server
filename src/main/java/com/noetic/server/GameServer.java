package com.noetic.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.noetic.server.network.connections.AuthConnection;
import com.noetic.server.network.connections.WorldConnection;
import com.noetic.server.enums.LogType;
import com.noetic.server.network.Network;
import com.noetic.server.network.handler.LoginHandler;
import com.noetic.server.network.handler.PacketHandler;
import com.noetic.server.network.packets.APacket;
import com.noetic.server.utils.Configuration;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameServer {

    public static final String SALT = "chupakabra";

    private Server world;
    private Server authServer;
    private static ConsoleGUI consoleGUI;

    private final Map<String, PacketHandler> packetHandlers = new HashMap<>();

    public GameServer () throws IOException {
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "%4$s: %5$s [%1$tc]%n");

        consoleGUI = new ConsoleGUI(this);

        packetHandlers.put("cs_login", new LoginHandler());

        authServer = new Server() {
            protected Connection newConnection() {
                return new AuthConnection();
            }
        };


        authServer.start();
        authServer.bind(Integer.parseInt(Configuration.authServerPort));
        Network.registerLib(authServer.getKryo());
        authServer.addListener(new Listener() {
            public void received(Connection connection, Object object) {
                for (Map.Entry<String, PacketHandler> set : packetHandlers.entrySet()) {
                    if (set.getKey().equalsIgnoreCase(object.toString())) {
                        set.getValue().handlePacket(authServer, connection, (APacket) object);
                    }
                }
            }

            public void disconnected(Connection connection) {
                AuthConnection temp = (AuthConnection) connection;
                if (Objects.nonNull(temp.getAccount())) {
                    Logger.getLogger("server").log(Level.INFO, "{0} got disconnected from the authentication server.",
                            temp.getAccount().getUsername());
                } else {
                    Logger.getLogger("server").log(Level.INFO, "Client {0} got disconnected from the authentication server.",
                            connection.getID());
                }
            }
        });

        world = new Server() {
            protected Connection newConnection() {
                return new WorldConnection();
            }
        };
        world.start();
        world.bind(Integer.parseInt(Configuration.worldServerPort), Integer.parseInt(Configuration.worldServerPort) + 1);

        Network.registerLib(world.getKryo());
        world.addListener(new Listener() {
            public void received(Connection connection, Object object) {
                for (Map.Entry<String, PacketHandler> set : packetHandlers.entrySet()) {
                    if (set.getKey().equalsIgnoreCase(object.toString())) {
                        set.getValue().handlePacket(world, connection, (APacket)object);
                    }
                }
            }

            // TODO: Save player data, send to all other players, etc,.
            public void disconnected(Connection connection) {

            }
        });

        if (Objects.nonNull(world) && Objects.nonNull(authServer)) {
            String msg = String.format("World Server started on ports: %s, %s",
                    Configuration.worldServerPort, Integer.parseInt(Configuration.worldServerPort) + 1);
            Logger.getLogger("server").log(Level.INFO, msg);
            consoleGUI.writeMessage(LogType.Server, msg);
            msg = String.format("AuthServer started on port %s.", Configuration.authServerPort);
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
