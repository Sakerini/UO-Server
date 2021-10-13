package com.noetic.server.domain.model;

import lombok.Data;

@Data
public class GameCharacter {

    private String Name;
    private int ZoneID;
    private int RaceID;
    private int RealmID;
    private float X, Y;
    private int Direction;
    private int Level;
    private int XP;
}
