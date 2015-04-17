package com.acn.parsing;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import com.acn.node.Node;

public class FileParser {
	
	public static ArrayList<Node> parse(String filename) throws FileNotFoundException
	{
		ArrayList<Node> nodes = new ArrayList<Node>();
		File file = new File(filename);
		Scanner reader = new Scanner(file);
		while( reader.hasNextLine() )
		{
			String line = reader.nextLine();
			Scanner lineReader = new Scanner(line);
			lineReader.useDelimiter(", ");
			Node entry = new Node();
			entry.name = lineReader.next();
			entry.x = Integer.parseInt(lineReader.next());
			entry.y = Integer.parseInt(lineReader.next());
			String temp = lineReader.next();
			String[] addrPort = temp.split("/");
			entry.address = addrPort[0];
			entry.port = Integer.parseInt(addrPort[1]);
			entry.temperature = Integer.parseInt(lineReader.next());
			entry.range = Integer.parseInt(lineReader.next());
			entry.isSink = Integer.parseInt(lineReader.next()) == 1 ? true : false;
			nodes.add(entry);
		}
		return nodes;
	}
	
}
