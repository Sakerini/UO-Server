package com.noetic.server.service.impl;

import com.noetic.server.GameServer;
import com.noetic.server.domain.model.Account;
import com.noetic.server.domain.model.GameCharacter;
import com.noetic.server.enums.LogType;
import com.noetic.server.enums.QueueStatus;
import com.noetic.server.service.CharacterService;
import com.noetic.server.utils.Configuration;

import java.io.*;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CharacterServiceImpl implements CharacterService {
    @Override
    public QueueStatus createCharacter(String name, int gender, Account account) {
        File accountFolder = new File(Configuration.accountDataPath + "/" + account.getUsername());
        if (accountFolder.exists() && accountFolder.isDirectory()) {
            File characterFolder = new File(accountFolder.getAbsolutePath() + "/Characters");
            characterFolder.mkdir();
            name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
            File characterFile = new File(characterFolder + "/" + name + ".data");
            if (characterFile.exists()) {
                Logger.getLogger("server").log(Level.WARNING, "Account {0} trying to create existing character!", account.getUsername());
                return QueueStatus.Failed;
            }
            try {
                DataOutputStream stream = new DataOutputStream(new FileOutputStream(characterFile));
                int zone = 0;
                stream.writeUTF(name);
                stream.writeByte(zone);
                stream.writeByte(gender);
                stream.close();
                GameCharacter character = new GameCharacter();
                character.setName(name);
                character.setGenderID(gender);
                character.setZoneID(zone);
                account.getCharacters().add(character);
                Logger.getLogger("server").log(Level.INFO, "Account {0} created character!", account.getUsername());
                return QueueStatus.Success;
            } catch (IOException e) {
                GameServer.getServerConsole().writeMessage(LogType.Server, "Unable to create character. See console log.");
                Logger.getLogger("server").log(Level.SEVERE, "Account {0} failed to create character!", account.getUsername());
                e.printStackTrace();
            }
        }
        return QueueStatus.Failed;
    }

    @Override
    public QueueStatus deleteCharacter(String name) {
        return null;
    }
}
