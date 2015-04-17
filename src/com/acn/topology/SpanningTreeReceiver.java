package com.acn.topology;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.acn.Sensor;
import com.acn.networking.UDPBroadcaster;
import com.acn.networking.UDPReceiver;
import com.acn.node.Node;
import com.acn.temperature.TemperatureSender;

public class SpanningTreeReceiver implements com.acn.networking.UDPReceiverListener{
	/**
	 * Determines whether a received message will be considered.  If the message contains
	 * this node, then we accept the message and consider the sender to be our parent.
	 * The children ArrayList is filled by this method with the children of this node 
	 * if the sender is considered the parent.
	 * This method returns true if this happens.
	 * @param message
	 * @param parent
	 * @param children
	 * @return
	 */
	public Map<String,Integer> myTemp;
	public SpanningTreeReceiver(){
		myTemp = new HashMap<String,Integer>();
	}
	public boolean receiveTree(String message, Node parent, ArrayList<Node> children)
	{
		//Receive tree data as per pseudocode in design doc
		return false;
	}
	
	@Override
	public void messageReceived(byte type, String message, InetAddress addr,
			int port) {
		// System.out.println("I got message "+message+" from "+addr+"/"+port);
		UDPBroadcaster br = new UDPBroadcaster(Sensor.s);
		Node sendingNode = Sensor.helper.getNodeByAddr(addr, port);
		byte recvType = UDPReceiver.REQ;
		try {
			
		if(message.contains("request")){
			
			
			if(message.contains("max")){
				recvType = UDPReceiver.MAX;
			}
			if(message.contains("min")){
				recvType = UDPReceiver.MIN;
			}
			if(message.contains("avg")){
				recvType = UDPReceiver.AVG;
			}
			//if sending node is parent reply if not discard
			TemperatureSender myTempSender = new TemperatureSender(br, Sensor.nodes);
			if(Sensor.parentNode != null){
				
				if(sendingNode.name.equalsIgnoreCase(Sensor.parentNode.name)){
					myTempSender.sendTemp(UDPReceiver.REQ, message);//rebroadcast request temp
					//return;
				}
			}
			 if(Sensor.parentNode == null && Sensor.helper.isSink(Sensor.sensorname) == false){
		    	   System.out.println("parent node null "+ Sensor.sensorname);
		    	   myTempSender.sendTemp(recvType, ""+Sensor.helper.getNodeByName(Sensor.sensorname).temperature);
		    	   System.out.println(" Temp: At node: "+Sensor.sensorname +" : "+Sensor.helper.getNodeByName(Sensor.sensorname).temperature);
					   
		    	 //  return;
		       }
			 else{
			//	 System.out.println("parent node is good  "+Sensor.parentNode.name+" "+ Sensor.sensorname);
		    	 
			 }
			
		
			if(Sensor.parentNode != null && sendingNode.name.equalsIgnoreCase(Sensor.parentNode.name)== true){
			if(Sensor.myChildNodes ==null){
						myTempSender.sendTemp(recvType, ""+Sensor.helper.getNodeByName(Sensor.sensorname).temperature);
						System.out.println("got here 2");
						
				}
				
			}	
		}
		if(type == UDPReceiver.MAX){
			//System.out.println("got here my");
			TemperatureSender myTempSender = new TemperatureSender(br, Sensor.nodes);
			
			if(isChild(Sensor.helper.getNodeByAddr(addr, port).name)){
				myTemp.put(sendingNode.name, Integer.parseInt(message));
			   if(myTemp.size() == Sensor.myChildNodes.size()){
				   if(Sensor.helper.isSink(Sensor.sensorname)){
						System.out.println("Max Temp:"+getData());
						
					}
				   System.out.println("Max Temp: At node: "+Sensor.sensorname +" : "+getData());
				   
				   myTempSender.sendTemp(UDPReceiver.MAX,"" + getData());
			   }
			
			}
			
			
		}
		if(type == UDPReceiver.MIN){
			//System.out.println("got here my");
			TemperatureSender myTempSender = new TemperatureSender(br, Sensor.nodes);
			
			if(isChild(Sensor.helper.getNodeByAddr(addr, port).name)){
				myTemp.put(sendingNode.name, Integer.parseInt(message));
			   if(myTemp.size() == Sensor.myChildNodes.size()){
				   if(Sensor.helper.isSink(Sensor.sensorname)){
						System.out.println("Min Temp:"+getMinData());
						
					}
				   System.out.println("Min Temp: At node: "+Sensor.sensorname +" : "+getMinData());
				   
				   myTempSender.sendTemp(UDPReceiver.MIN,"" + getMinData());
			   }
			
			}
			
			
		}
		if(type == UDPReceiver.AVG){
			//System.out.println("got here my");
			TemperatureSender myTempSender = new TemperatureSender(br, Sensor.nodes);
			
			if(isChild(Sensor.helper.getNodeByAddr(addr, port).name)){
				myTemp.put(sendingNode.name, Integer.parseInt(message));
			   if(myTemp.size() == Sensor.myChildNodes.size()){
				   if(Sensor.helper.isSink(Sensor.sensorname)){
						System.out.println("AVG Temp:"+getAvgData()/20.0);
						return;
					}
				   System.out.println("Sum Temp: At node: "+Sensor.sensorname +" : "+getAvgData());
				   
				   myTempSender.sendTemp(UDPReceiver.AVG,"" + getAvgData());
			   }
			
			}
			
			
		}
		
		if (message.equalsIgnoreCase("get") == true) {
			if (Sensor.parentNode != null)
				System.out.print(" get parent: " + Sensor.parentNode.name
						+ "  current node :" + Sensor.sensorname + "[");

			if (Sensor.myChildNodes != null) {
				for (Node s : Sensor.myChildNodes) {
					System.out.print(s.name + " ");
				}
				System.out.println("]");

			}
			
			if(Sensor.parentNode != null){
				if(sendingNode.name.equalsIgnoreCase(Sensor.parentNode.name) == true){
					System.out.println("message" + message);
					br.broadcastMessage(UDPReceiver.REQ, message);//rebraodcast get
				}
			}
			return;
		}
        
		if (message.contains("(") == false || message.contains(")") == false){
			
			return;
			
		}
		
		
		
		SpanningTreeSender sts;
	
			sts = new SpanningTreeSender(br, Sensor.nodes);
			if (message.contains(Sensor.sensorname) == true) {
				Sensor.myChildNodes = Sensor.helper.stringToChildNodes(Sensor.sensorname.charAt(0), message);
				System.out.println("Childnode[");
				if (Sensor.myChildNodes != null) {
					for (Node s : Sensor.myChildNodes) {
						System.out.println(s.name + " ");
					}
				}
				
			}
		
			// checking if sending node is parent of current node
			if (message.contains(Sensor.sensorname) == true) {
				if(message.contains("(") == false || message.contains(")") == false){
					Sensor.parentNode = sendingNode;
				}
				ArrayList<Node> childNodes = Sensor.helper.stringToChildNodes(
						sendingNode.name.charAt(0), sendingNode.name + "("
								+ message + ")");

				if (childNodes != null) {

					for (Node n : childNodes) {
						if (n.name.equalsIgnoreCase(Sensor.sensorname) == true) {

							Sensor.parentNode = sendingNode;
						
						}
					}
				}

			}

			if (message.contains(Sensor.sensorname) == false) {
				// System.out.println("workerless");
				return;

			}// if i am a child rebroadcast note parent set above
			if (Sensor.parentNode != null) {
				sts.sendTree(Sensor.helper.stringToChildString(Sensor.sensorname.charAt(0), message));

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public boolean isChild(String name){
		if (Sensor.myChildNodes != null) {
			for (Node s : Sensor.myChildNodes) {
				if(s.name.equalsIgnoreCase(name)== true){
					return true;
				}
			}
		}
		return false;
	}
	public int getData(){
		int max = 0;
		for(String key : myTemp.keySet()){
			if(myTemp.get(key) > max){
				max = myTemp.get(key);
			}
		}
		int mytemp = Sensor.helper.getNodeByName(Sensor.sensorname).temperature;
		if(mytemp > max){
			return mytemp;
		}else{
			return max;
		}
	}
	public int getMinData(){
		int min = Integer.MAX_VALUE;
		for(String key : myTemp.keySet()){
			if(myTemp.get(key) < min){
				min = myTemp.get(key);
			}
		}
		int mytemp = Sensor.helper.getNodeByName(Sensor.sensorname).temperature;
		if(mytemp < min){
			return mytemp;
		}else{
			return min;
		}
	}
	public int getAvgData(){
		int sum=0;
		for(String key : myTemp.keySet()){
			
				sum += myTemp.get(key);
		}
		
		int mytemp = Sensor.helper.getNodeByName(Sensor.sensorname).temperature;
		sum += mytemp;
		return sum;
	}
}
