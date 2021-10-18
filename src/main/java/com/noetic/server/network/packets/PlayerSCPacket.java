package com.noetic.server.network.packets;

public class PlayerSCPacket extends APacket {

    public String name;
    public int genderId;
    public float x, y;

    @Override
    public String toString() {
        return "sc_player";
    }
}
