package com.noetic.server.handlers.command;

import com.noetic.server.GameServer;
import com.noetic.server.enums.AccountLevel;
import com.noetic.server.enums.LogType;
import com.noetic.server.service.AccountService;
import com.noetic.server.service.impl.AccountServiceImpl;
import com.noetic.server.utils.BCrypt;
import lombok.SneakyThrows;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.util.*;

public class AccountCommandHandler extends CommandHandler {
    private final int OK = 0;
    private final int ARGUMENTS_ERROR = -1;

    private final HashMap<String, Method> subCommands = new LinkedHashMap<>();
    private final AccountService accountService = new AccountServiceImpl();

    public AccountCommandHandler() {
        super("account", AccountLevel.Administrator);

        /** Register sub-commands with a method. **/
        try {
            subCommands.put("create", this.getClass().getDeclaredMethod("handleCreation", String[].class));
        } catch (NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
    }

    @SneakyThrows
    private void handleCreation(String[] args) {
        if (args.length != 4) {
            GameServer.getServerConsole().writeMessage(LogType.Server, "Usage: account create [username] [password].");
            return;
        }

        String username = args[2].toUpperCase();
        String password = args[3];

        MessageDigest md= MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(password.getBytes());
        String shaHashedPassword = BCrypt.BytesToHex (hash);

        String bcrypt_salt = BCrypt.gensalt(12);
        String bcrypt_hash = BCrypt.hashpw(shaHashedPassword + GameServer.SALT, bcrypt_salt);

        accountService.createAccount(username, bcrypt_hash, bcrypt_salt);
    }

    @Override
    public void handleCommand(String[] args) {
        if (args.length == 1)
            GameServer.getServerConsole().writeMessage(LogType.Server, getAvailableCommands());

        Method method = null;
        if (args.length >= 2) {
            String subCommand = args[1];
            for (Map.Entry<String, Method> set : subCommands.entrySet()) {
                if (set.getKey().equals(subCommand)) {
                    method = set.getValue();
                }
            }

            if (Objects.isNull(method)) {
                GameServer.getServerConsole().writeMessage(LogType.Server, getAvailableCommands());
            } else {
                invokeHandlingMethod(method, args);
            }

        }
    }

    private void invokeHandlingMethod(Method method, String[] args) {
        try {
            method.invoke(this, (Object)args);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private String getAvailableCommands() {
        StringBuilder buffer = new StringBuilder();
        ArrayList<String> subs = new ArrayList<>();

        for (Map.Entry<String, Method> set : subCommands.entrySet()) {
            subs.add(set.getKey());
        }

        for (int i = 0; i < subs.size(); i++) {
            String str = subs.get(i);
            if (i+1 != subs.size())
                buffer.append(str).append(", ");
            else
                buffer.append(str);
        }
        return "Available sub-commands for account: " + buffer.toString();
    }
}
