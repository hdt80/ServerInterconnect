package net.njay.serverinterconnect.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.ShortBufferException;

import net.njay.customevents.event.Event;
import net.njay.serverinterconnect.ServerInterconnect;
import net.njay.serverinterconnect.client.Client;
import net.njay.serverinterconnect.event.PacketRecievedEvent;
import net.njay.serverinterconnect.log.Log;
import net.njay.serverinterconnect.server.Server;
import net.njay.serverinterconnect.xml.XMLBridge;

public class PacketStream {
	
	public static SerializablePacket read(DataInputStream in, boolean callEvent) throws ClassNotFoundException, IllegalBlockSizeException, BadPaddingException, IOException{
		SerializablePacket p = SerializablePacket.deserialize(in.readUTF());
		if (callEvent)
			Event.callEvent(new PacketRecievedEvent(p));
		return p;
	}
	
	public static void write(SerializablePacket p) throws IllegalBlockSizeException, IOException, InvalidKeyException, InvalidAlgorithmParameterException, ShortBufferException, BadPaddingException{
		if (ServerInterconnect.getConfig().getMode() == XMLBridge.Mode.CLIENT){
			Log.debug("Sending Packet: " + p.getClass().getName());
			DataOutputStream out = Client.getThread().getOutputStream();
			out.writeUTF(p.serialize());
		}
		else
			Server.relay(p);
	}
	
	public static void safeWrite(SerializablePacket p) {
		try{
			if (ServerInterconnect.getConfig().getMode() == XMLBridge.Mode.CLIENT){
				Log.debug("Sending Packet: " + p.getClass().getName());
				DataOutputStream out = Client.getThread().getOutputStream();
				out.writeUTF(p.serialize());
			}
			else
				Server.relay(p);
		}catch(Exception e){ e.printStackTrace(); }
	}
	
	public static void writeOut(DataOutputStream out, SerializablePacket p) throws IllegalBlockSizeException, IOException{
		Log.debug("Sending Packet: " + p.getClass().getName());
		out.writeUTF(p.serialize());
	}

}
