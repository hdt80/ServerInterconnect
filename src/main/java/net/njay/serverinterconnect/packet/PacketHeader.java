package net.njay.serverinterconnect.packet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SealedObject;

import net.njay.serverinterconnect.ServerInterconnect;
import net.njay.serverinterconnect.encoder.Base64Coder;

public class PacketHeader implements Serializable{
	
	private static final long serialVersionUID = 2701076415301210897L;
	
	private String protocol;
	private String clientID;
	private int id;
	
	public PacketHeader(String protocol, String clientID, int id){
		this.protocol = protocol;
		this.clientID = clientID;
		this.id = id;
	}
	
	public String getProtocol(){ return this.protocol; }
	public String geClientID() { return this.clientID; }
	public int getPacketID(){ return this.id; }
	
	@Override
	public boolean equals(Object obj){
		if (!(obj instanceof PacketHeader)) return false;
		PacketHeader header = (PacketHeader) obj;
		return (header.getProtocol().equals(protocol)) && (header.geClientID().equals(clientID)) && (header.getPacketID() == id);
	}
	
	public String serialize() throws IOException, IllegalBlockSizeException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(buffer);
		oos.writeObject(this);
		oos.flush(); oos.close();
		return new String(Base64Coder.encode(buffer.toByteArray()));
	}
	
	public static PacketHeader deserialize(String serialized) throws ClassNotFoundException, IOException, IllegalBlockSizeException, BadPaddingException {
		ByteArrayInputStream in = new ByteArrayInputStream(Base64Coder.decode(serialized));
		ObjectInputStream ois = new ObjectInputStream(in);
		PacketHeader p = (PacketHeader) ois.readObject();
		in.close(); ois.close();
		return p;
	}
	
	public static PacketHeader toPacketHeader(SerializablePacket packet){
		return new PacketHeader(ServerInterconnect.getConfig().getProtocol(),
				ServerInterconnect.getConfig().getUsername(), packet.getPacketId());
	}

}
