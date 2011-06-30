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
	static final int LOGGING_PERIOD = 20;
	int sleepTime = DEFAULT_VALUE;
	int MAX_BUFFER_SIZE = 1000;
	FTSL_Logger logger;
	Timer timer = new Timer();
	boolean stop = false;
	int lastRPID = 0;
	int lastEnd = 0;

	// ///////////////////////////// Session Basic Info
	ServerSocket serverSocket;
	Socket socket;
	ObjectInputStream inputStream = null;
	ObjectOutputStream outputStream = null;
	String sessionID = "";

	// //////////////////////////// Packets Info
	int lastSentPacketID = 0;
	int lastRecievedPacketID = 0;
	Vector<FTSLMessage> sentBuffer = new Vector<FTSLMessage>();
	HashMap<Integer, String> receivedBuffer = new HashMap<Integer, String>();

	// ///////////////////////////// Messages Info
	int sendMessageID = 1;
	Vector<MessageInfo> SentMessagesInfo = new Vector<MessageInfo>();

	/* ***************************** Constructor */
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
			
			readSessionID();


		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		timer.scheduleAtFixedRate(new logTask(this, logger),
				LOGGING_PERIOD * 1000, LOGGING_PERIOD * 1000);

	}

	// ///////////////////// setters and getters

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

	// /////////////////////////////////////

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

	/* ******************************* Operations */

	public int increaseLastSentPacketID() {
		lastSentPacketID++;
		logger.logSessionInfo("LastSentPacketID", lastSentPacketID);
		return lastSentPacketID;
	}

	public void increaseSendMessageID() {
		sendMessageID++;
		logger.logSessionInfo("SendMessageID", sendMessageID);
	}

	public void increaseLastRecievedPacketID() {
		lastRecievedPacketID++;
		logger.logSessionInfo("LastRecievedPacketID", lastRecievedPacketID);
	}

	public int increaseLastReceivedPacketID() {
		lastRecievedPacketID++;
		logger.logSessionInfo("LastRecievedPacketID", lastRecievedPacketID);
		return lastRecievedPacketID;
	}

	// //////////////////////////////////////

	public void keepSentPacket(int id, FTSLMessage packet) {
		sentBuffer.add(packet);
		logger.logSentMessage(packet);
	}

	public void addreceivedMessage(int id, String str) {
		receivedBuffer.put(id, str);
	}

	// //////////////////////////////////////

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

	// //////////////////////////////////////

	public void addMessageInfo() {

		MessageInfo info = new MessageInfo();
		if (SentMessagesInfo.size()==0)
			info.setStart(1);

		else
			info.setStart(lastEnd + 1);

		info.setId(sendMessageID);
		info.setEnd(lastSentPacketID);
		lastEnd=lastSentPacketID;
		SentMessagesInfo.add(info);
		logger.logMessageInfo(info);

	}
	
	public void addMessageInfo(MessageInfo info) {
		SentMessagesInfo.add(info);
	}

	// //////////////////////////////////////

	public int removeDeliveredMessages(int rpid) {
		int id = rpid;

		if (rpid > lastRPID) {
			lastRPID=rpid;
			int index = 0;
			while (index < SentMessagesInfo.size()) {
				MessageInfo info = SentMessagesInfo.get(index);
				
				if (info.getEnd() <= rpid) {
					id = info.getEnd();
					SentMessagesInfo.remove(index);
				} else
					index = SentMessagesInfo.size();
			}
			
			index = 0;
			while (index < sentBuffer.size()) {
				FTSLMessage message = sentBuffer.get(index);
				if (message.getHeader().getPID() <= id) {
					sentBuffer.remove(index);
//					Logger.log("######## removing: " + message.getHeader().getPID());
					
				} else
					index = sentBuffer.size();
			}
		}
		return id;
	}

	// /////////////////////////////////////

	public void write(byte[] buffer) {

		while (stop == true);
		buffer = processOutputPacket(buffer);
		try {
			outputStream.write(buffer);
			outputStream.flush();
			System.out.println(new String(buffer));
		} catch (IOException e) {
			Logger.log("server stopped here");
			e.printStackTrace();
			stop = true;
		}
	}

	public byte[] processOutputPacket(byte[] packet) {

		increaseLastSentPacketID();
		FTSLHeader header = new FTSLHeader(sessionID, "APP", lastSentPacketID,
				lastRecievedPacketID, packet.length);
		FTSLMessage pkt = new FTSLMessage(packet, header);
		byte[] buffer = pkt.toByte_();
		keepSentPacket(lastSentPacketID, pkt);
		
		return buffer;
	}

	public void flush() { // the end of a stream of the message
		addMessageInfo();
		increaseSendMessageID();
	}

	// /////////////////////////////////////
	public void readSessionID() {
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
		String flag = message.getHeader().getFLAG();
		sessionID = message.getHeader().getSID();
		
		if (flag.compareTo("NTF") == 0) {
			
			logger = new FTSL_Logger();
			// System.out.println(System.currentTimeMillis());
			Session tempSession = logger.initSession(sessionID);

			if (tempSession != null) {
			
				lastSentPacketID = tempSession.getLastSentPacketID();
				lastRecievedPacketID = tempSession.getLastRecievedPacketID();
				sentBuffer = tempSession.getSentBuffer();
				receivedBuffer = tempSession.getReceivedBuffer();
				sendMessageID = tempSession.getSendMessageID();
				SentMessagesInfo = tempSession.getSentMessagesInfo();
				tempSession=null;
				processFTSLHeader(packet);
			}
			//System.out.println(System.currentTimeMillis());
		}
		else {
			
			logger = new FTSL_Logger(sessionID);
		}
	}

	public int read(byte buffer[], int pos, int len) {

		int expectedID = lastRecievedPacketID + 1;
		if (receivedBuffer.containsKey(expectedID)) {

			byte[] tempBuffer = receivedBuffer.get(expectedID).getBytes();
			processInputPacket(tempBuffer);

			for (int i = 0; i < tempBuffer.length; i++)
				buffer[pos + i] = tempBuffer[i];

			return tempBuffer.length;

		} else {

			int read = 0;
			byte[] packet = new byte[len];

			while (read == 0) {
				try {
					read = inputStream.read(packet);
				} catch (IOException e) {
					e.printStackTrace();
					read = 0;
				}
			}

			int test = 1;
			while (read != -1 & test == 1) {
				int pSize = processInputPacket(packet);
				// System.out.println(new String (packet));

				if (pSize == 0) {
					try {
						// System.out.println(new String (packet));

						read = inputStream.read(packet);
					} catch (IOException e) {
						e.printStackTrace();
						read = 0;
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

	public int processInputPacket(byte buffer[]) {

		String packet = new String(buffer);
		if (!packet.startsWith(FTSLHeader.protocol))
			return 0;

		int result = processFTSLHeader(buffer);

		if (result == 0) {
			return 0;

		} else {

			int index = packet.indexOf("\n");
			String h = packet.substring(0, index);
			String b = packet.substring(index + 1);

			byte[] tempBody = b.getBytes();
			for (int i = 0; i < tempBody.length; i++)
				buffer[i] = tempBody[i];

			FTSLHeader header = FTSLHeader.valueOf_(h);
			return header.getMessageSize();
		}
	}

	public int processFTSLHeader(byte[] buffer) {

		String packet = new String(buffer);
		int index = packet.indexOf("\n");
		String str = packet.substring(0, index);

		FTSLHeader header = FTSLHeader.valueOf_(str);

		String flag = header.getFLAG();
		String sid = header.getSID();
		int pid = header.getPID();
		int rpid = header.getrPID();

		if (flag.compareTo("APP") == 0) {

			int expectedPID = lastRecievedPacketID + 1;
			System.out.println("pid: "+pid+" expected id: "+expectedPID);
			if (pid == expectedPID) {
				// it is the right message, no need to check anything else
				increaseLastReceivedPacketID();
				removeDeliveredMessages(rpid);
				return 1;

			} else if (pid < expectedPID) {

				return 0;

			} else {
				receivedBuffer.put(pid, packet);
				if (!receivedBuffer.containsKey(pid - 1)) {
					FTSLHeader h = new FTSLHeader(sid, "NAK", pid,
							lastRecievedPacketID, 0);
					FTSLMessage ftslPacket = new FTSLMessage(null, h);

					try {

						outputStream.write(ftslPacket.toByte_());
						outputStream.flush();

					} catch (IOException e) {
						e.printStackTrace();
						if (socket.isConnected() == false)
							stop = true;
					}
				}
				return 0;
			}

		} else if (flag.compareTo("REQ") == 0) {

			FTSLHeader h = new FTSLHeader(sid, "REP", 0, lastRecievedPacketID,
					0);

			FTSLMessage ftslPacket = new FTSLMessage(null, h);

			try {
				outputStream.write(ftslPacket.toByte_());
				outputStream.flush();

			} catch (IOException e) {
				e.printStackTrace();
				stop = true;
			}
			return 0;

		} else if (flag.compareTo("REP") == 0) {

			removeDeliveredMessages(rpid);

			index = 0;
			while (index < sentBuffer.size()) {
				FTSLMessage pkt = sentBuffer.get(index);
				if (pkt.getHeader().getPID() > rpid) {
					try {
						outputStream.write(pkt.toByte_());
						outputStream.flush();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				index++;
			}
			return 0;

		} else if (flag.compareTo("NTF") == 0) {

			//int id = removeDeliveredMessages(rpid);

			FTSLHeader h = new FTSLHeader(sid, "NTF", 0, lastRecievedPacketID,
					0);

			Logger.log("LastRecievedPacketID: "+lastRecievedPacketID);
			FTSLMessage ftslPacket = new FTSLMessage(null, h);
			System.out.println("server: "+ftslPacket.toString_());

			try {
				outputStream.write(ftslPacket.toByte_());
				outputStream.flush();

			} catch (IOException e) {
				e.printStackTrace();
				stop = true;
			}

			index = 0;
			while (index < sentBuffer.size()) {
				FTSLMessage pkt = sentBuffer.get(index);
				if (pkt.getHeader().getPID() > rpid) {
					try {
						outputStream.write(pkt.toByte_());
						outputStream.flush();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				index++;
			}

			return 0;

		} else if (flag.compareTo("ACK") == 0) {

			int id = removeDeliveredMessages(rpid);

			return 0;

		} else if (flag.compareTo("NAK") == 0) {
			removeDeliveredMessages(rpid);
			// all messages are lost should be sent again
			index = 0;
			while (index < sentBuffer.size()) {
				FTSLMessage pkt = sentBuffer.get(index);
				int id = pkt.getHeader().getPID();
				if (id > rpid & id < pid) {
					try {
						outputStream.write(pkt.toByte_());
						outputStream.flush();

					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				index++;
			}

			return 0;
		}
		return 0;
	}

	// ///////////////////////////////////////////////

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
