package net.njay.serverinterconnect;

import java.io.File;
import net.njay.serverinterconnect.client.Client;
import net.njay.serverinterconnect.packet.PacketType;
import net.njay.serverinterconnect.server.Server;
import net.njay.serverinterconnect.xml.XMLBridge;

public class ServerInterconnect {

	private static XMLBridge xmlBridge;
	
	public static void initialize(File xml){
		registerCorePackets();
		xmlBridge = new XMLBridge(xml);
		try {
			xmlBridge.init();
		} catch (Exception e) {
			System.err.println("There was a problem with your xml file!");
			e.printStackTrace(); System.exit(-1);
		}
		if (xmlBridge.getMode() == XMLBridge.Mode.CLIENT)
			Client.connect(xmlBridge.getHostName(), xmlBridge.getPort());
		else
			Server.start(xmlBridge.getPort());
	}
	
	private static void registerCorePackets(){
		PacketType.register(2, "net.njay.serverinterconnect.packet.packets.RequestAuthenticationPacket");
		PacketType.register(3, "net.njay.serverinterconnect.packet.packets.AuthenticationPacket");
	}
	
	public static XMLBridge getXMLBridge(){
		return xmlBridge;
	}
}
