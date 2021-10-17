package com.noetic.server.network.packets;

public class CharacterCreateCSPacket extends APacket {
    public String name;
    public int gender;
    @Override
    public String toString() {
        return "cs_character_create";
    }
}
