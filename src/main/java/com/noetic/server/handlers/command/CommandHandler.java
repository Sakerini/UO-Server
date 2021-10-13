package com.noetic.server.handlers.command;

import com.noetic.server.enums.AccountLevel;

public abstract class CommandHandler {

    private String prefix;
    private AccountLevel level;

    public CommandHandler(String prefix, AccountLevel level) {
        this.prefix = prefix;
        this.level = level;
    }

    public abstract void handleCommand(String[] args);

    public String getPrefix() {
        return prefix;
    }

    public AccountLevel getLevel() {
        return level;
    }
}
