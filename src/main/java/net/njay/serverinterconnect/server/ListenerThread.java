package net.njay.serverinterconnect.server;

import java.io.IOException;
import java.net.Socket;

public class ListenerThread extends Thread{

	@Override
	public void run(){
		try {
            while (true) {
                Socket sock = Server.serverSocket.accept();
                ServerThread t = new ServerThread(sock);
                t.start();
                Server.threads.add(t);
                System.out.println(sock.getInetAddress()+" has joined.");
            }
        } catch (IOException e) {
            System.out.println("Server socket closed.");
        }
	}
	
}
