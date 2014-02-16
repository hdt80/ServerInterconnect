package net.njay.serverinterconnect.server;

import java.io.IOException;
import java.net.Socket;

import net.njay.serverinterconnect.log.Log;

public class ListenerThread extends Thread{

	@Override
	public void run(){
		try {
            while (true) {
            	Log.debug("Starting ListenerThread");
                Socket sock = Server.serverSocket.accept();
                Log.debug("New connection: " + sock.getInetAddress().toString() + ". Evaluating connection...");
                EvaluationThread t = new EvaluationThread(sock);
                t.start();
                //Server.threads.add(t);
                System.out.println(sock.getInetAddress()+" has joined.");
            }
        } catch (IOException e) {
            System.out.println("Server socket closed.");
        }
	}
	
}
