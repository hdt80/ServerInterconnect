package net.njay.serverinterconnect.file;

import net.njay.serverinterconnect.log.Log;
import net.njay.serverinterconnect.packet.PacketStream;
import net.njay.serverinterconnect.packet.packets.file.FileFragmentPacket;


public class FileSender {

	private FileHeader header;
	private byte[] content;
	
	public FileSender(FileHeader header, byte... content){
		this.header = header;
		this.content = content;
	}
	
	public void begin(long rate){
		FileSenderThread fsr = new FileSenderThread(rate);
		fsr.start();
	}
	
	class FileSenderThread extends Thread{
		int currentContentIndex = 0, currentPacketId = 0;
		long delay = 0;
		
		public FileSenderThread(long delay){
			this.delay = delay;
		}
		
		public void run() {
			byte[] piece = new byte[header.getPacketSize() > content.length ? content.length : header.getPacketSize()];
			for (int j = 0; currentContentIndex < content.length && j < header.getPacketSize(); currentContentIndex++, j++)
				piece[j] = content[currentContentIndex];			
			Log.debug(currentContentIndex + "  " + content.length);
			PacketStream.safeWrite(new FileFragmentPacket(currentPacketId++, header, piece));
			delay();
			if (!check()) run();
		}		
		
		@SuppressWarnings("deprecation")
		private boolean check(){
			if (currentContentIndex >= content.length){
				stop();
				return true;
			}
			return false;
		}
		
		private void delay(){
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {}
		}
	}
	
}
