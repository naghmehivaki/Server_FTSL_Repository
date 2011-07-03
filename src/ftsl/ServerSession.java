package ftsl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import util.Logger;

public class ServerSession {
	
	ServerSocket serverSocket;
	boolean isTransactional = false;
	int timeout=-1;

	
	public ServerSession(int port, int flag, String address, boolean t, int time) {
		try {
			isTransactional=t;
			timeout=time;
			
			serverSocket = new ServerSocket(port, flag, InetAddress.getByName(address));
			//Logger.log("Server Session created the new ServerSocket "+serverSocket.toString());

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public ServerSession(int port, int flag, String address) {
		try {
			
			serverSocket = new ServerSocket(port, flag, InetAddress.getByName(address));
			//Logger.log("Server Session created the new ServerSocket "+serverSocket.toString());

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public ServerSession(int port, int flag, String address, boolean t) {
		try {
			isTransactional=t;
			serverSocket = new ServerSocket(port, flag, InetAddress.getByName(address));
			//Logger.log("Server Session created the new ServerSocket "+serverSocket.toString());

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public ServerSession(int port, int flag, String address, int time) {
		try {
			timeout=time;
			serverSocket = new ServerSocket(port, flag, InetAddress.getByName(address));
			//Logger.log("Server Session created the new ServerSocket "+serverSocket.toString());

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	////////////////////////////////////////
	public Session accept(){
		Session session=null;
		try {
			Socket socket = serverSocket.accept();
			System.out.println(System.currentTimeMillis());

			//Logger.log("Server Session accepted the new socket request "+ socket+" from the client.");
			session= new Session(socket,isTransactional,timeout);
			//Logger.log("Server session created a new session with the client");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return session;
	}

}
