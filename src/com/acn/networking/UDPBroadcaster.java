package com.acn.networking;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

/**
 * Creates a broadcaster object that will send UDP packets to all of its neighbors.
 * Neighbors must be added before the broadcaster will send to them.
 * @author erick
 *
 */
public class UDPBroadcaster {
	//Both the broadcaster and listener could have been the same object, as they both use the same socket.
	//However, it is better to split distinct functionality into separate objects.
	private DatagramSocket socket;
	
	private ArrayList<InetAddress> neighborAddr = new ArrayList<InetAddress>();
	private ArrayList<Integer> neighborPort = new ArrayList<Integer>();
	
	public UDPBroadcaster(DatagramSocket s)
	{
		socket = s;
	}
	
	public void addNeighbor(InetAddress a, int p)
	{
		neighborAddr.add(a);
		neighborPort.add(p);
	}

	public void broadcastMessage(byte type, String message) throws IOException
	{
		message = ""+(char)type+message;
		byte[] mBytes = message.getBytes();
		for(int i = 0; i < neighborAddr.size(); i++)
		{
			DatagramPacket packet = new DatagramPacket(mBytes, mBytes.length, neighborAddr.get(i), neighborPort.get(i));
			socket.send(packet);
		}
	}
}
