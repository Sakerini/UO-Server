package com.noetic.server.service.impl;

import com.esotericsoftware.kryonet.Connection;
import com.noetic.server.GameServer;
import com.noetic.server.domain.model.Account;
import com.noetic.server.enums.AccountLevel;
import com.noetic.server.enums.LogType;
import com.noetic.server.enums.QueueStatus;
import com.noetic.server.service.AccountService;
import com.noetic.server.utils.Configuration;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AccountServiceImpl implements AccountService {

    private static List<Account> accounts = new ArrayList<>();

    @Override
    public QueueStatus createAccount(String username, String hash, String salt) {
        File accountFolder = new File(Configuration.accountDataPath + "/" + username);
        if (accountFolder.mkdirs()) {
            File accountFile = new File(accountFolder + "/" + username + ".data");
            try (DataOutputStream writer = new DataOutputStream(new FileOutputStream(accountFile))) {
                writer.writeUTF(username);
                writer.writeUTF(hash);
                writer.writeUTF(salt);
                writer.writeInt(AccountLevel.Player.getLevel());

                GameServer.getServerConsole().writeMessage(LogType.Server, "Account created: " + username);
                Account newAccount = new Account();
                newAccount.setUsername(username);
                newAccount.setHashedPassword(hash);
                newAccount.setSalt(salt);
                accounts.add(newAccount);
                return QueueStatus.Success;
            } catch (IOException ex) {
                GameServer.getServerConsole().writeMessage(LogType.Server, "Unable to create account. See console log.");
                Logger.getLogger("server").log(Level.SEVERE, "Failed to create account '{0}'; {1}", new Object[] { username, ex.getMessage() });
                return QueueStatus.Error;
            }
        } else {
            GameServer.getServerConsole().writeMessage(LogType.Server, "Account '" + username + "' already exists.");
            return QueueStatus.Failed;
        }
    }

    @Override
    public Account getAccount(String username) {
        for (Account account : accounts) {
            if (account.getUsername().equalsIgnoreCase(username)) {
                return account;
            }
        }
        return null;
    }

    @Override
    public boolean isOnline(String username) {
        //todo check auth connections
        //todo check world connections
        return false;
    }
}