package com.noetic.server.network.connections;

import com.esotericsoftware.kryonet.Connection;
import com.noetic.server.domain.model.Account;
import lombok.Getter;
import lombok.Setter;

import java.util.logging.Level;
import java.util.logging.Logger;

@Getter
@Setter
public class AuthConnection extends Connection {
    private Account account;

    public AuthConnection () {
        Logger.getLogger("server").log(Level.INFO, "Auth client connected.");
    }
}
