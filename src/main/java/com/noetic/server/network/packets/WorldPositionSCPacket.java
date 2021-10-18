package com.noetic.server.network.packets;

public class WorldPositionSCPacket extends APacket {
    public float X;
    public float Y;

    @Override
    public String toString() {
        return "sc_world_position";
    }
}
