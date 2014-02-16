package net.njay.serverinterconnect.packet.packets.file;

import net.njay.serverinterconnect.file.FileHeader;
import net.njay.serverinterconnect.packet.SerializablePacket;

public class FileFragmentPacket extends SerializablePacket{
	
	private static final long serialVersionUID = 8007074826835946481L;
	
	private int id;
	private FileHeader header;
	private byte[] content;
	
	public FileFragmentPacket(int id, FileHeader header, byte... content){
		this.id = id;
		this.header = header;
		this.content = content;
	}

	public int getPlacementId(){ return this.id; }
	public FileHeader getHeader(){ return this.header; }
	public byte[] getContent(){ return this.content; };
	
	public int getPacketId(){
		return 4;
	}
}
