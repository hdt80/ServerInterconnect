package net.njay.serverinterconnect.packet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SealedObject;

import net.njay.serverinterconnect.api.packet.Packet;
import net.njay.serverinterconnect.encoder.Base64Coder;
import net.njay.serverinterconnect.encoder.EncryptionUtil;

public abstract class SerializablePacket extends Packet implements Serializable{

	private static final long serialVersionUID = 8603315557752257705L;
	
	protected String originAddress;
	protected ArrayList<String> recipientList = null;
	
	public SerializablePacket(String... recipients){
		recipientList = new ArrayList<String>();
		for (String s : recipients) recipientList.add(s);
	}
	
	public SerializablePacket(ArrayList<String> recipients){
		this.recipientList = recipients;
	}

	public ArrayList<String> getRecipients(){ return this.recipientList; }
	public void setRecipients(ArrayList<String> recipients){ this.recipientList = recipients; }
	public void setOriginAddress(String address){ this.originAddress = address; }
	public String getOriginAddress(){ return this.originAddress; }
	
	public String serialize() throws IOException, IllegalBlockSizeException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(buffer);
		oos.writeObject(new SealedObject(this, EncryptionUtil.getEncryptionCipher()));
		oos.flush(); oos.close();
		return new String(Base64Coder.encode(buffer.toByteArray()));
	}
	
	public static SerializablePacket deserialize(String serialized) throws ClassNotFoundException, IOException, IllegalBlockSizeException, BadPaddingException {
		ByteArrayInputStream in = new ByteArrayInputStream(Base64Coder.decode(serialized));
		ObjectInputStream ois = new ObjectInputStream(in);
		SealedObject obj = (SealedObject) ois.readObject();
		SerializablePacket p = (SerializablePacket) obj.getObject(EncryptionUtil.getDecryptionCipher());
		in.close(); ois.close();
		return p;
	}

}
