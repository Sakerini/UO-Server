package com.noetic.server;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            new GameServer();
        } catch (IOException e) {
            System.out.println("Unable to start the gameserver: ");
            e.printStackTrace();
        }
    }
}
