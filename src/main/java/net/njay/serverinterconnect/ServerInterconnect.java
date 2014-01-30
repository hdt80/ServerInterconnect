package net.njay.serverinterconnect;

import java.io.File;
import net.njay.serverinterconnect.client.Client;
import net.njay.serverinterconnect.server.Server;
import net.njay.serverinterconnect.xml.XMLBridge;

public class ServerInterconnect {

	private static XMLBridge xmlBridge;
	
	public static void initialize(File xml){
		xmlBridge = new XMLBridge(xml);
		try {
			xmlBridge.init();
		} catch (Exception e) {
			System.out.println("There was a problem with your xml file!");
			e.printStackTrace();
			System.exit(-1);
		}
		if (xmlBridge.getMode() == XMLBridge.Mode.CLIENT)
			Client.connect(xmlBridge.getHostName(), xmlBridge.getPort());
		else
			Server.start(xmlBridge.getPort());
	}
	
	public static XMLBridge getXMLBridge(){
		return xmlBridge;
	}
}
