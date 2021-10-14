package com.noetic.server.network.handler;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.noetic.server.network.packets.APacket;

public interface PacketHandler {
    void handlePacket(Server server, Connection connection, APacket packet);

}
