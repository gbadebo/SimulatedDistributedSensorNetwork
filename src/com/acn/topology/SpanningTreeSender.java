package com.acn.topology;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import com.acn.networking.UDPBroadcaster;
import com.acn.networking.UDPReceiver;
import com.acn.node.Node;

public class SpanningTreeSender {
	
	UDPBroadcaster broadcast;
	
	public SpanningTreeSender(UDPBroadcaster b, ArrayList<Node> nodes) throws UnknownHostException
	{
		broadcast = b;
		for( int i = 0; i < nodes.size(); i++)
		{
			broadcast.addNeighbor(InetAddress.getByName(nodes.get(i).address), nodes.get(i).port);
		}
	}
	
	/**
	 * Send a broadcast message to all neighbors (including nonchildren)
	 * that declares who our children are.
	 * @param children
	 */
	public void sendTree(ArrayList<Node> children)
	{
		//Send tree data as per pseudocode in design doc
		
		
	}
	public void sendTree(String tree)
	{
		//Send tree data as per pseudocode in design doc
		try{
			broadcast.broadcastMessage(UDPReceiver.CONFIG, tree);
		}catch(IOException ex){
			
		}
		
		
	}
}
