package com.noetic.server.domain.model;

import lombok.Data;

@Data
public class GameCharacter {

    private String name;
    private int zoneID;
    private int raceID;
    private int realmID;
    private float x, y;
    private int direction;
    private int level;
    private int xp;
}
