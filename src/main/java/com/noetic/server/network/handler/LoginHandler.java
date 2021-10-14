package com.noetic.server.network.handler;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.noetic.server.GameServer;
import com.noetic.server.domain.model.Account;
import com.noetic.server.network.packets.APacket;
import com.noetic.server.network.connections.AuthConnection;
import com.noetic.server.network.packets.LoginCSPacket;
import com.noetic.server.network.packets.LoginSCPacket;
import com.noetic.server.service.AccountService;
import com.noetic.server.utils.BCrypt;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginHandler implements PacketHandler{

    private final static int UNKNOWN = 10;
    private final static int ONLINE = 2;
    private final static int INCORRECT = 1;
    private final static int OK = 0;

    private AccountService accountService;

    @Override
    public void handlePacket(Server server, Connection connection, APacket packet) {
        AuthConnection authConnection = (AuthConnection) connection;
        LoginCSPacket loginPacket = (LoginCSPacket) packet;

        String username = loginPacket.username;
        String password = loginPacket.password;

        Logger.getLogger("server").log(Level.INFO, "{0} is logging in.", username);

        LoginSCPacket response = new LoginSCPacket();
        Account account = accountService.getAccount(username);
        if (Objects.isNull(account)) {
            response.code = UNKNOWN;
            authConnection.sendTCP(response);
            authConnection.close();
            return;
        } else if (accountService.isOnline(username)) {
            response.code = ONLINE;
            authConnection.sendTCP(response);
            authConnection.close();
            return;
        }

        String hashPassword = BCrypt.hashpw(password + GameServer.SALT, account.getSalt());
        if (account.getHashedPassword().equalsIgnoreCase(hashPassword)) {
            authConnection.setAccount(account);
            response.code = OK;
            authConnection.sendTCP(response);
        } else {
            response.code = INCORRECT;
            authConnection.sendTCP(response);
            authConnection.close();
        }
    }
}
