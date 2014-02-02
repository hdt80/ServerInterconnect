package net.njay.serverinterconnect.server;

public class ClientConnection {

	private ServerThread thread;
	private String id;
	
	public ClientConnection(ServerThread thread, String id){
		this.thread = thread;
		this.thread.start();
		this.id = id;
	}
	
	public ServerThread getThread(){ return this.thread; }
	public String getId(){ return this.id; }

}
