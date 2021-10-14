package com.noetic.server.domain.model;

import com.noetic.server.enums.AccountLevel;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Account {
    private String username;
    private String hashedPassword;
    private String salt;
    private AccountLevel security;
    private List<GameCharacter> characters = new ArrayList<>();
    private GameCharacter onlineCharacter;
    private int realmID;

    public void RemoveCharacter(String name) {
        characters.removeIf(n -> n.getName().equalsIgnoreCase(name));
    }
}
