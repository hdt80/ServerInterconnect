package net.njay.serverinterconnect.packet.packets.auth;

import net.njay.serverinterconnect.ServerInterconnect;
import net.njay.serverinterconnect.packet.SerializablePacket;

public class AuthenticationPacket extends SerializablePacket{

	private static final long serialVersionUID = 4782575110658769181L;
	
	private String username, password;
	
	public AuthenticationPacket(String username, String password){
		this.username = username;
		this.password = password;
	}
	
	public String getUsername(){ return this.username; }
	public String getPassword(){ return this.password; }

	public int getPacketId(){
		return 3;
	}
	
	public boolean authenticate(){
		return password.equals(ServerInterconnect.getConfig().getPassword());
	}
	
}
