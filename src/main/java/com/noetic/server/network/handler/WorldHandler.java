package com.noetic.server.network.handler;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.noetic.server.domain.model.Account;
import com.noetic.server.domain.model.GameCharacter;
import com.noetic.server.network.Network;
import com.noetic.server.network.connections.AuthConnection;
import com.noetic.server.network.connections.WorldConnection;
import com.noetic.server.network.packets.*;
import com.noetic.server.utils.ZoneManager;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WorldHandler implements PacketHandler{

    @Override
    public void handlePacket(Server server, Connection connection, APacket packet) {
        WorldConnection worldConnection = (WorldConnection) connection;
        if (packet instanceof WorldConnectionCSPacket) {
            handleWorldConnection(server, worldConnection, ((WorldConnectionCSPacket) packet));
        }
    }

    private void handleWorldConnection(Server server, WorldConnection connection, WorldConnectionCSPacket packet) {
        String accountName = packet.accountName;
        String characterName = packet.characterName;

        Connection[] authConnections = Network.getAuthServer().getConnections();
        for (Connection con: authConnections) {
            AuthConnection temp = ((AuthConnection) con);
            if (temp.getAccount().getUsername().equalsIgnoreCase(accountName)) {
                Logger.getLogger("server").log(Level.INFO, "{0} is requesting a world-com.noetic.mmo.server.logon.",
                        temp.getAccount().getUsername());
                connection.setAccount(temp.getAccount());

                for (GameCharacter player : connection.getAccount().getCharacters()) {
                    if (player.getName().equalsIgnoreCase(characterName)) {
                        connection.getAccount().setOnlineCharacter(player);
                    }
                }
                temp.close();
            }
        }

        connection.sendTCP(new WorldConnectionSCPacket());
        WorldPositionSCPacket positionPacket = new WorldPositionSCPacket();
        positionPacket.X = connection.getAccount().getOnlineCharacter().getX();
        positionPacket.Y = connection.getAccount().getOnlineCharacter().getY();
        connection.sendTCP(positionPacket);
        Logger.getLogger("server").log(Level.INFO, "Sent {0} a spawn packet.",
                connection.getAccount().getOnlineCharacter().getName());

        List<GameCharacter> players = ZoneManager.getPlayersInZone(
                connection.getAccount().getOnlineCharacter().getZoneID());

        for (GameCharacter player : players) {
            PlayerSCPacket playerPacket = new PlayerSCPacket();
            playerPacket.name = player.getName();
            playerPacket.genderId = player.getGenderID();
            playerPacket.x = player.getX();
            playerPacket.y = player.getY();
            connection.sendTCP(playerPacket);
        }

        connection.sendTCP(new PlayerListSCPacket());
        connection.sendTCP(new WorldSCPacket());
        ZoneManager.addPlayerToZone(connection.getAccount().getOnlineCharacter(),
                connection.getAccount().getOnlineCharacter().getZoneID());

        //send new player to all other players
        PlayerConnectSCPacket playerConnectPacket = new PlayerConnectSCPacket();
        GameCharacter connectedPlayer = connection.getAccount().getOnlineCharacter();
        playerConnectPacket.name = connectedPlayer.getName();
        playerConnectPacket.genderId = connectedPlayer.getGenderID();
        playerConnectPacket.x = connectedPlayer.getX();
        playerConnectPacket.y = connectedPlayer.getY();

        Connection[] connections = server.getConnections();

        for (Connection con : connections) {
            WorldConnection worldCon = ((WorldConnection) con);
            if (!worldCon.getAccount().getUsername().equalsIgnoreCase(connection.getAccount().getUsername())) {
                if (worldCon.getAccount().getOnlineCharacter().getZoneID() == connection.getAccount().getOnlineCharacter().getZoneID()) {
                    worldCon.sendTCP(connectedPlayer);
                }
            }
        }
    }
}
