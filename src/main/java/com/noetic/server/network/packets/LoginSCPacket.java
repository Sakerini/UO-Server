package com.noetic.server.network.packets;

public class LoginSCPacket extends APacket{

    public int code;

    @Override
    public String toString() {
        return "sc_login";
    }
}
