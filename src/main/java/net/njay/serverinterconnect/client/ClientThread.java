package net.njay.serverinterconnect.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.util.LinkedList;
import java.util.Queue;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

import net.njay.customevents.event.Event;
import net.njay.customevents.event.EventHandler;
import net.njay.customevents.event.Listener;
import net.njay.serverinterconnect.ServerInterconnect;
import net.njay.serverinterconnect.event.PacketRecievedEvent;
import net.njay.serverinterconnect.packet.Packet;
import net.njay.serverinterconnect.packet.PacketHeader;
import net.njay.serverinterconnect.packet.PacketStream;
import net.njay.serverinterconnect.packet.packets.AuthenticationPacket;
import net.njay.serverinterconnect.packet.packets.RequestAuthenticationPacket;
import net.njay.serverinterconnect.xml.XMLBridge;

public class ClientThread extends Thread implements Listener{

	public Socket socket = null;
    public DataOutputStream out = null;
    public DataInputStream in = null;
    
    private Queue<Packet> packetQueue = new LinkedList<Packet>();
    private State currentState;
    private String serverAddress;
    private int port;
	
    public ClientThread(String serverAddress, int port){
    	setState(State.NOT_CONNECTED);
    	this.serverAddress = serverAddress;
    	this.port = port;
    	Event.addListener(this);
    	start();
    }
    
    public void sendPacket(Packet packet) throws IllegalBlockSizeException, IOException{
    	if (currentState != State.CONNECTED)
    		packetQueue.add(packet);
    	else
    		PacketStream.write(getOutputStream(), new PacketHeader(ServerInterconnect.getXMLBridge().getProtocol(),
    				ServerInterconnect.getXMLBridge().getID(), packet.getPacketId()), packet);
    }
    
	@Override
	public void run(){
		try {
			init();
		} catch (UnknownHostException e) {
			System.out.println("Could not connect to server: Unknown host!");
			e.printStackTrace();
			return;
		} catch (IOException e) {
			System.out.println("Could not connect to server: Failed to connect.");
			e.printStackTrace();
			return;
		}
		setState(State.WAITING_AUTH);
		System.out.println("Connected to " + socket.getInetAddress() + " successfully!");
		try {
			while(listen());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			System.out.println("Possible malformed packet from: " + socket.getRemoteSocketAddress());
            run();
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			System.out.println("Possible malformed packet from: " + socket.getRemoteSocketAddress());
            run();
			e.printStackTrace();
		} catch (BadPaddingException e) {
			System.out.println("Possible malformed packet from: " + socket.getRemoteSocketAddress());
            run();
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			System.out.println("Possible malformed packet from: " + socket.getRemoteSocketAddress());
            run();
			e.printStackTrace();
		} finally {
			try {
				cleanup();
			} catch (IOException e) {
				System.out.println("Failed to cleanup! Termination is recommended!");
				e.printStackTrace();
			}
			System.out.println("Disconnected sucessfully.");
		}
	}
	
	public boolean listen() throws IOException, ClassNotFoundException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException{
		PacketStream.read(in, true);
		return true;
	}
	
	public void cleanup() throws IOException{
		in.close();
		out.close();
		socket.close();
	}
	
	public void init() throws UnknownHostException, IOException{
		socket = new Socket(InetAddress.getByName(serverAddress),
    			port);
		out = new DataOutputStream(socket.getOutputStream());
        in = new DataInputStream(socket.getInputStream());
	}
	
	public DataOutputStream getOutputStream(){
		return this.out;
	}
	
	public DataInputStream getInputStream(){
		return this.in;
	}
	
	@EventHandler //TODO: POSSIBLE VULNERABILITY, CLIENTS CAN BE TRICKED INTO SENDING CREDENTIALS
	public void onPacketRecieve(PacketRecievedEvent e) throws IllegalBlockSizeException, IOException{
		if (!(e.getPacket() instanceof RequestAuthenticationPacket)) return;
		XMLBridge xml = ServerInterconnect.getXMLBridge();
		Packet p = new AuthenticationPacket(xml.getID(), xml.getPassword());
		PacketStream.write(out, new PacketHeader(xml.getProtocol(), xml.getID(), p.getPacketId()), p);
		setState(State.CONNECTED);
		Event.removeListener(this);
	}
	
	private void setState(State state){
		this.currentState = state;
		if (state == State.CONNECTED)
			try {
				flushQueue();
			} catch (Exception e) {e.printStackTrace();}
	}
	
	private void flushQueue() throws IllegalBlockSizeException, IOException{
		while (!packetQueue.isEmpty())
			sendPacket(packetQueue.poll());
	}

	public enum State{
		NOT_CONNECTED, WAITING_AUTH, CONNECTED;
	}
}