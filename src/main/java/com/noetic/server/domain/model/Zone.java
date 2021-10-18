package com.noetic.server.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

@Getter
@Setter
public class Zone {

    private int id;
    private String name;

    private ArrayList<GameCharacter> players = new ArrayList<>();

    public void addPlayer(GameCharacter player) {
        Logger.getLogger("server").log(Level.INFO, "Added player {0} to zone {1}:{2}.",
                new Object[] { player.getName(), name, id});
        players.add(player);
    }

    public void removePlayer(GameCharacter player) {
        Logger.getLogger("server").log(Level.INFO, "Removed player {0} from zone {1}:{2}.",
                new Object[] { player.getName(), name, id});
        players.remove(player);
    }

    public ArrayList<GameCharacter> getPlayers() {
        return players;
    }
}
