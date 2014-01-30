package net.njay.serverinterconnect;

import java.io.File;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.ShortBufferException;

import net.njay.customevents.event.Event;
import net.njay.customevents.event.EventHandler;
import net.njay.customevents.event.Listener;
import net.njay.serverinterconnect.client.Client;
import net.njay.serverinterconnect.event.PacketRecievedEvent;
import net.njay.serverinterconnect.packet.packets.TestPacket;
import net.njay.serverinterconnect.server.Server;
import net.njay.serverinterconnect.xml.XMLBridge;

import org.jdom2.JDOMException;

public class Main implements Listener{

	static boolean client = false;
	
	public static void main(String[] args) throws IOException, JDOMException {
		Event.addListener(new Main());	
		ServerInterconnect.initialize(new File("config.xml"));
		if (ServerInterconnect.getXMLBridge().getMode() == XMLBridge.Mode.CLIENT)
			client();
	}
	
	public static void client(){
		client = true;
		Thread t = new Thread(new Runnable(){
			public void run(){
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					Client.send(new TestPacket());
					System.out.println("Sent!");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		t.start();
	}
	
	@EventHandler
	public void test(PacketRecievedEvent e) throws IOException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, ShortBufferException, BadPaddingException{
		if (!client){
			Server.relay(new TestPacket());
		}
		System.out.println("ID: " + e.getPacket().getId());
	}
	
	

}
