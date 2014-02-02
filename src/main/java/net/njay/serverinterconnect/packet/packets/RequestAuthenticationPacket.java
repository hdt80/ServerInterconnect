package net.njay.serverinterconnect.packet.packets;

import net.njay.serverinterconnect.packet.Packet;

public class RequestAuthenticationPacket extends Packet{

	private static final long serialVersionUID = -6821972949733137450L;

	public RequestAuthenticationPacket(String senderId) {
		super(getId(), senderId);
	}
	
	public static int getId(){
		return 2;
	}

}
