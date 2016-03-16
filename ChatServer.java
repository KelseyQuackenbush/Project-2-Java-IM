import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;


public class ChatServer {

	private static final int PORT = 5621;

	//set of names of all clients

	private static List IPAddresses = Collections.synchronizedList(new ArrayList());

	//set of messages sent on server to send out to all clients

	private static HashSet<PrintWriter> messages = new HashSet<PrintWriter>();

	//main method, listens on port and creates handler threads
	public static void main(String[] args){
		ServerSocket listener = new ServerSocket(PORT);
		try{
			while (true){
				new Handler(listener.accept()).start();		
			}
		}
		finally{
			listener.close();
		}

	}
}
//handler thread class
private static class Handler extends Thread{
	private String IPAddress;
	private Socket socket;
	private BufferedReader input;
	private PrintWriter output;


	public Handler (Socket socket){
		this.socket = socket;
	}

	public void run(){
		try{
			input = new BufferedReader (new 
					InputStreamReader(socket.getInputStream());
			output = new PrintWriter(socket.getOutputStream(), true);
			//Request IP address from clients
			while (true){
				output.println("Input IP address.");
				IPAddress = input.readLine();
				if(IPAddress == null){
					return;
				}
				synchronized (IPAddress){
					if (!IPAddress.contains(IPAddress)){
						IPAddresses.add(IPAddress);
						break;
					}
				}
				//notify reader of correct address
				output.println("Address Accepted");
				messages.add(output);
			}
			while(true){
				String input1 = input.readLine();
				if( input1 == null){
					return;
				}
				for (PrintWriter writer :messages){
					writer.println("Message "+ IPAddress+ ": "+ input1);
				}
			}
		}
		catch (IOException e){
			System.out.println(e);
		}
		finally{
			if (IPAddress != null){
				IPAddresses.remove(IPAddress)s
			}
			if (output != null){
				messages.remove(output);

			}
			try{
				socket.close();
				
			}
			catch (IOException e){
				
			}
		}
	}
}

