package net.njay.serverinterconnect.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

import net.njay.serverinterconnect.packet.PacketStream;

public class ClientThread extends Thread{

	public Socket socket = null;
    public DataOutputStream out = null;
    public DataInputStream in = null;
    
    private String serverAddress;
    private int port;
	
    public ClientThread(String serverAddress, int port){
    	this.serverAddress = serverAddress;
    	this.port = port;
    	start();
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

}
