package net.njay.serverinterconnect.file;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.njay.serverinterconnect.packet.packets.file.FileFragmentPacket;

public class FilePrecursor implements Serializable{

	private static final long serialVersionUID = -5444772361593110436L;
	
	private List<FileFragmentPacket> packets = new ArrayList<FileFragmentPacket>();

	public void add(FileFragmentPacket packet){
		this.packets.add(packet);
	}
	
	public byte[] getContent(){
		int currentIndex = 0, totalSize = 0;
		for (FileFragmentPacket packet : packets)
			totalSize += packet.getContent().length;
		byte[] list = new byte[totalSize];
		for (FileFragmentPacket packet : packets){
			for (int i = 0; i < packet.getContent().length; i++)
				list[currentIndex++] = packet.getContent()[i];
		}
		return list;
	}
}
