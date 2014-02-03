package net.njay.serverinterconnect;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import net.njay.customevents.event.Event;
import net.njay.customevents.event.EventHandler;
import net.njay.customevents.event.Listener;
import net.njay.serverinterconnect.client.Client;
import net.njay.serverinterconnect.event.PacketRecievedEvent;
import net.njay.serverinterconnect.packet.packets.TestPacket;
import net.njay.serverinterconnect.xml.XMLBridge;

import org.jdom2.JDOMException;

public class Sample implements Listener{

	static boolean client = false;
	
	public static void main(String[] args) throws IOException, JDOMException {
		Event.addListener(new Sample());	
		ServerInterconnect.initialize(new File("config.xml"));
		if (ServerInterconnect.getXMLBridge().getMode() == XMLBridge.Mode.CLIENT)
			client();
	}
	
	public static void client(){
		client = true;
		Thread t = new Thread(new Runnable(){
			public void run(){
				try {
					//Thread.sleep(2000);
					TestPacket packet = new TestPacket(ServerInterconnect.getXMLBridge().getID());
					ArrayList<String> list = new ArrayList<String>();
					list.add("CLIENT01");
					packet.setRecipients(list);
					Client.send(packet);
				} catch (Exception e) {e.printStackTrace();}
			}
		});
		t.start();
	}
	
	@EventHandler
	public void test(PacketRecievedEvent e) throws Exception{
		System.out.println("Packet Recieved:{ ClassType: " + e.getPacket().getClass().getName() + 
				", ID: " + e.getPacket().getPacketId() + "}");
	}
	
	

}
