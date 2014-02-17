package net.njay.serverinterconnect.xml;

import java.io.IOException;

import org.jdom2.JDOMException;

public abstract class ConnectionConfig {

	public abstract void init() throws IOException, JDOMException;
	public abstract String getProtocol();
	public abstract int getPort();
	public abstract String getUsername();
	public abstract String getPassword();
	public abstract String getHostName();
	public abstract String getServerPassword();
	public abstract void registerPackets();
	public abstract Mode getMode();

	
	public enum Mode{
		SERVER, CLIENT;
	}
}
