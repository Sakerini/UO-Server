package com.noetic.server.service;

import com.noetic.server.domain.model.Account;
import com.noetic.server.enums.QueueStatus;

public interface CharacterService {
    QueueStatus createCharacter(String name, int gender, Account account);
    QueueStatus deleteCharacter(String name);
}
