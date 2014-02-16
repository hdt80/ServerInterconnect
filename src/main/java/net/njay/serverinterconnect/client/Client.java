package net.njay.serverinterconnect.client;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.ShortBufferException;

import net.njay.serverinterconnect.packet.SerializablePacket;

public class Client {

	private static ClientThread thread;
	
	public static void connect(String hostName, int port) {
        thread = new ClientThread(hostName, port);
    }
	
	public static void send(SerializablePacket p) throws IOException, InvalidKeyException, 
	InvalidAlgorithmParameterException, IllegalBlockSizeException, 
	ShortBufferException, BadPaddingException{
		getThread().sendPacket(p);
	}
	
	public static ClientThread getThread(){
		return thread;
	}

}
