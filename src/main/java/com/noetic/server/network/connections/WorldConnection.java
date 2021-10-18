package com.noetic.server.network.connections;

import com.esotericsoftware.kryonet.Connection;
import com.noetic.server.domain.model.Account;
import lombok.Getter;
import lombok.Setter;

import java.util.logging.Level;
import java.util.logging.Logger;

@Setter
@Getter
public class WorldConnection extends Connection {
    private Account account;

    public WorldConnection() {
        Logger.getLogger("server").log(Level.INFO, "World client connected.");
    }
}
