package net.njay.serverinterconnect.packet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SealedObject;
import javax.crypto.ShortBufferException;

import net.njay.serverinterconnect.ServerInterconnect;
import net.njay.serverinterconnect.encoder.Base64Coder;
import net.njay.serverinterconnect.encoder.EncryptionUtil;

public abstract class Packet implements Serializable{

	private static final long serialVersionUID = 8603315557752257705L;

	public void send(DataOutputStream out) throws IOException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, ShortBufferException, BadPaddingException{
		out.writeUTF(ServerInterconnect.getXMLBridge().getProtocol());
		out.writeInt(getId());
		out.writeUTF(serialize());
	}
	
	public abstract int getId();
	
	public String serialize() throws IOException, IllegalBlockSizeException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(buffer);
		oos.writeObject(new SealedObject(this, EncryptionUtil.getEncryptionCipher()));
		oos.flush(); oos.close();
		return new String(Base64Coder.encode(buffer.toByteArray()));
	}
	
	public static Packet deserialize(String serialized) throws ClassNotFoundException, IOException, IllegalBlockSizeException, BadPaddingException {
		ByteArrayInputStream in = new ByteArrayInputStream(Base64Coder.decode(serialized));
		ObjectInputStream ois = new ObjectInputStream(in);
		SealedObject obj = (SealedObject) ois.readObject();
		Packet p = (Packet) obj.getObject(EncryptionUtil.getDecryptionCipher());
		in.close(); ois.close();
		return p;
	}

}
