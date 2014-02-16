package net.njay.serverinterconnect.api.packet;

import java.util.ArrayList;

public abstract class Packet {

	public abstract int getPacketId();
	public abstract ArrayList<String> getRecipients();
	public abstract void setRecipients(ArrayList<String> recipients);
	public abstract String getOriginAddress();
	public abstract void setOriginAddress(String address);

}