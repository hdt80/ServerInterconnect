package net.njay.serverinterconnect.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.ShortBufferException;

import net.njay.serverinterconnect.ServerInterconnect;
import net.njay.serverinterconnect.event.PacketRecievedEvent;
import net.njay.serverinterconnect.packet.Packet;
import net.njay.serverinterconnect.packet.PacketType;


public class ServerThread extends Thread {
    public Socket socket = null;
    public DataOutputStream out = null;
    public DataInputStream in = null;
    public String name = "";
    public String longname = "";
    public String host;

    public ServerThread(Socket socket) {
        this.socket = socket;
        host = socket.getInetAddress().getCanonicalHostName() + ":" + socket.getPort();
    }

    public boolean listen() throws IOException, ClassNotFoundException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException{
    	String protocol = in.readUTF();
		if (!ServerInterconnect.getXMLBridge().getProtocol().equals(protocol))
			throw new RuntimeException("Protocols do not match!");
		int packetID = in.readInt();
		String packetSerialized = in.readUTF(); 
		Packet p = Packet.deserialize(packetSerialized);
		if (packetID != p.getId())
			throw new RuntimeException("Packet ID mismatch!");
		if (!PacketType.isValid(p.getId(), p.getClass().getName()))
			throw new RuntimeException("Packet ID not recognized!");
		System.out.println("Packet recieved!");
		PacketRecievedEvent.call(p);
		return true;
	}
    
    public void send(Packet p) throws IOException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, ShortBufferException, BadPaddingException{
    	p.send(out);
    }

    public void run() {
        try {
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());
            while (listen());
        } catch (EOFException e) {
        	System.out.println("Possible malformed packet from: " + socket.getRemoteSocketAddress());
            run();
        	return;
        } catch (Exception e) {
            e.printStackTrace();
            return;
        } finally {
            try {
                if (out != null)
                    out.close();
                if (in != null)
                    in.close();
                socket.close();

                Server.threads.remove(this);
            } catch (IOException e) {
            }
        }
    }

}
