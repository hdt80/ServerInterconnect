package net.njay.serverinterconnect.packet.packets;

import net.njay.serverinterconnect.ServerInterconnect;
import net.njay.serverinterconnect.packet.Packet;

public class AuthenticationPacket extends Packet{

	private static final long serialVersionUID = 4782575110658769181L;
	
	private String username, password;
	
	public AuthenticationPacket(String username, String password){
		super(getId(), username);
		this.username = username;
		this.password = password;
	}
	
	public String getUsername(){ return this.username; }
	public String getPassword(){ return this.password; }

	public static int getId(){
		return 3;
	}
	
	public boolean authenticate(){
		return password.equals(ServerInterconnect.getXMLBridge().getPassword());
	}
	
}
