package net.njay.serverinterconnect.packet;

import java.util.HashMap;

public class PacketType {

	private static HashMap<Integer, String> ids = new HashMap<Integer, String>();
	
	public static void register(int id, String className){
		ids.put(id, className);
	}
	
	public static boolean isValid(int id, String className){
		if (ids.get(id) == null)
			return false;
		return ids.get(id).equals(className);
	}
}
