package net.njay.serverinterconnect.event;

import net.njay.customevents.event.Event;
import net.njay.customevents.event.EventCaller;
import net.njay.serverinterconnect.packet.Packet;

public class PacketRecievedEvent extends Event{

	private Packet packet;
	
	public PacketRecievedEvent(Packet packet){
		this.packet = packet;
	}
	
	public Packet getPacket(){
		return this.packet;
	}
	
	public static void call(Packet p){
		PacketRecievedEvent e = new PacketRecievedEvent(p);
		EventCaller<PacketRecievedEvent> caller = new EventCaller<PacketRecievedEvent>();
		caller.call(Event.getListeners(), e);
	}

}
