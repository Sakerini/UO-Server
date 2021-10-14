package com.noetic.server.network;

import com.esotericsoftware.kryo.Kryo;
import java.util.ArrayList;

public class Network {

	public static void registerLib(Kryo kryo) {
		kryo.register(ArrayList.class);
	}
}
