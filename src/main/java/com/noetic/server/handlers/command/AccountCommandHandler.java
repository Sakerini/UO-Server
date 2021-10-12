package com.noetic.server.handlers.command;

import com.noetic.server.enums.AccountLevel;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class AccountCommandHandler extends CommandHandler {

    private HashMap<String, Method> subCommands = new LinkedHashMap<String, Method>();

    public AccountCommandHandler() {
        super("account", AccountLevel.Administrator);

        /** Register sub-commands with a method. **/
        try {
            subCommands.put("create", this.getClass().getDeclaredMethod("handleCreation", String[].class));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private void handleCreation(String[] args) {
    }

    @Override
    public void handleCommand(String[] args) {

    }

    @Override
    public String toString() {
        return null;
    }
}
