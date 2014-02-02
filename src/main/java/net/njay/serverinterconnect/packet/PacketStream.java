package net.njay.serverinterconnect.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

import net.njay.serverinterconnect.ServerInterconnect;
import net.njay.serverinterconnect.event.PacketRecievedEvent;

public class PacketStream {
	
	public static Packet read(DataInputStream in, boolean callEvent) throws ClassNotFoundException, IllegalBlockSizeException, BadPaddingException, IOException{
		PacketHeader header = PacketHeader.deserialize(in.readUTF());
		if (!ServerInterconnect.getXMLBridge().getProtocol().equals(header.getProtocol()))
			throw new RuntimeException("Protocols do not match!");

		Packet p = Packet.deserialize(in.readUTF());
		if (header.getPacketID() != p.getPacketId())
			throw new RuntimeException("Packet ID mismatch!");
		if (!PacketType.isValid(p.getPacketId(), p.getClass().getName()))
			throw new RuntimeException("Packet ID not recognized!");

		if (callEvent)
			PacketRecievedEvent.call(p);
		return p;
	}
	
	public static void write(DataOutputStream out, PacketHeader header, Packet p) throws IllegalBlockSizeException, IOException{
		out.writeUTF(header.serialize());
		out.writeUTF(p.serialize());
	}

}
