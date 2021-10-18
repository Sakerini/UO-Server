package com.noetic.server.network;

import com.esotericsoftware.kryo.Kryo;
import com.noetic.server.network.packets.*;

import java.util.ArrayList;

public class Network {

	public static void registerLib(Kryo kryo) {
		kryo.register(LoginSCPacket.class);
		kryo.register(LoginCSPacket.class);
		kryo.register(CharacterListSCPacket.class);
		kryo.register(CharacterListCSPacket.class);
		kryo.register(CharacterSCPacket.class);
		kryo.register(CharacterCreateSCPacket.class);
		kryo.register(CharacterCreateCSPacket.class);
		kryo.register(ArrayList.class);
	}
}
