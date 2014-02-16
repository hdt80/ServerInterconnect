package net.njay.serverinterconnect.event;

import net.njay.customevents.event.Event;
import net.njay.serverinterconnect.packet.SerializablePacket;

public class PacketRecievedEvent extends Event{

	private SerializablePacket packet;
	
	public PacketRecievedEvent(SerializablePacket packet){
		this.packet = packet;
	}
	
	public SerializablePacket getPacket(){
		return this.packet;
	}

}
