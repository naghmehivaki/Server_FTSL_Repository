package ftsl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.TimerTask;
import java.util.Vector;
import java.util.Timer;

import ftsl.FTHTTP.AppInterfaceImp;

//import util.Logger;

public class Session extends Thread {

	int DEFAULT_VALUE = 1000;
	static final int LOGGING_PERIOD = 10;
	int sleepTime = DEFAULT_VALUE;
	int MAX_BUFFER_SIZE = 1000;
	//AppInterface appInterface = new AppInterfaceImp();
	FTSL_Logger logger;
	Timer timer = new Timer();

	ServerSocket serverSocket;
	Socket socket;
	ObjectInputStream inputStream = null;
	ObjectOutputStream outputStream = null;
	String sessionID = "";
	// ///////////////////////////////////////// Packets Info
	int lastSentPacketID = 0;
	int lastRecievedPacketID = 0;
	Vector<FTSLMessage> sentBuffer = new Vector<FTSLMessage>();
	HashMap<Integer, String> receivedBuffer = new HashMap<Integer, String>();
	// ///////////////////////////////////////// Messages Info
	int sendMessageID = 0;
	int recieveMessageID = 0;
	// MessageInfo LastReceivedMessageInfo = new MessageInfo();
	Vector<MessageInfo> SentMessagesInfo = new Vector<MessageInfo>();

	// /////////////////////////////////////////// Constructor

	class logTask extends TimerTask {
		Session session;
		FTSL_Logger logger;

		public logTask(Session s, FTSL_Logger l) {
			session = s;
			logger = l;
		}

		public void run() {
			// logger.lockLogger();
			logger.log(session);
			// logger.releaseLogger();
			// System.exit(0); //Stops the AWT thread (and everything else)
		}
	}

	public Session() {

	}

	public void close() {
		try {
			inputStream.close();
			outputStream.close();
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

			// inputChannel = new InputChannel(ois, sessionID);
			// outputChannel = new OutputChannel(oos, sessionID);
			logger = new FTSL_Logger(sessionID);

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
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

			// inputChannel = new InputChannel(ois);
			// outputChannel = new OutputChannel(oos);
			String sid=read();
			
			// Logger.log("session ID is: " + sessionID);

			// Logger.log("Session created the input and outputstream for in the session of the server side");
			if (sid != "") {
				sessionID=sid;
				System.out.println(System.currentTimeMillis());

				logger = new FTSL_Logger(sessionID);
				logger.logSession(this);
			} 
			System.out.println(System.currentTimeMillis());
	

			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		timer.scheduleAtFixedRate(new logTask(this, logger),
				LOGGING_PERIOD * 1000, LOGGING_PERIOD * 1000);

	}

	// ////////////////////////////////////////// setters and getters
	// public InputChannel getInputChannel() {
	// return inputChannel;
	// }
	//
	// public OutputChannel getOutputChannel() {
	// return outputChannel;
	// }

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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.logSessionInfo("Socket", socket);
	}

	public void setSendMessageID(int sendMessageID) {
		this.sendMessageID = sendMessageID;
	}

	public void setRecieveMessageID(int recieveMessageID) {
		this.recieveMessageID = recieveMessageID;
	}

	// public void setInputChannel(InputChannel inputChannel) {
	// this.inputChannel = inputChannel;
	// }
	//
	// public void setOutputChannel(OutputChannel outputChannel) {
	// this.outputChannel = outputChannel;
	// }

