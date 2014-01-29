package net.njay.serverinterconnect.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.util.ArrayList;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.ShortBufferException;
import net.njay.serverinterconnect.packet.Packet;

public class Server {
    public static ArrayList<ServerThread> threads;
    public static ServerSocket serverSocket = null;

    public static void start(int port) {
        threads = new ArrayList<ServerThread>();
        System.out.println("Starting server...");
        try {
        	serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println("Could not listen on port " + port);
            System.exit(-1);
        }
        System.out.println("Listening on port " + port + ".");
        new ListenerThread().start();
    }

    public static void relay(Packet packet) throws IOException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, ShortBufferException, BadPaddingException {
        for (ServerThread thread : threads)
            thread.send(packet);
    }

    public static void stop() {
        try {
            System.out.println("Terminating Server.");
            serverSocket.close();
            for (ServerThread thread : threads) {
                thread.socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
