package com.acn.topology;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Stack;

import com.acn.node.Node;

public class TopoHelpers {
	
	ArrayList<Node> nodes = new ArrayList<Node>();
	
	public TopoHelpers(ArrayList<Node> arr)
	{
		nodes = arr;
	}
	
	public boolean isSink(String name)
	{
		for(Node entry:nodes)
		{
			if( name.equals(entry.name) && entry.isSink )
				return true;
		}
		return false;
	}
	
	/**
	 * Determines if node with name name2 is a neighbor of node with name name1.
	 * While this is not necessarily a symmetrical relationship (what if name2
	 * has a shorter range than name1?) we must assume it later on because we
	 * build our tree assuming that the neighbor relationship is symmetric.
	 * 
	 * This method will likely return a null pointer exception of one of the nodes
	 * does not exist.
	 * @param name1
	 * @param name2
	 * @return
	 */
	public boolean isNeighbor(String name1, String name2)
	{
		Node node1 = null;
		Node node2 = null;
		for(Node entry:nodes)
		{
			if( name1.equals(entry.name) )
				node1 = entry;
			if( name2.equals(entry.name) )
				node2 = entry;
		}
		double distance = Math.sqrt(Math.pow(node2.x-node1.x, 2)+Math.pow(node2.y-node1.y, 2));
		if( distance <= node1.range )
			return true;
		return false;
	}
	
	/**
	 * Get the list of neighbors for the node with name name
	 * @param name
	 * @return
	 */
	public ArrayList<Node> getNeighbors(String name)
	{
		ArrayList<Node> results = new ArrayList<Node>();
		Node theNode = null;
		for(Node entry:nodes)
		{
			if( name.equals(entry.name) )
			{
				theNode = entry;
			}
		}
		for(Node entry:nodes)
		{
			if( !name.equals(entry.name) )//Make sure you don't consider yourself
			{
				double distance = Math.sqrt(Math.pow(theNode.x-entry.x, 2)+Math.pow(theNode.y-entry.y, 2));
				if( distance <= theNode.range )
					results.add(entry);
			}
		}
		return results;
	}
	public ArrayList<Node> getNeighbors(Node theNode)
	{
		ArrayList<Node> results = new ArrayList<Node>();
		
		for(Node entry:nodes)
		{
			if( !theNode.name.equals(entry.name) )//Make sure you don't consider yourself
			{
				double distance = Math.sqrt(Math.pow(theNode.x-entry.x, 2)+Math.pow(theNode.y-entry.y, 2));
				if( distance <= theNode.range )
					results.add(entry);
			}
		}
		return results;
	}
	
	/**
	 * Find a node by its address and port number.  This is helpful for identifying a sender easily.
	 * This is allowed because if a node receives a message, that means that the sender is a neighbor,
	 * and all nodes are allowed only to get data about their neighbors.
	 * @param addr
	 * @param port
	 * @return
	 */
	public Node getNodeByAddr(InetAddress addr, int port)
	{
		
		for(Node entry:nodes)
		{
			if( entry.address.equalsIgnoreCase(addr.getHostName()) && entry.port == port )
			{
				return entry;
			}
		}
		
		return null;
	}
	public Node getNodeByName(String name)
	{
		for(Node entry:nodes)
		{
			if( entry.name.equals(name) )
			{
				return entry;
			}
		}
		return null;
	}
	public String TraverseTree(Node n) {
		if (n == null)
			return "";
		if (n.childNodes.size() == 0)
			return n.name;
		String temp;
		temp = n.name + "(";
		for (Node myNode : n.childNodes) {
			temp += TraverseTree(myNode);

		}
		temp += ")";

		return temp;

	}

	public ArrayList<Node> stringToChildNodes(char node,
			String spanningTree) {
		
		
		if (spanningTree.length() == 0 || spanningTree.length() == 1)
			return null;
		Stack<Character> processor = new Stack<Character>();
		ArrayList<Node> childNodes = new ArrayList<Node>();
		
		int i = spanningTree.indexOf(node);
        
		i++;
		if (spanningTree.charAt(i) != '(') {
			return null;//             node = 'Q';
			        //           spanningTree = "QR(S)"; //Q should return nothing not S
		}
		int j = 0;

		for (j = i; j < spanningTree.length(); j++) {
			Character c = new Character(spanningTree.charAt(j));

			if (c.charValue() == '(') {
				processor.push(c);
				j++;

			}
			c = spanningTree.charAt(j);
			if (c.charValue() != '(' && c.charValue() != ')'
					&& processor.size() == 1) {
				// System.out.println("  "+spanningTree.charAt(j));
				childNodes.add(getNodeByName(""
						+ spanningTree.charAt(j)));
			}
			if (c.charValue() == ')') {
				if (processor.isEmpty() == true)
					return null;
				processor.pop();
				if (processor.isEmpty() == true)
					break;

			}

			// Process char
		}

		return childNodes;

	}

	public String stringToChildString(char node, String spanningTree) {
		if (spanningTree.length() <= 2 || spanningTree.contains("(") == false
				|| spanningTree.contains("(") == false)
			return "";
		Stack<Character> processor = new Stack<Character>();
		int i = spanningTree.indexOf(node);
		System.out.println("" + spanningTree.charAt(i));
		i++;
		Character c1 = new Character(spanningTree.charAt(i));
		if (c1.charValue() != '(') {

			return "";// for scenerio like FG where G
		}
		int j = 0;

		for (j = i; j < spanningTree.length(); j++) {
			Character c = new Character(spanningTree.charAt(j));

			if (c.charValue() == '(') {
				processor.push(c);
			}
			if (c.charValue() == ')') {

				if (processor.isEmpty() == true) {
					return "";
				}// should not be empty here
				processor.pop();
			}

			if (processor.isEmpty() == true)
				break;

			// Process char
		}

		return spanningTree.substring(i + 1, j);

	}

	
}