	public String getSessionID() {
		return sessionID;
	}

	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
		//logger.logSessionInfo("SessionID", sessionID);
	}

	public int getLastSentPacketID() {
		return lastSentPacketID;
	}

	public void setLastSentPacketID(int id) {
		this.lastSentPacketID = id;
	//	logger.logSessionInfo("LastSentPacketID", lastSentPacketID);

	}

	public int getLastRecievedPacketID() {
		return lastRecievedPacketID;
	}

	public void setLastRecievedPacket(int id) {
		lastRecievedPacketID = id;
	//	logger.logSessionInfo("lastRecievedPacketID", lastRecievedPacketID);

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
//		logger.logSessionInfo("SendMessageID", sendMessageID);

	}

	public int getRecieveMessageID() {
		return recieveMessageID;
	}

	public void setLastRecievedMessageID(int id) {
		recieveMessageID = id;
//		logger.logSessionInfo("RecieveMessageID", recieveMessageID);

	}

	// public MessageInfo getLastReceivedMessageInfo() {
	// return LastReceivedMessageInfo;
	// }
	//
	// public void setLastReceivedMessageInfo(MessageInfo
	// lastReceivedMessageInfo) {
	// LastReceivedMessageInfo = lastReceivedMessageInfo;
	// }

	public Vector<MessageInfo> getSentMessagesInfo() {
		return SentMessagesInfo;
	}

	public void setSentMessagesInfo(Vector<MessageInfo> sentMessagesInfo) {
		SentMessagesInfo = sentMessagesInfo;
	}

	public void setLastRecievedPacketID(int lastRecievedPacketID) {
		lastRecievedPacketID = lastRecievedPacketID;
//		logger.logSessionInfo("LastRecievedPacketID", lastRecievedPacketID);

	}

	// ////////////////////////////////////////////// Operations

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

	public void keepSentPacket(int id, FTSLMessage packet) {
		sentBuffer.add(packet);
		logger.logSentMessage(packet);
		// if (sentBuffer.size() == MAX_BUFFER_SIZE) {
		// //Logger.log("1111111111111111111111111111111111111111");
		// sendFTSLRequest();
		// }

	}

	public void sendFTSLRequest() {
		FTSLHeader header = new FTSLHeader(sessionID, "FTSL_REQUEST", 0,
				lastRecievedPacketID, 0);

		FTSLMessage packet = new FTSLMessage(null, header);
		byte[] buffer = packet.toByte_();

		try {

			outputStream.write(buffer, 0, buffer.length);
			outputStream.flush();

		} catch (IOException e) {

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

		}
	}

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

	public void addreceivedMessage(int id, String str) {
		receivedBuffer.put(id, str);
	}

	public void updateMessageInfo(int pid) {
		SentMessagesInfo.lastElement().setIndex(pid);
	}

	public void increaseLastRecievedPacketID() {
		lastRecievedPacketID++;
		logger.logSessionInfo("lastRecievedPacketID", lastRecievedPacketID);
		// return LastRecievedPacketID;
	}

	public void increaseRecieveMessageID() {
		recieveMessageID++;
		logger.logSessionInfo("RecieveMessageID", recieveMessageID);
		// return recieveMessageID;
	}

	public synchronized void removeDeliveredMessages(int rpid) {

		int index = 0;
		// Vector<MessageInfo> SentMessagesInfo = new Vector<MessageInfo>();

		int id = rpid;
		while (index < SentMessagesInfo.size()) {
			MessageInfo info = SentMessagesInfo.get(index);
			if (info.getEnd() != 0 & info.getEnd() <= rpid) {
				id = info.getEnd();
				SentMessagesInfo.remove(index);
				// Logger.log("***********************************************");

			} else
				index = SentMessagesInfo.size();
		}

		index = 0;
		while (index < sentBuffer.size()) {
			FTSLMessage message = sentBuffer.get(index);
			if (message.getHeader().getPID() <= rpid) {
				sentBuffer.remove(index);
				// Logger.log("***********************************************");

			} else
				index = sentBuffer.size();
		}

	}

	// public synchronized void updateReceivedMessageInfo(int mid) {
	//
	// LastRecievedPacketID++;
	// if (recieveMessageID < mid) {
	// recieveMessageID=mid;
	// if (mid==2){
	// MessageInfo info = new MessageInfo();
	// info.setStart(1);
	// info.setIndex(LastRecievedPacketID - 1);
	// info.setEnd(LastRecievedPacketID - 1);
	// info.setId(recieveMessageID - 1);
	// LastReceivedMessageInfo = info;
	// Logger.log("ooooooooooooooooooooooooooooooooooo");
	// }
	// else if (mid>2){
	// MessageInfo info = new MessageInfo();
	// info.setStart(LastReceivedMessageInfo.getEnd() + 1);
	// info.setIndex(LastRecievedPacketID - 1);
	// info.setEnd(LastRecievedPacketID - 1);
	// info.setId(recieveMessageID - 1);
	// LastReceivedMessageInfo = info;
	// Logger.log("wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww");
	//
	// }
	// }
	// }
	// public void updateReceivedMessageInfo() {
	//
	// if (recieveMessageID == 1) {
	// MessageInfo info = new MessageInfo();
	// info.setStart(1);
	// info.setIndex(LastRecievedPacketID);
	// info.setEnd(LastRecievedPacketID);
	// info.setId(recieveMessageID);
	// LastReceivedMessageInfo = info;
	// } else if (recieveMessageID > 1) {
	// MessageInfo info = new MessageInfo();
	// info.setStart(LastReceivedMessageInfo.getEnd() + 1);
	// info.setIndex(LastRecievedPacketID);
	// info.setEnd(LastRecievedPacketID);
	// info.setId(recieveMessageID);
	// LastReceivedMessageInfo = info;
	// }
	//
	// }

	public int getLastRecievedMessageID() {
		return recieveMessageID;
	}

	public void updateSocket(Socket s) {
		this.socket = s;
		logger.logSessionInfo("Socket", socket);
		// ObjectInputStream ois = null;
		// ObjectOutputStream oos = null;

		try {
			inputStream = new ObjectInputStream(s.getInputStream());
			outputStream = new ObjectOutputStream(s.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// inputChannel.setInputStream(ois);
		// outputChannel.setOutputStream(oos);
	}

	// public ObjectOutputStream getOutputStream() {
	// return outputChannel.getOutputStream();
	// }
	//
	// public ObjectInputStream getInputStream() {
	// return inputChannel.getInputStream();
	// }

	// /////////////////////
	public void write(byte[] buffer) {

		buffer = processOutputPacket(buffer);

		try {
			outputStream.write(buffer);
			outputStream.flush();

			// Logger.log("Server session wrote:\n" + new String(buffer));
		} catch (IOException e) {
			// HandleFailure();
		}
	}

	// public int read(byte buffer[]){
	// int read=-1;
	//
	// try {
	// read = inputStream.read(buffer);
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	//
	// int test=1;
	// while (read !=-1 & test == 1){
	//
	// Logger.log("Server session read:\n"+ new String(buffer));
	//
	// byte [] tempBuffer= ClientFTSL.processInputPacket(sessionID, buffer);
	// if (tempBuffer==null){
	// try {
	// read = inputStream.read(buffer);
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }else {
	// test=0;
	// buffer=tempBuffer;
	// }
	// }
	//
	// return read;
	// }

	public void flush(){
		sendMessageID++;
		addMessageInfo();
	}
	
	public String read() {
		byte[] packet = new byte[1024];
		int read = 0;
		try {
			read = inputStream.read(packet);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		while (read == -1) {
			try {
				read = inputStream.read(packet);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// Logger.log("Server read the session ID from the client side:\n"+ new
		// String(packet));
		FTSLMessage message = FTSLMessage.valueOf_(packet);
		String flag= message.getHeader().getFLAG();
		if (flag.compareTo("FTSL_NOTIFICATION")==0) {
			
			System.out.println(System.currentTimeMillis());
			logger = new FTSL_Logger();

			Session tempSession = logger.initSession(message.getHeader().getSID());
			sessionID = tempSession.getSessionID();
			lastSentPacketID = tempSession.getLastSentPacketID();
			lastRecievedPacketID = tempSession.getLastRecievedPacketID();
			sentBuffer = tempSession.getSentBuffer();
			receivedBuffer = tempSession.getReceivedBuffer();
			sendMessageID = tempSession.getSendMessageID();
			recieveMessageID = tempSession.getRecieveMessageID();
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
			processFTSLBody(receivedBuffer.get(expectedID));
			//ServerFTSL.updateSession(sessionID, this);
			//System.out.println("tempSize" + tempBuffer.length);
			for (int i = 0; i < tempBuffer.length; i++)
				buffer[pos + i] = tempBuffer[i];

			return tempBuffer.length;

		} else {
			int read = -1;

			byte[] packet = new byte[3096];

			// System.out.println("len ============"+ len);
			try {
				read = inputStream.read(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}

			int test = 1;
			while (read != -1 & test == 1) {

				// Logger.log("Server session read:\n"+ new String(packet));

				int pSize = processInputPacket(packet, read);

				if (pSize == 0) {
					//System.out.println(new String(packet));
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
					// Logger.log("buffer after process is: \n"+new
					// String(buffer));
				}
			}

			return read;
		}
	}

	// ///////////////////////////
	public byte[] processOutputPacket(byte[] packet) {

		// Logger.log("FTSL is writing in the output stream in the server side of the proxy.");
		increaseLastSentPacketID();

//		if (appInterface.isNewOutgoingMessage(packet)) {
//			increaseSendMessageID();
//			addMessageInfo();
//		} else {
//			updateMessageInfo(lastSentPacketID);
//		}

		FTSLHeader header = new FTSLHeader(sessionID,
				"", lastSentPacketID,
				recieveMessageID, sendMessageID, packet.length);

		// Logger.log("the header of the packet is: " + header.toString_());

		FTSLMessage pkt = new FTSLMessage(packet, header);
		byte[] buffer = pkt.toByte_();

		keepSentPacket(lastSentPacketID, pkt);

		// ServerFTSL.updateSession(sessionID, this);

		return buffer;

	}

	public int processInputPacket(byte buffer[], int read) {

		String packet = new String(buffer);
		if (!packet.startsWith(FTSLHeader.protocol))
			return 0;

		int index = packet.indexOf("\n");
		if (index == -1) {
			//System.out.println("*********" + packet);
			// byte[] test=new byte [1024];
			try {
				int k = inputStream.read(buffer, read, 1024);

				packet = new String(buffer);
				//System.out.println(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		index = packet.indexOf("\n");
		String h = packet.substring(0, index);
		String b = packet.substring(index + 1);
		// System.out.println("header: "+h+" body is:"+b);
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

	public void processFTSLBody(String body) {

		// System.out.println("body: "+body);
		int index = body.indexOf(" ");
		if (index != -1) {
			String m = body.substring(0, index);
			if (m.compareTo("GET") == 0) {
				increaseRecieveMessageID();
				return;
				// session.updateReceivedMessageInfo();
			}

		}

		// index = body.indexOf("\n");
		// String lastLine="";
		// while(index!=-1){
		// lastLine=body;
		// body=body.substring(index+1);
		// index=body.indexOf("\n");
		// }

		// if (body.compareTo("")==0)
		// updateReceivedMessageInfo();

	}

	public int processFTSLHeader(FTSLHeader header, byte[] b) {

		String flag = header.getFLAG();
		String sid = header.getSID();
		int pid = header.getPID();
		int mid = header.getMID();
		int rpid = header.getrPID();

		if (flag.compareTo("") == 0) {
			
			/*
			 * this packet is a http reply assumption: server side received all
			 * packets in the same order are sent and prepared their reply and
			 * rent the replies. 1- get the packet ID and clean the request from
			 * the buffer. but first it should check to see if there any packet
			 * with the pid less than this pid and has not been replied yet. in
			 * this case we should send those requests first and get their reply
			 */

			// Logger.log("the packet is received is a "+
			// appInterface.getRecieveMessageType());

			// first thing we should check is to see if the packet is the right

			// if (LastRecievedPacketID - lastSentPacketID >= MAX_BUFFER_SIZE) {
			// sendFTSLReply();
			// }

			int expectedPID = lastRecievedPacketID + 1;

			if (pid == expectedPID) {
				// it is the right message, no need to check anything else
				increaseLastRecievedPacketID();
				processFTSLBody(new String(b));
				removeDeliveredMessages(rpid);

				// ServerFTSL.updateSession(sessionID, this);
				return 1;

			} else {

				receivedBuffer.put(pid, new String(b));
				logger.logReceivedMessage(pid, new String(b));

				if (!receivedBuffer.containsKey(pid - 1)) {
					FTSLHeader h = new FTSLHeader(sid, "FTSL_NACK", pid,
							lastRecievedPacketID, getLastRecievedMessageID());
					FTSLMessage ftslPacket = new FTSLMessage(null, h);

					try {

						outputStream.write(ftslPacket.toByte_());
						outputStream.flush();

					} catch (IOException e) {
						// TODO: handle exception
					}
				}
				return 0;

			}

		} else if (flag.compareTo("FTSL_REQUEST") == 0) {
			// this is a request for the last packet and message is received in
			// client side

			// Logger.log("Server recieved a FTSL REQUEST " +
			// LastRecievedPacketID);
			FTSLHeader h = new FTSLHeader(sid, "FTSL_REPLY", 0,
					lastRecievedPacketID, 0);

			FTSLMessage ftslPacket = new FTSLMessage(null, h);

			try {

				outputStream.write(ftslPacket.toByte_());
				outputStream.flush();

			} catch (IOException e) {
				// TODO: handle exception
			}
			return -1;

		} else if (flag.compareTo("FTSL_REPLY") == 0) {
			// Logger.log("Server recieved a FTSL REPLY " + rpid);

			removeDeliveredMessages(rpid);

			// // int index = 0;
			// // while (index < sentBuffer.size()) {
			// // FTSLMessage packet = sentBuffer.get(index);
			// // if (packet.getHeader().getPID() > rpid) {
			// // try {
			// //
			// // outputStream.write(packet.toByte_());
			// // outputStream.flush();
			// //
			// // } catch (IOException e) {
			// // // TODO: handle exception
			// // }
			// // }
			// // index++;
			// // }
			//
			// ServerFTSL.updateSession(sessionID, this);
			return -1;

		} else if (flag == "FTSL_NOTIFICATION") {

			FTSLHeader h = new FTSLHeader(sid, "FTSL_ACK", pid,
					lastRecievedPacketID, getLastRecievedMessageID());
			FTSLMessage ftslPacket = new FTSLMessage(null, h);

			try {

				outputStream.write(ftslPacket.toByte_());
				outputStream.flush();

			} catch (IOException e) {
				// TODO: handle exception
			}
			
		} else if (flag.compareTo("FTSL_ACK") == 0) {

			removeDeliveredMessages(rpid);
			// all messages are lost should be sent again
			int index = 0;
			while (index < sentBuffer.size()) {
				FTSLMessage packet = sentBuffer.get(index);
				if (packet.getHeader().getPID() > rpid) {
					try {

						outputStream.write(packet.toByte_());
						outputStream.flush();

					} catch (IOException e) {
						// TODO: handle exception
					}
				}
				index++;
			}

			// ServerFTSL.updateSession(sessionID, this);
			return -1;

		} else if (flag.compareTo("FTSL_NACK") == 0) {
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
						// TODO: handle exception
					}
				}
				index++;
			}

			// ServerFTSL.updateSession(sessionID, this);
			return -1;

		}
		return -1;
	}

}
