package com.noetic.server.enums;

public enum AccountLevel {
    Player(0),
    Moderator(1),
    Gamemaster(2),
    Administrator(3);

    private int level;

    AccountLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }
}
