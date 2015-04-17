package com.acn.networking;

import java.net.InetAddress;

public interface UDPReceiverListener {
	public void messageReceived(byte type, String message, InetAddress addr, int port);
}
