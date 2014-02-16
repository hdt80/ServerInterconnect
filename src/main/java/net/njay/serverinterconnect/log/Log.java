package net.njay.serverinterconnect.log;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Log {

	private static Logger log;

	private static boolean debug = false;

	static {
		Log.log = Logger.getLogger("Log");
	}

	public static void log(Level lvl, String msg) {
		log.log(lvl, msg);
	}

	public static void log(String msg) {
		log(Level.INFO, msg);
	}

	public static void log(Exception e) {
		e.printStackTrace();
	}

	public static void debug(String msg) {
		if (debug) System.out.println("[DEBUG] " + msg);
	}

	public static void setDebugging(boolean debug) {
		Log.debug = debug;
		if (debug) System.out.println("Debugging on.");
	}

}
