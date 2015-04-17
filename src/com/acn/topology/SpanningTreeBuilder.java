package com.acn.topology;
//author Gbadebo Ayoade
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import com.acn.node.Node;

public class SpanningTreeBuilder {
	
	ArrayList<Node> nodes ;
	
	public SpanningTreeBuilder(ArrayList<Node> arr){
	
		nodes = new ArrayList<Node>(arr);
	} 
	public Node buildTree()
	{
		//Build tree as per pseudocode in design doc
		
		Node sinkNode = findSink();
		
		if (sinkNode == null) return null;
		
		Queue<Node> processing = new LinkedList<Node>();
		nodes.remove(sinkNode);
		
		// System.out.println("parent  "+sinkNode.name);
		 
		if( sinkNode != null){
			
			ArrayList<Node> neighbors = findNeighbours(sinkNode);
			
			
			sinkNode.childNodes = new ArrayList<Node>(neighbors);
			
				
			
			for(Node n:sinkNode.childNodes)
			{   
			//	System.out.println(" "+n.name);
				nodes.remove(n);
				processing.add(n);
			}
		
			 while (processing.isEmpty() == false){
				 Node procNode = processing.remove();
			//	 System.out.println("parent  "+procNode.name);
				 
				 ArrayList<Node> neighbors1 = findNeighbours(procNode);
				 procNode.childNodes = new ArrayList<Node>(neighbors1);
				
				 for(Node n:procNode.childNodes){
				//	 System.out.println(" "+n.name);
						nodes.remove(n);
						processing.add(n);
					}
									 
			 }
				
			
		}
		
		return sinkNode;
		
		
	}
	
	public Node findSink(){
		TopoHelpers helper = new TopoHelpers(nodes);
		for(Node n:nodes)
		{
			if (helper.isSink(n.name) == true){
				return n;
			}
		}
		
		return null;
	}
	
	public ArrayList<Node> findNeighbours(Node node){
		TopoHelpers helper = new TopoHelpers(nodes);
		ArrayList<Node> neighbors = helper.getNeighbors(node);
				
		return neighbors;
	}
	
}
