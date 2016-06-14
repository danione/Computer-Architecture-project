package org.elsys.echo;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class ClientHandler implements Runnable {

	private Socket client;
	private EchoServer server;
	private Map<String, String> myMap = new HashMap();

	public ClientHandler(Socket socket, EchoServer server) {
		this.client = socket;
		this.server = server;
	}
	
	@Override
	public void run() {
		try (
			Scanner inScanner = new Scanner(client.getInputStream());
			PrintWriter outWriter = 
					new PrintWriter(client.getOutputStream(), true);
		) {
			
			System.out.println("New client accepted: " + client);
			String input;
			while((input = inScanner.nextLine()) != null) {
				
				if(!contKey(client.toString()))
				{
					setUpAccount(input, client.toString());
					continue;
				}
				else
				{
					if(checkFor(input,"LOGIN"))
					{
						outWriter.println("SERVER : ERROR_ALREADY_REGISTERED");
						continue;
					}
					else if(checkFor(input, "BRDCAST"))
					{
						outWriter.println("SERVER : OK BRDCAST");
						sendToAll(input, client.toString());
						continue;
					}
					else if(checkFor(input, "LIST"))
					{
						listAll();
						continue;
					}
					else
					{
						outWriter.println("UNKNOWN COMMAND " + input);
						continue;
					}
					
				}
			}
		} catch (IOException e) {
			System.err.println("Not able to accept requests.");
			System.exit(-1);
		} finally {
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void listAll() throws IOException {
		PrintWriter outWriter = 
				new PrintWriter(client.getOutputStream(), true);
		Set<String> key = myMap.keySet();
		outWriter.print("LIST ");
		 
		for (Iterator<String> i = key.iterator(); i.hasNext();) 
		{
			String k = (String) i.next();
		    String value = (String) myMap.get(k);
			outWriter.print(value + ",");
				       
				       
		}
		outWriter.println();
		
	}

	private boolean contVal(String string) {
		return myMap.containsValue(string);
	}
	
	private boolean contKey(String string)
	{
		return myMap.containsKey(string);
	}
	
	private void setUpAccount(String input, String val) throws IOException
	{
		PrintWriter outWriter = 
				new PrintWriter(client.getOutputStream(), true);
		if(checkFor(input,"LOGIN"))
		{
			String [] get = input.split(" ",2);
			if(!contVal(get[1]))
			{
				myMap.put(val,get[1]);
				outWriter.println("SERVER : OK");
			}
			else
			{
				outWriter.println("ERROR_NOT_UNIQUE");
			}
		}
		else
		{
			outWriter.println("SERVER : ERROR_NOT_REGISTERED");
		}
	}
	
	private boolean checkFor(String string, String string1)
	{
		String [] get = string.split(" ",2);
		if(get[0].equals(string1))
			return true;
		else
			return false;
	}
	
	private void sendToAll(String string, String name) throws IOException
	{
		PrintStream outWriter = 
				new PrintStream(client.getOutputStream(), true);
		outWriter.println("FUCK");
	}
}
