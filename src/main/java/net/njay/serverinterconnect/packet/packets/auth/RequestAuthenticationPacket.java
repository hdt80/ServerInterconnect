package net.njay.serverinterconnect.packet.packets.auth;

import net.njay.serverinterconnect.packet.SerializablePacket;

public class RequestAuthenticationPacket extends SerializablePacket{

	private static final long serialVersionUID = -6821972949733137450L;
	
	public int getPacketId(){
		return 2;
	}

}
