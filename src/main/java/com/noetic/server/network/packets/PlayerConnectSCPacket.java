package com.noetic.server.network.packets;

public class PlayerConnectSCPacket extends APacket{
    public String name;
    public int genderId;
    public float x, y;

    @Override
    public String toString() {
        return "sc_player_connect";
    }
}
