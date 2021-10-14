package com.noetic.server.network.packets;

public class LoginCSPacket extends APacket {
    public String username;
    public String password;

    @Override
    public String toString() {
        return "cs_login";
    }
}
