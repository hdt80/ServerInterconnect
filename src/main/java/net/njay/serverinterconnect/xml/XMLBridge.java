package net.njay.serverinterconnect.xml;

import java.io.File;
import java.io.IOException;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class XMLBridge extends ConnectionConfig{

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
	}
	
	public String getProtocol(){
		return rootElement.getAttributeValue("proto");
	}
	
	public int getPort(){
		return Integer.valueOf(rootElement.getChild("server").getChild("port").getTextNormalize());
	}
	
	public String getUsername(){
		return rootElement.getChild("auth").getChild("username").getTextNormalize();
	}
	
	public String getHostName(){
		return rootElement.getChild("server").getChild("hostname").getTextNormalize();
	}
	
	public String getServerPassword(){
		return rootElement.getChild("server").getChild("password").getTextNormalize();
	}
	
	public String getPassword(){
		return rootElement.getChild("auth").getChild("password").getTextNormalize();
	}
	
	public Mode getMode(){
		return Mode.valueOf(rootElement.getChild("Mode").getTextNormalize().toUpperCase());
	}
}