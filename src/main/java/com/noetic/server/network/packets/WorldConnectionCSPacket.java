package com.noetic.server.network.packets;

public class WorldConnectionCSPacket extends APacket{

    public String accountName;
    public String characterName;

    @Override
    public String toString() {
        return "cs_world_connection";
    }
}
