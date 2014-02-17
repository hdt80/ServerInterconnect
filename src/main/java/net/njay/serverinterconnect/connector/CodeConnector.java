package net.njay.serverinterconnect.connector;

import net.njay.serverinterconnect.ServerInterconnect;
import net.njay.serverinterconnect.client.Client;
import net.njay.serverinterconnect.xml.ConnectionConfig;

public class CodeConnector {

	private String hostName;
	private String password;
	private int port;
	private String serverPassword;
	
	public CodeConnector(String hostName, int port, String password, String serverPassword){
		this.hostName = hostName;
		this.port = port;
		this.password = password;
		this.serverPassword = serverPassword;
	}
	
	public void connect(ConnectionConfig config){
		ServerInterconnect.initialize(config, false);
		Client.connect(hostName, port);
	}
}
