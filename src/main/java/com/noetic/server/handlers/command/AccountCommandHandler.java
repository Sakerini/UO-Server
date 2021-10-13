package com.noetic.server.handlers.command;

import com.noetic.server.GameServer;
import com.noetic.server.enums.AccountLevel;
import com.noetic.server.enums.LogType;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class AccountCommandHandler extends CommandHandler {

    private final HashMap<String, Method> subCommands = new LinkedHashMap<String, Method>();

    public AccountCommandHandler() {
        super("account", AccountLevel.Administrator);

        /** Register sub-commands with a method. **/
        try {
            subCommands.put("create", this.getClass().getDeclaredMethod("handleCreation", String[].class));
        } catch (NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
    }

    private void handleCreation(String[] args) {
        GameServer.getServerConsole().writeMessage(LogType.Server, "Account is Creating xD");
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
