package com.acn;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

import com.acn.networking.UDPBroadcaster;
import com.acn.networking.UDPReceiver;
import com.acn.node.Node;
import com.acn.parsing.FileParser;
import com.acn.topology.SpanningTreeBuilder;
import com.acn.topology.SpanningTreeReceiver;
import com.acn.topology.SpanningTreeSender;
import com.acn.topology.TopoHelpers;

public class Sensor  {
	public static String sensorname = "";
	public static DatagramSocket s;
	public static ArrayList<Node> nodes;
	public static ArrayList<Node> myChildNodes;
	public static Node parentNode;
	public static TopoHelpers helper;
	public static void main(String[] args) throws IOException {
		if (args.length == 3) {
			System.out.println("Killer process");
		}

		if (args.length < 2) {
			System.out
					.println("Not enough arguments.  Correct usage is: java -jar node.jar <name> <topology file>.");
		} else {
			String name = args[0];
			String file = args[1];

			// String name = "A";
			// String file = "/home/gbaduz/Topology(2).txt";
			sensorname = name;
			nodes = FileParser.parse(file);
			System.out.println("I am " + name + ".");
			System.out.println("There are " + nodes.size()
					+ " nodes in this network.");
			helper = new TopoHelpers(nodes);
			// System.out.println("Am I sink: "+helper.isSink(name));
			// System.out.print(name+"'s neighbors: [");
			ArrayList<Node> neighbors = helper.getNeighbors(name);
			for (Node n : neighbors) {
				// System.out.print(n.name+", ");
			}
			// System.out.println("]");

			int socket = helper.getNodeByName(name).port;
			System.out.println("My socket: " + socket);
			s = new DatagramSocket(socket);

			UDPReceiver r = new UDPReceiver(s);

			SpanningTreeReceiver n = new SpanningTreeReceiver();
			r.registerListener(n);
			r.startDaemon();

			if (helper.isSink(name) == true) {

				Node sinkTree = new SpanningTreeBuilder(nodes).buildTree();
				if (sinkTree == null) {

					System.out.println("Not Sink\n\n");

				}
				String spanningTree = helper.TraverseTree(sinkTree);
				System.out.println(spanningTree);

				UDPBroadcaster br = new UDPBroadcaster(s);

				SpanningTreeSender sts = new SpanningTreeSender(br, nodes);
				
				sts.sendTree(helper.stringToChildString(name.charAt(0), spanningTree));
				myChildNodes = helper.stringToChildNodes(sensorname.charAt(0),
						spanningTree);

				// sts.sendTree(spanningTree);
				System.out.println("Enter command here");
				System.out.println("requestmax for max, requestmin for min, requestavg for avg.");
				Scanner uInput = new Scanner(System.in);

				while (true) {
					br.broadcastMessage(UDPReceiver.REQ, uInput.nextLine());
				}
				// uInput.close();
				//
			}

		}

	}

	
	
}
