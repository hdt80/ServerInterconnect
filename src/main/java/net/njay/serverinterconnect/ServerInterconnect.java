package net.njay.serverinterconnect;

import java.io.File;
import java.util.logging.Level;

import net.njay.customevents.event.Event;
import net.njay.serverinterconnect.client.Client;
import net.njay.serverinterconnect.file.FileReciever;
import net.njay.serverinterconnect.log.Log;
import net.njay.serverinterconnect.packet.PacketType;
import net.njay.serverinterconnect.server.Server;
import net.njay.serverinterconnect.upnp.UPNP;
import net.njay.serverinterconnect.xml.ConnectionConfig;
import net.njay.serverinterconnect.xml.XMLBridge;

public class ServerInterconnect {

	private static ConnectionConfig config;
	
	public static void initialize(ConnectionConfig conf, boolean defaultConnect){
		registerCorePackets();
		Event.addListener(new FileReciever());
		config = conf;
		try {
			config.init();
		} catch (Exception e) {
			System.err.println("There was a problem with your xml file!");
			e.printStackTrace(); System.exit(-1);
		}
		try {
			if (UPNP.forward(config.getPort(), config.getPort()+1) != config.getPort())
				Log.log(Level.SEVERE, "Failed to portforward!");
		} catch (Exception e1) { Log.log(e1); }
		if (!defaultConnect) return;
		if (config.getMode() == XMLBridge.Mode.CLIENT)
			Client.connect(config.getHostName(), config.getPort());
		else
			Server.start(config.getPort());
	}
	
	private static void registerCorePackets(){
		PacketType.register(2, "net.njay.serverinterconnect.packet.packets.auth.RequestAuthenticationPacket");
		PacketType.register(3, "net.njay.serverinterconnect.packet.packets.auth.AuthenticationPacket");
		PacketType.register(4, "net.njay.serverinterconnect.packet.packets.file.FileFragmentPacket");
	}
	
	public static ConnectionConfig getConfig(){
		return config;
	}
}
