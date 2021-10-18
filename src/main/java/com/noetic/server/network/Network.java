package com.noetic.server.network;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Server;
import com.noetic.server.network.packets.WorldConnectionSCPacket;
import com.noetic.server.network.packets.*;

import java.util.ArrayList;

public class Network {

	private static Server authServer;
	private static Server worldServer;

	public static void registerLib(Kryo kryo) {
		kryo.register(LoginSCPacket.class);
		kryo.register(LoginCSPacket.class);
		kryo.register(CharacterListSCPacket.class);
		kryo.register(CharacterListCSPacket.class);
		kryo.register(CharacterSCPacket.class);
		kryo.register(CharacterCreateSCPacket.class);
		kryo.register(CharacterCreateCSPacket.class);
		kryo.register(ArrayList.class);
		kryo.register(WorldConnectionCSPacket.class);
		kryo.register(WorldConnectionSCPacket.class);
		kryo.register(WorldPositionSCPacket.class);
		kryo.register(PlayerSCPacket.class);
		kryo.register(PlayerListSCPacket.class);
		kryo.register(WorldSCPacket.class);
	}

	public static Server getAuthServer() {
		return authServer;
	}

	public static void setAuthServer(Server authServer) {
		Network.authServer = authServer;
	}

	public static Server getWorldServer() {
		return worldServer;
	}

	public static void setWorldServer(Server worldServer) {
		Network.worldServer = worldServer;
	}
}
