package com.noetic.server.network.handler;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.noetic.server.domain.model.GameCharacter;
import com.noetic.server.enums.QueueStatus;
import com.noetic.server.network.connections.AuthConnection;
import com.noetic.server.network.packets.*;
import com.noetic.server.service.CharacterService;
import com.noetic.server.service.impl.CharacterServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CharacterHandler implements PacketHandler {
    private final int SERVER_ERROR = -1;
    private final int EXISTS = 1;
    private final int OK = 0;

    @Override
    public void handlePacket(Server server, Connection connection, APacket packet) {
        if (packet instanceof CharacterListCSPacket) {
            handleCharacterListPacket(((AuthConnection) connection));
        } else if (packet instanceof CharacterCreateCSPacket){
            handleCharacterCreatePacket(((AuthConnection) connection), ((CharacterCreateCSPacket) packet));
        }
    }

    private void handleCharacterListPacket(AuthConnection connection) {
        Logger.getLogger("server").log(Level.INFO, "Sending character list packet to: {0}", connection.getAccount().getUsername());
        ArrayList<CharacterSCPacket> scCharacterList = new ArrayList<>();
        for (GameCharacter character : connection.getAccount().getCharacters()) {
            CharacterSCPacket characterSCPacket = new CharacterSCPacket();
            characterSCPacket.name = character.getName();
            characterSCPacket.zone = character.getZoneID();
            characterSCPacket.gender = character.getGenderID();
            scCharacterList.add(characterSCPacket);
        }
        CharacterListSCPacket packet = new CharacterListSCPacket();
        packet.characterList = scCharacterList;
        connection.sendTCP(packet);
    }

    private void handleCharacterCreatePacket(AuthConnection connection, CharacterCreateCSPacket packet) {
        Logger.getLogger("server").log(Level.INFO, "Creation character packet to: {0}", connection.getAccount().getUsername());
        CharacterService characterService = new CharacterServiceImpl();
        QueueStatus status = characterService.createCharacter(packet.name, packet.gender, connection.getAccount());
        CharacterCreateSCPacket response = new CharacterCreateSCPacket();
        if (status.equals(QueueStatus.Failed)) {
            response.code = EXISTS;
        } else if(status.equals(QueueStatus.Error)){
            response.code = SERVER_ERROR;
        } else {
            response.code = OK;
        }
        connection.sendTCP(response);
    }
}
