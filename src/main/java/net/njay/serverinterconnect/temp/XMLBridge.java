package net.njay.serverinterconnect.temp;

import java.io.File;
import java.io.IOException;

import net.njay.serverinterconnect.packet.PacketType;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class XMLBridge {

	private File xmlFile;
	private Document document; 
	private Element rootElement;
	
	public XMLBridge(File xmlFile){
		this.xmlFile = xmlFile;
	}
	
	public void init() throws IOException, JDOMException{
		xmlFile.createNewFile();
		document = (Document) new SAXBuilder().build(xmlFile);
		rootElement = document.getRootElement();
		registerPackets();
	}
	
	public String getProtocol(){
		return rootElement.getAttributeValue("proto");
	}
	
	public byte[] getKeyBytes(){
		return rootElement.getChild("SecretKey").getTextNormalize().getBytes();
	}
	
	public byte[] getIVBytes(){
		return rootElement.getChild("IV").getTextNormalize().getBytes();
	}
	
	public int getPort(){
		return Integer.valueOf(rootElement.getChild("server").getChild("port").getTextNormalize());
	}
	
	public String getHostName(){
		return rootElement.getChild("server").getChild("hostname").getTextNormalize();
	}
	
	public void registerPackets(){
		for (Element element : rootElement.getChild("packets").getChildren())
			PacketType.register(Integer.valueOf(element.getAttributeValue("id")), 
					element.getAttributeValue("path"));
	}
	
	public Mode getMode(){
		return Mode.valueOf(rootElement.getChild("Mode").getTextNormalize().toUpperCase());
	}
	
	public enum Mode{
		SERVER, CLIENT;
	}
}



/*
<config proto="0.0.1">
	<Mode> Server </Mode>
	<SecretKey> KEYKEY123 </SecretKey>
	<IV> 12345678 </IV>
	<packets>
		<packet id=1 path=path />
	</packets>
</config>
*/