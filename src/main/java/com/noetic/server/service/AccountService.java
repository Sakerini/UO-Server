package com.noetic.server.service;

import com.noetic.server.enums.QueueStatus;

public interface AccountService {
    public QueueStatus createAccount(String username, String hash, String salt);
}
