package com.noetic.server;

import com.noetic.server.utils.Configuration;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            Configuration.load();
            new GameServer();
        } catch (IOException e) {
            System.out.println("Unable to start the game server: ");
            e.printStackTrace();
        }
    }
}
