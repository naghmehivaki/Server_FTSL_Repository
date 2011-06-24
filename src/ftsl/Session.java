package ftsl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.TimerTask;
import java.util.Vector;
import java.util.Timer;
import util.Logger;

public class Session extends Thread {

	int DEFAULT_VALUE = 1000;
	static final int LOGGING_PERIOD = 10;
	int sleepTime = DEFAULT_VALUE;
	int MAX_BUFFER_SIZE = 1000;
	FTSL_Logger logger;
	Timer timer = new Timer();
	
	/////////////////////////////// Session Basic Info
	ServerSocket serverSocket;
	Socket socket;
	ObjectInputStream inputStream = null;
	ObjectOutputStream outputStream = null;
	String sessionID = "";
	
	////////////////////////////// Packets Info
	int lastSentPacketID = 0;
	int lastRecievedPacketID = 0;
	Vector<FTSLMessage> sentBuffer = new Vector<FTSLMessage>();
	HashMap<Integer, String> receivedBuffer = new HashMap<Integer, String>();
	
	/////////////////////////////// Messages Info
	int sendMessageID = 0;
	Vector<MessageInfo> SentMessagesInfo = new Vector<MessageInfo>();
	
	/* ***************************** Constructor*/
	public Session() {

	}

	public Session(String dest, int port) {
		try {

			sessionID = String.valueOf(System.currentTimeMillis())
					+ String.valueOf(Math.random());
			// Logger.log("Session Id " + sessionID+
			// " is assigned to the session.");

			socket = new Socket(dest, port);
			// Logger.log("Client session created new socket " +
			// socket.toString()+ " to server " + dest);

			outputStream = new ObjectOutputStream(socket.getOutputStream());
			outputStream.flush();
			inputStream = new ObjectInputStream(socket.getInputStream());

			// Logger.log("Client session is created input and output streams.");

			logger = new FTSL_Logger(sessionID);

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		logger.logSession(this);
		timer.scheduleAtFixedRate(new logTask(this, logger),
				LOGGING_PERIOD * 1000, LOGGING_PERIOD * 1000);

	}

	public Session(Socket s) {
		try {

			socket = s;
			outputStream = new ObjectOutputStream(socket.getOutputStream());
			outputStream.flush();
			inputStream = new ObjectInputStream(socket.getInputStream());
			
			String sid=read();
			
			if (sid != "") {
				sessionID=sid;
				System.out.println(System.currentTimeMillis());

				logger = new FTSL_Logger(sessionID);
				logger.logSession(this);
			} 
			System.out.println(System.currentTimeMillis());
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		timer.scheduleAtFixedRate(new logTask(this, logger),
				LOGGING_PERIOD * 1000, LOGGING_PERIOD * 1000);

	}

	/////////////////////// setters and getters

	public Socket getSocket() {
		return socket;
	}

	public HashMap<Integer, String> getReceivedBuffer() {
		return receivedBuffer;
	}

	public void setReceivedBuffer(HashMap<Integer, String> receivedBuffer) {
		this.receivedBuffer = receivedBuffer;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
		try {
			outputStream = new ObjectOutputStream(socket.getOutputStream());
			inputStream = new ObjectInputStream(socket.getInputStream());

		} catch (IOException e) {
			e.printStackTrace();
		}

		logger.logSessionInfo("Socket", socket);
	}

	public void setSendMessageID(int sendMessageID) {
		this.sendMessageID = sendMessageID;
	}

	public String getSessionID() {
		return sessionID;
	}

	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}

	public int getLastSentPacketID() {
		return lastSentPacketID;
	}

	public void setLastSentPacketID(int id) {
		this.lastSentPacketID = id;
	}

	public int getLastRecievedPacketID() {
		return lastRecievedPacketID;
	}

	public void setLastRecievedPacket(int id) {
		lastRecievedPacketID = id;
	}

	public Vector<FTSLMessage> getSentBuffer() {
		return sentBuffer;
	}

	public void setSentBuffer(Vector<FTSLMessage> sentBuffer) {
		this.sentBuffer = sentBuffer;
	}

	public int getSendMessageID() {
		return sendMessageID;
	}

	public void addSentMessage(FTSLMessage packet) {
		sentBuffer.add(packet);
	}

	public void setLastSentMessageID(int id) {
		this.sendMessageID = id;
	}

	public Vector<MessageInfo> getSentMessagesInfo() {
		return SentMessagesInfo;
	}

	public void setSentMessagesInfo(Vector<MessageInfo> sentMessagesInfo) {
		SentMessagesInfo = sentMessagesInfo;
	}

	public void setLastRecievedPacketID(int id) {
		lastRecievedPacketID = id;
	}
	
	///////////////////////////////////////

	public void updateSocket(Socket s) {
		this.socket = s;
		logger.logSessionInfo("Socket", socket);
		try {
			inputStream = new ObjectInputStream(s.getInputStream());
			outputStream = new ObjectOutputStream(s.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/* ******************************* Operations*/

	public int increaseLastSentPacketID() {
		lastSentPacketID++;
		logger.logSessionInfo("LastSentPacketID", lastSentPacketID);
		return lastSentPacketID;
	}

	public int increaseSendMessageID() {
		sendMessageID++;
		logger.logSessionInfo("SendMessageID", sendMessageID);
		return sendMessageID;
	}
	public void increaseLastRecievedPacketID() {
		lastRecievedPacketID++;
		logger.logSessionInfo("lastRecievedPacketID", lastRecievedPacketID);
	}
	
	////////////////////////////////////////
	

	public void keepSentPacket(int id, FTSLMessage packet) {
		sentBuffer.add(packet);
		logger.logSentMessage(packet);
	}
	public void addreceivedMessage(int id, String str) {
		receivedBuffer.put(id, str);
	}
	////////////////////////////////////////

	public void sendFTSLRequest() {
		FTSLHeader header = new FTSLHeader(sessionID, "FTSL_REQUEST", 0,
				lastRecievedPacketID, 0);

		FTSLMessage packet = new FTSLMessage(null, header);
		byte[] buffer = packet.toByte_();

		try {

			outputStream.write(buffer, 0, buffer.length);
			outputStream.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void sendFTSLReply() {
		FTSLHeader header = new FTSLHeader(sessionID, "FTSL_REPLY", 0,
				lastRecievedPacketID, 0);

		FTSLMessage packet = new FTSLMessage(null, header);
		byte[] buffer = packet.toByte_();

		try {

			outputStream.write(buffer, 0, buffer.length);
			outputStream.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	////////////////////////////////////////

	public void addMessageInfo() {

		if (!SentMessagesInfo.isEmpty()) {
			SentMessagesInfo.lastElement().setEnd(lastSentPacketID - 1);
			logger.logMessageInfo(SentMessagesInfo.lastElement());
		}
		MessageInfo info = new MessageInfo();
		info.setStart(lastSentPacketID);
		info.setIndex(lastSentPacketID);
		info.setId(sendMessageID);
		SentMessagesInfo.add(info);
	}

	public void addMessageInfo(MessageInfo info) {
		SentMessagesInfo.add(info);
	}
	
	public void updateMessageInfo(int pid) {
		SentMessagesInfo.lastElement().setIndex(pid);
	}
	
	////////////////////////////////////////
	
	public synchronized void removeDeliveredMessages(int rpid) {

		int index = 0;
		int id = rpid;
		while (index < SentMessagesInfo.size()) {
			MessageInfo info = SentMessagesInfo.get(index);
			if (info.getEnd() != 0 & info.getEnd() <= rpid) {
				id = info.getEnd();
				SentMessagesInfo.remove(index);
			} else
				index = SentMessagesInfo.size();
		}
		index = 0;
		while (index < sentBuffer.size()) {
			FTSLMessage message = sentBuffer.get(index);
			if (message.getHeader().getPID() <= rpid) {
				sentBuffer.remove(index);

			} else
				index = sentBuffer.size();
		}

	}
	///////////////////////////////////////

	public void write(byte[] buffer) {

		buffer = processOutputPacket(buffer);

		try {
			outputStream.write(buffer);
			//outputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public byte[] processOutputPacket(byte[] packet) {

		// Logger.log("FTSL is writing in the output stream in the server side of the proxy.");
		increaseLastSentPacketID();

		FTSLHeader header = new FTSLHeader(sessionID,
				"APP", lastSentPacketID,
				 lastRecievedPacketID, packet.length);

		// Logger.log("the header of the packet is: " + header.toString_());
		FTSLMessage pkt = new FTSLMessage(packet, header);
		byte[] buffer = pkt.toByte_();

		keepSentPacket(lastSentPacketID, pkt);
		return buffer;
	}
		
	public void flush() {  // the end of a stream of the message
		try {
			outputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		addMessageInfo();
		sendMessageID++;
	}

	
	///////////////////////////////////////

	public String read() {
		byte[] packet = new byte[1024];
		int read = 0;
		try {
			read = inputStream.read(packet);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		while (read == -1) {
			try {
				read = inputStream.read(packet);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// Logger.log("Server read the session ID from the client side:\n"+ new
		// String(packet));
		FTSLMessage message = FTSLMessage.valueOf_(packet);
		String flag= message.getHeader().getFLAG();
		if (flag.compareTo("NTF")==0) {
			
			System.out.println(System.currentTimeMillis());
			logger = new FTSL_Logger();

			Session tempSession = logger.initSession(message.getHeader().getSID());
			sessionID = tempSession.getSessionID();
			lastSentPacketID = tempSession.getLastSentPacketID();
			lastRecievedPacketID = tempSession.getLastRecievedPacketID();
			sentBuffer = tempSession.getSentBuffer();
			receivedBuffer = tempSession.getReceivedBuffer();
			sendMessageID = tempSession.getSendMessageID();
			SentMessagesInfo = tempSession.getSentMessagesInfo();
		
			System.out.println(System.currentTimeMillis());

			return "";
		}
		return message.getHeader().getSID();

	}
	public int read(byte buffer[], int pos, int len) {

		int expectedID = lastRecievedPacketID + 1;
		if (receivedBuffer.containsKey(expectedID)) {
			byte[] tempBuffer = receivedBuffer.get(expectedID).getBytes();
			increaseLastRecievedPacketID();
			for (int i = 0; i < tempBuffer.length; i++)
				buffer[pos + i] = tempBuffer[i];

			return tempBuffer.length;

		} else {
			int read = -1;

			byte[] packet = new byte[3096];

			try {
				read = inputStream.read(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}

			int test = 1;
			while (read != -1 & test == 1) {

				int pSize = processInputPacket(packet, read);

				if (pSize == 0) {
					try {
						read = inputStream.read(packet);
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					test = 0;
					read = pSize;
					for (int i = 0; i < read; i++)
						buffer[pos + i] = packet[i];
				}
			}

			return read;
		}
	}

	public int processInputPacket(byte buffer[], int read) {

		String packet = new String(buffer);
		if (!packet.startsWith(FTSLHeader.protocol))
			return 0;

		int index = packet.indexOf("\n");
		if (index == -1) {
			try {
				int k = inputStream.read(buffer, read, 1024);
				packet = new String(buffer);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		index = packet.indexOf("\n");
		String h = packet.substring(0, index);
		String b = packet.substring(index + 1);
		FTSLHeader header = FTSLHeader.valueOf_(h);
		byte[] body = new byte[header.getMessageSize()];
		byte[] tempBody = b.getBytes();
		for (int i = 0; i < header.getMessageSize(); i++)
			body[i] = tempBody[i];

		int result = processFTSLHeader(header, body);
		byte[] tempBuffer = b.getBytes();
		if (tempBuffer == null || tempBuffer.length == 0)
			return 0;

		if (result == 0) {
			return 0;

		} else {
			for (int i = 0; i < tempBuffer.length; i++)
				buffer[i] = tempBuffer[i];
		}
		return header.getMessageSize();
	}

	public int processFTSLHeader(FTSLHeader header, byte[] b) {

		String flag = header.getFLAG();
		String sid = header.getSID();
		int pid = header.getPID();
		int rpid = header.getrPID();

		if (flag.compareTo("APP") == 0) {

			

			int expectedPID = lastRecievedPacketID + 1;

			if (pid == expectedPID) {
				increaseLastRecievedPacketID();
				removeDeliveredMessages(rpid);
				return 1;

			} else {

				receivedBuffer.put(pid, new String(b));
				logger.logReceivedMessage(pid, new String(b));

				if (!receivedBuffer.containsKey(pid - 1)) {
					FTSLHeader h = new FTSLHeader(sid, "NAK", pid,
							lastRecievedPacketID, 0);
					FTSLMessage ftslPacket = new FTSLMessage(null, h);

					try {
						outputStream.write(ftslPacket.toByte_());
						outputStream.flush();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				return 0;

			}

		} else if (flag.compareTo("REQ") == 0) {
	
			// Logger.log("Server recieved a FTSL REQUEST " +
			// LastRecievedPacketID);
			FTSLHeader h = new FTSLHeader(sid, "REP", 0,
					lastRecievedPacketID, 0);

			FTSLMessage ftslPacket = new FTSLMessage(null, h);

			try {

				outputStream.write(ftslPacket.toByte_());
				outputStream.flush();

			} catch (IOException e) {
				e.printStackTrace();
			}
			return -1;

		} else if (flag.compareTo("REP") == 0) {
			removeDeliveredMessages(rpid);
			return -1;

		} else if (flag == "NTF") {

			FTSLHeader h = new FTSLHeader(sid, "ACK", pid,
					lastRecievedPacketID, 0);
			FTSLMessage ftslPacket = new FTSLMessage(null, h);

			try {

				outputStream.write(ftslPacket.toByte_());
				outputStream.flush();

			} catch (IOException e) {
				e.printStackTrace();
			}
			
		} else if (flag.compareTo("ACK") == 0) {

			removeDeliveredMessages(rpid);
			int index = 0;
			while (index < sentBuffer.size()) {
				FTSLMessage packet = sentBuffer.get(index);
				if (packet.getHeader().getPID() > rpid) {
					try {

						outputStream.write(packet.toByte_());
						outputStream.flush();

					} catch (IOException e) {
						e.printStackTrace();

					}
				}
				index++;
			}

			// ServerFTSL.updateSession(sessionID, this);
			return -1;

		} else if (flag.compareTo("NAK") == 0) {
			removeDeliveredMessages(rpid);
			// all messages are lost should be sent again
			int index = 0;
			while (index < sentBuffer.size()) {
				FTSLMessage packet = sentBuffer.get(index);
				if (packet.getHeader().getPID() > rpid
						& packet.getHeader().getPID() < pid) {
					try {

						outputStream.write(packet.toByte_());
						outputStream.flush();

					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				index++;
			}
			return -1;

		}
		return -1;
	}

	/////////////////////////////////////////////////
	
	class logTask extends TimerTask {
		Session session;
		FTSL_Logger logger;

		public logTask(Session s, FTSL_Logger l) {
			session = s;
			logger = l;
		}

		public void run() {
			logger.log(session);
		}
	}

	public void close() {
		try {
			inputStream.close();
			outputStream.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}
