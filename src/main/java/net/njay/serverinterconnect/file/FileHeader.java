package net.njay.serverinterconnect.file;

import java.io.File;
import java.io.Serializable;

public class FileHeader implements Serializable{

	private static final long serialVersionUID = 1495765273076063651L;
	
	private String fileName, fileExt;
	private int fileSize, packetSize, incomingPackets;

	public FileHeader(String fileName, String fileExt, int fileSize, int packetSize, int incomingPackets){
		this.fileName = fileName;
		this.fileExt = fileExt;
		this.fileSize = fileSize;
		this.packetSize = packetSize;
		this.incomingPackets = incomingPackets;
	}
	
	public String getFileName(){ return this.fileName; }
	public String getFileExt(){ return this.fileExt; }
	public int getFileSize(){ return this.fileSize; }
	public int getPacketSize(){ return this.packetSize; }
	public int getIncomingPackets(){ return this.incomingPackets; }

	public static FileHeader toHeader(File file, int packetSize){
		int i = file.getName().lastIndexOf('.');
		String extension = file.getName().substring(i+1);
		System.out.println((int) Math.ceil((double)file.length()/((double)packetSize)));
		return new FileHeader(file.getName().replaceAll(extension, ""), extension, (int) file.length(), 
				packetSize, (int) Math.ceil((double)file.length()/((double)packetSize)));
	}
}
