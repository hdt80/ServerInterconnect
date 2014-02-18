package net.njay.serverinterconnect;

import java.io.File;
import java.io.IOException;
import net.njay.customevents.event.Event;
import net.njay.customevents.event.EventHandler;
import net.njay.customevents.event.Listener;
import net.njay.serverinterconnect.event.PacketRecievedEvent;
import net.njay.serverinterconnect.file.FileSender;
import net.njay.serverinterconnect.log.Log;
import net.njay.serverinterconnect.xml.XMLBridge;

import org.jdom2.JDOMException;

public class Sample implements Listener{

	static boolean client = false;
	
	public static void main(String[] args) throws IOException, JDOMException {
		Log.setDebugging(true);
		Event.addListener(new Sample());
		XMLBridge xml = new XMLBridge(new File("config.xml"));
		ServerInterconnect.initialize(xml, true);
		if (ServerInterconnect.getConfig().getMode() == XMLBridge.Mode.CLIENT)
			client();
	}
	
	public static void client(){
		client = true;
		Thread t = new Thread(new Runnable(){
			public void run(){
				try {
					Thread.sleep(2000);
					FileSender sender = new FileSender(new File("test.txt"));
					sender.begin(20);
				} catch (Exception e) {e.printStackTrace();}
			}
		});
		t.start();
	}
	
	@EventHandler
	public void test(PacketRecievedEvent e) throws Exception{
		Log.debug("Packet Recieved:{ ClassType: " + e.getPacket().getClass().getName() + 
				"}");
	}
}
