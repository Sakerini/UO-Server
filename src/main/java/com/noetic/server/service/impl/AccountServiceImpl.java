package com.noetic.server.service.impl;

import com.noetic.server.GameServer;
import com.noetic.server.domain.model.Account;
import com.noetic.server.domain.model.GameCharacter;
import com.noetic.server.enums.AccountLevel;
import com.noetic.server.enums.LogType;
import com.noetic.server.enums.QueueStatus;
import com.noetic.server.service.AccountService;
import com.noetic.server.utils.Configuration;
import org.w3c.dom.CDATASection;

import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AccountServiceImpl implements AccountService {
    private static final List<Account> accounts = new ArrayList<>();

    public static void loadAccounts(){
        File[] accFiles = new File(Configuration.accountDataPath).listFiles();
        if (Objects.nonNull(accFiles)) {
            for (File file : accFiles) {
                if (file.isDirectory()) {
                    String name = file.getName();
                    File dataFile = new File(file.getAbsolutePath() + "/" + name + ".data");
                    if (dataFile.exists()) {
                        loadAccount(dataFile);
                    }
                }
            }
            List<Account> tmp = accounts;
            GameServer.getServerConsole().writeMessage(LogType.Server, ("Loaded " + accounts.size() + " accounts"));
        }
    }

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

                Account newAccount = new Account();
                newAccount.setUsername(username);
                newAccount.setHashedPassword(hash);
                newAccount.setSalt(salt);
                accounts.add(newAccount);
                return QueueStatus.Success;
            } catch (IOException ex) {
                GameServer.getServerConsole().writeMessage(LogType.Server, "Unable to create account. See console log.");
                Logger.getLogger("server").log(Level.SEVERE, "Failed to create account '{0}'; {1}", new Object[]{username, ex.getMessage()});
                return QueueStatus.Error;
            }
        } else {
            GameServer.getServerConsole().writeMessage(LogType.Server, "Account '" + username + "' already exists.");
            return QueueStatus.Failed;
        }
    }

    @Override
    public QueueStatus deleteAccount(String username) {
        for (Account account : accounts) {
            if (account.getUsername().equalsIgnoreCase(username)) {
                File accountFolder = new File(Configuration.accountDataPath + "/" + username);
                try {
                    deleteDirectory(accountFolder);
                } catch (IOException e) {
                    e.printStackTrace();
                    return QueueStatus.Error;
                }
                accounts.remove(account);
                return QueueStatus.Success;
            }
        }
        return QueueStatus.Failed;
    }

    private void deleteDirectory(File file) throws IOException {
        File[] accFiles = file.listFiles();
        if (Objects.nonNull(accFiles)) {
            for (File f : accFiles) {
                if (!Files.isSymbolicLink(f.toPath())) {
                    deleteDirectory(f);
                }
            }
        }
        boolean delete = file.delete();
        if (!delete) {
            throw new IOException();
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

    private static void loadAccount(File data) {
        try {
            DataInputStream stream = new DataInputStream(new FileInputStream(data));
            Account account = new Account();
            account.setUsername(stream.readUTF());
            account.setHashedPassword(stream.readUTF());
            account.setSalt(stream.readUTF());
            int level = stream.readInt();
            for (AccountLevel accountLevel : AccountLevel.values()) {
                if (accountLevel.getLevel() == level) {
                    account.setSecurity(accountLevel);
                }
            }
            loadCharacters(account);
            accounts.add(account);
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadCharacters(Account account) {
        File characterFolder = new File(Configuration.accountDataPath + "/"+ account.getUsername() + "/Characters");
        if (characterFolder.exists()) {
            File[] characters = characterFolder.listFiles();
            if (Objects.nonNull(characters) && characters.length > 0) {
                for (File dataFile : characters) {
                    readCharacters(dataFile, account);
                }
            }
            Logger.getLogger("server").log(Level.INFO, "Loaded {0} characters on account {1}.",
                    new Object[] { account.getCharacters().size(), account.getUsername() });
        }
    }

    private static void readCharacters(File dataFile, Account account) {
        try (DataInputStream stream = new DataInputStream(new FileInputStream(dataFile))) {
            GameCharacter character = new GameCharacter();
            character.setName(stream.readUTF());
            character.setZoneID(stream.readByte());
            character.setGenderID(stream.readByte());
            account.getCharacters().add(character);
        } catch (IOException ex) {
            Logger.getLogger("server").log(Level.SEVERE, "Failed to load character; {0}", ex.getMessage());
        }
    }
}