package com.noetic.server.network.packets;

public class CharacterCreateSCPacket extends APacket {
    public int code;
    @Override
    public String toString() {
        return "sc_character_create";
    }
}
