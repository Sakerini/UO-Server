package com.noetic.server.utils;

import com.noetic.server.GameServer;
import com.noetic.server.domain.model.GameCharacter;
import com.noetic.server.domain.model.Zone;

import java.util.ArrayList;

public class ZoneManager {

    private static ZoneParser parser;
    private static ArrayList<Zone> zones;

    public static void initialize(GameServer server) {
        if (parser == null) {
            zones = new ArrayList<>();
            parser = new ZoneParser(server);
            parser.start();
        }
    }

    /**
     * Adds a zone to this manager.
     * @param zone
     */
    public static void addZone(Zone zone) {
        zones.add(zone);
    }

    /**
     * Adds a player to a zone.
     * @param player
     * @param zoneId
     */
    public static void addPlayerToZone(GameCharacter player, int zoneId) {
        for (Zone zone : zones) {
            if (zone.getId() == zoneId) {
                zone.addPlayer(player);
            }
        }
    }

    /**
     * Remove a player from their zone.
     * @param player
     */
    public static void removePlayerFromZone(GameCharacter player) {
        for (Zone zone : zones) {
            if (zone.getId() == player.getZoneID()) {
                zone.removePlayer(player);
            }
        }
    }

    public static ArrayList<GameCharacter> getPlayersInZone(int zoneId) {
        for (Zone zone : zones) {
            if (zone.getId() == zoneId) {
                return zone.getPlayers();
            }
        }
        return null;
    }

    /**
     * @return the current state of the parser.
     */
    public static ZoneParser.State getParseState() {
        return parser.getState();
    }
}

