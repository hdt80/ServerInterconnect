package net.njay.serverinterconnect.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.util.ArrayList;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.ShortBufferException;

import net.njay.customevents.event.Event;
import net.njay.customevents.event.EventHandler;
import net.njay.customevents.event.Listener;
import net.njay.serverinterconnect.event.PacketRecievedEvent;
import net.njay.serverinterconnect.packet.Packet;

public class Server implements Listener{
    public static ArrayList<ClientConnection> activeConnections;
    public static ServerSocket serverSocket = null;

    public static void start(int port) {
    	Event.addListener(new Server());
    	activeConnections = new ArrayList<ClientConnection>();
        System.out.println("Starting server...");
        try {
        	serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println("Could not listen on port " + port);
            System.exit(-1);
        }
        System.out.println("Listening on port " + port + ".");
        new ListenerThread().start();
    }

    public static void relay(Packet packet) throws IOException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, ShortBufferException, BadPaddingException {
    	if (packet.getRecipients() == null)  
    		for (ClientConnection thread : activeConnections){
	        	 thread.getThread().send(packet);
	        }
    	else
    		for (ClientConnection thread : activeConnections){
    			if (packet.getRecipients().contains(thread.getId()))
    				thread.getThread().send(packet);
	        }
    }

    public static void stop() {
        try {
            System.out.println("Terminating Server.");
            serverSocket.close();
            for (ClientConnection thread : activeConnections)
                thread.getThread().socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @EventHandler
    public void onRecieve(PacketRecievedEvent e) throws InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, ShortBufferException, BadPaddingException, IOException{
    	relay(e.getPacket());
    }
}
