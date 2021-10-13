package com.noetic.server.domain.model;

import com.noetic.server.enums.AccountLevel;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Account {
    private String Username;
    private String HashedPassword;
    private String Salt;
    private AccountLevel Security;
    private List<GameCharacter> Characters = new ArrayList<>();
    private GameCharacter OnlineCharacter;
    private int RealmID;

    public void RemoveCharacter(String name) {
        Characters.removeIf(n -> n.getName().equalsIgnoreCase(name));
    }
}
