package net.njay.serverinterconnect.packet.packets;

import net.njay.serverinterconnect.packet.Packet;

public class TestPacket extends Packet{
	
	private static final long serialVersionUID = -7790693472403661813L;

	public TestPacket(String senderId){
		super(getId(), senderId);
	}
	
	public static final int getId() {
		return 1;
	}	
}
