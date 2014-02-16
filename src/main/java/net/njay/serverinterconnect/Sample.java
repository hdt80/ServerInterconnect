package net.njay.serverinterconnect;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import net.njay.customevents.event.Event;
import net.njay.customevents.event.EventHandler;
import net.njay.customevents.event.Listener;
import net.njay.serverinterconnect.event.PacketRecievedEvent;
import net.njay.serverinterconnect.file.FileHeader;
import net.njay.serverinterconnect.file.FileSender;
import net.njay.serverinterconnect.log.Log;
import net.njay.serverinterconnect.xml.XMLBridge;

import org.jdom2.JDOMException;

public class Sample implements Listener{

	static boolean client = false;
	
	public static void main(String[] args) throws IOException, JDOMException {
		Log.setDebugging(true);
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
					Thread.sleep(2000);
					File send = new File("crc.png");
					FileInputStream fis = new FileInputStream(send);
					byte[] list = new byte[(int) send.length()]; fis.read(list);
					
					FileSender sender = new FileSender(FileHeader.toHeader(send, 1500), list);
					sender.begin(20);
					fis.close();
				
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
