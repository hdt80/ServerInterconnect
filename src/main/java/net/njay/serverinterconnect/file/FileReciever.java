package net.njay.serverinterconnect.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import net.njay.customevents.event.EventHandler;
import net.njay.customevents.event.Listener;
import net.njay.serverinterconnect.event.PacketRecievedEvent;
import net.njay.serverinterconnect.log.Log;
import net.njay.serverinterconnect.packet.packets.file.FileFragmentPacket;

public class FileReciever implements Listener{

	private static List<ActiveReciever> activeRecivers = new ArrayList<ActiveReciever>();

	
	public static void addPacket(FileFragmentPacket packet) throws ClassNotFoundException, IOException{
		for (ActiveReciever ar : activeRecivers)
			if (ar.addPacket(packet)) return;
		ActiveReciever ar = new ActiveReciever(packet.getHeader());
		ar.addPacket(packet);
		activeRecivers.add(ar);
	}
	
	@EventHandler
	public void onPacketRecieve(PacketRecievedEvent e) throws ClassNotFoundException, IOException{
		if (!(e.getPacket() instanceof FileFragmentPacket)) return;
		addPacket((FileFragmentPacket) e.getPacket());
	}
	
	static class ActiveReciever{
		private FileHeader header;
		private FilePrecursor precursor;
		private int packetsRecieved = 0;
		
		public ActiveReciever(FileHeader header) throws IOException{
			this.header = header;
			new File(header.getFileName() + "doge").createNewFile();
			precursor = new FilePrecursor(); save();
		}
		
		public boolean addPacket(FileFragmentPacket packet) throws IOException, ClassNotFoundException{
			if (!packet.getHeader().getFileName().equals(header.getFileName())) return false;
			load();	precursor.add(packet); save();
			Log.debug("Adding packet in *.part: (" + packetsRecieved + ", " + packet.getHeader().getIncomingPackets() + ")");
			packetsRecieved++; check();
			return true;
		}
		
		private void save() throws FileNotFoundException, IOException{
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(header.getFileName() + "doge")));
			oos.writeObject(precursor);
			oos.close();
		}
		
		private void load() throws FileNotFoundException, IOException, ClassNotFoundException{
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(header.getFileName() + "doge")));
			precursor = (FilePrecursor) ois.readObject();
			ois.close();
		}
		
		private void finish() throws IOException{
			removeTemp();
			activeRecivers.remove(this);
			File file = new File(header.getFileName() + header.getFileExt());
			file.createNewFile();
			FileOutputStream stream = new FileOutputStream(file);
			stream.write(precursor.getContent());
			stream.close();
		}
		
		private void removeTemp(){
			File f = new File(header.getFileName() + "doge");
			f.delete();
		}
		
		private void check() throws IOException{
			if (packetsRecieved >= header.getIncomingPackets())
				finish();
		}
	}
}
