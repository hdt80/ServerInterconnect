package net.njay.serverinterconnect.encoder;

import javax.crypto.Cipher;

import net.njay.serverinterconnect.ServerInterconnect;

public class EncryptionUtil {

	public static Encoder encoder = new Encoder(ServerInterconnect.getXMLBridge().getKeyBytes(), 
			ServerInterconnect.getXMLBridge().getIVBytes());
	static{
		try {
			encoder.init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static Cipher getEncryptionCipher(){
		return encoder.getEncryptionCipher();
	}
	public static Cipher getDecryptionCipher(){
		return encoder.getDecryptionCipher();
	}

}
