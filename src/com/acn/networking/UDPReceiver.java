package com.acn.networking;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * Allows for the creation of a background daemon to listen for incoming UDP packets and to alert a
 * single registered listener of any incoming packets.
 * 
 * @author erick
 *
 */
public class UDPReceiver {
	
	//Begin the identifiers for the different kinds of messages
	public static final byte CONFIG = 0;
	public static final byte AVG = 1;
	public static final byte MIN = 2;
	public static final byte MAX = 3;
	public static final byte KILL = 4;
	public static final byte  REQ= 5;
	
	private DatagramSocket socket;
	
	private UDPReceiverListener listener;
	
	private boolean receive = true;
	
	public UDPReceiver(DatagramSocket s)
	{
		socket = s;
	}
	
	/**
	 * Register a single listener to this UDPReceiver.
	 * If the daemon is started, this listener will be alerted when a
	 * UDP message is received.
	 * @param l
	 */
	public void registerListener(UDPReceiverListener l)
	{
		listener = l;
	}
	
	/**
	 * There is a limit of 2048 bytes per packet
	 * because it seems that a hard buffer size must be predefined before the datagram is received.
	 * However, that probably will be an acceptable size.
	 *
	 */
	private class UDPReceiverDaemon extends Thread
	{	
		@Override
		public void run()
		{
			byte[] data = new byte[2048];
			while(receive)
			{
				DatagramPacket packet = new DatagramPacket(data, 2048);
				try {
					socket.receive(packet);
					String message = "";
					byte[] rData = packet.getData();
					byte mType = rData[0];
					for(int i = 1; i < packet.getLength(); i++ )
					{
						message += (char)rData[i];
					}
					
					//TODO: Potentially remove this; this is a kill switch for the network of nodes
					if( mType == KILL )
						System.exit(0);
					
					if( listener != null )
						listener.messageReceived(mType, message, packet.getAddress(), packet.getPort());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.err.println("Error in UDP Receiver Daemon socket.receive().");
				}
			}
		}
	}
	
	/**
	 * Starts a background thread to listen for received packets.  Any packets received will be passed to
	 * the listener that registers with this UDPReceiver.
	 */
	public void startDaemon()
	{
		UDPReceiverDaemon receiverDaemon = new UDPReceiverDaemon();
		receiverDaemon.start();
	}
	
	/**
	 * This will stop the daemon's execution after the next receive is completed;
	 * it is not guaranteed to kill the daemon if it never receives another UDP message, as
	 * it will continue to be blocked on the receive message.
	 */
	public void stopDaemon()
	{
		receive = false;
	}
}
