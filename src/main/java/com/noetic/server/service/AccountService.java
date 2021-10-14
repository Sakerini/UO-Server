package com.noetic.server.service;

import com.noetic.server.domain.model.Account;
import com.noetic.server.enums.QueueStatus;

public interface AccountService {
    QueueStatus createAccount(String username, String hash, String salt);
    Account getAccount(String username);
    boolean isOnline(String username);
}
