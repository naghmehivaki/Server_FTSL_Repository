package ftsl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

public class FTSL_Logger extends Thread {

	int CLEANUP_DURATION = 60; // seconds
	private Lock lock = new Lock();

	String ftsl = "ftsl";
	String session = "session";
	String sentBuffer = "sentBuffer";
	String sentMessagesInfo = "sentMessagesInfo";
	String receivedBuffer = "sentMessagesInfo";

	File ftslF = null;
	File sessionF = null;
	File sentBufferF = null;
	File sentMessagesInfoF = null;
	File receivedBufferF = null;

	FileOutputStream ftslOut = null;
	FileOutputStream sessionsOut = null;
	FileOutputStream sentBufferOut = null;
	FileOutputStream sentMessagesInfoOut = null;
	FileOutputStream receivedBufferOut = null;

	/* ************************* Constructors */
	public FTSL_Logger() {

		init();
	}

	public FTSL_Logger(String sessionID) {

		ftsl = ftsl + "_" + sessionID;
		session = session + "_" + sessionID;
		sentBuffer = sentBuffer + "_" + sessionID;
		sentMessagesInfo = sentMessagesInfo + "_" + sessionID;
		receivedBuffer = receivedBuffer + "_" + sessionID;

		init();

	}

	/* ************************* */

	public void init() {
		sessionF = new File(session);

		try {
			if (!sessionF.exists())
				sessionF.createNewFile();
			sessionsOut = new FileOutputStream(sessionF);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		sentBufferF = new File(sentBuffer);

		try {
			if (!sentBufferF.exists())
				sentBufferF.createNewFile();
			sentBufferOut = new FileOutputStream(sentBufferF);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		sentMessagesInfoF = new File(sentMessagesInfo);

		try {
			if (!sentMessagesInfoF.exists())
				sentMessagesInfoF.createNewFile();
			sentMessagesInfoOut = new FileOutputStream(sentMessagesInfoF);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		receivedBufferF = new File(receivedBuffer);
		try {
			if (!receivedBufferF.exists())
				receivedBufferF.createNewFile();
			receivedBufferOut = new FileOutputStream(receivedBufferF);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/* ***************************** */

	public void log(Session session) {

		try {
			lock.lock();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		System.out.println("loge is false now 1");

		ftslF = new File(ftsl);

		try {
			if (!ftslF.exists())
				ftslF.createNewFile();
			ftslOut = new FileOutputStream(ftslF);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("loge is false now 2");

		// ///////////////////// start log the session

		String log = "";

		try {

			log = "Session ID: " + session.getSessionID() + "\n";
			log = log + "Socket: " + session.getSocket().getInetAddress() + " "
					+ session.getSocket().getPort() + "\n";
			log = log + "lastSentPacketID: " + session.getLastSentPacketID()
					+ "\n";
			log = log + "lastRecievedPacketID: "
					+ session.getLastRecievedPacketID() + "\n";
			log = log + "sendMessageID: " + session.getSendMessageID() + "\n";

			log = log + "SentMessagesInfo\n";
			int index = 0;
			Vector<MessageInfo> SentMessagesInfo = session
					.getSentMessagesInfo();
			while (index < SentMessagesInfo.size()) {
				MessageInfo info = SentMessagesInfo.get(index);
				log = log + info.getStart() + " " + info.getEnd() + " "+ info.getId() + "\n";
				index++;
			}
			ftslOut.write(log.getBytes());
			System.out.println("loge is false now 3");

			log = "sentBuffer\n";

			index = 0;
			int count=0;
			Vector<FTSLMessage> sentBuffer = session.getSentBuffer();
			while (index < sentBuffer.size()) {

				FTSLMessage packet = sentBuffer.get(index);
				log = log + packet.toString_() + "#####\n";
			//	System.out.println(sentBuffer.size()+" "+index);
				if (count==100){
					ftslOut.write(log.getBytes());
					log="";
					count=0;
					
				}
				count++;
				index++;
				
			}
			ftslOut.write(log.getBytes());

			System.out.println("loge is false now 4");

			log = "receivedBuffer\n";
			HashMap<Integer, String> receivedBuffer = session
					.getReceivedBuffer();
			Set<Integer> ids = receivedBuffer.keySet();
			Iterator<Integer> it = ids.iterator();
			while (it.hasNext()) {
				int id = it.next();
				log = log + id + ": " + receivedBuffer.get(id) + "\n"
						+ "#####\n";
			}

			ftslOut.write(log.getBytes());

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("loge is false now 5");

		// ////////////////////////////////////////////// end log the session

		try {
			sessionsOut.close();
			sentBufferOut.close();
			sentMessagesInfoOut.close();
			receivedBufferOut.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("loge is false now 6");


		sessionF.delete();
		sentBufferF.delete();
		sentMessagesInfoF.delete();
		receivedBufferF.delete();

		System.out.println("loge is false now 7");

		init();
		lock.unlock();
		System.out.println("loge is true now");

	}

	/* ***************************** */

	public void logSessionInfo(String key, String value) {

		String log = key + ": " + value + "\n";

		try {
			lock.lock();
			sessionsOut.write(log.getBytes());
			lock.unlock();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	/* ***************************** */

	public void logSessionInfo(String key, Socket socket) {
		String log = key + ": " + socket.getInetAddress() + " "
				+ socket.getPort() + "\n";
		try {
			lock.lock();
			sessionsOut.write(log.getBytes());
			lock.unlock();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	/* ***************************** */

	public void logSessionInfo(String key, int value) {

		String log = key + ": " + value + "\n";

		try {
			lock.lock();
			sessionsOut.write(log.getBytes());
			lock.unlock();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	/* ***************************** */

	public void logSession(Session session) {

		String log = "SessionID: " + session.getSessionID() + "\n";
		log = log + "Socket: " + session.getSocket().getInetAddress() + " "
				+ session.getSocket().getPort() + "\n";
		log = log + "lastSentPacketID: " + session.getLastSentPacketID() + "\n";
		log = log + "lastRecievedPacketID: "
				+ session.getLastRecievedPacketID() + "\n";
		log = log + "sendMessageID: " + session.getSendMessageID() + "\n";

		try {
			lock.lock();
			sessionsOut.write(log.getBytes());
			lock.unlock();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	/* ***************************** */

	public void logMessageInfo(MessageInfo info) {
		String log = info.getStart() + " " + info.getEnd() + " " + info.getId()
				+ "\n";

		try {
			lock.lock();
			sentMessagesInfoOut.write(log.getBytes());
			lock.unlock();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	/* ***************************** */

	public void logSentMessage(String message) {
		String log = message + "\n";

		try {
			lock.lock();
			sentBufferOut.write(log.getBytes());
			lock.unlock();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	/* ***************************** */

	public void logSentMessage(FTSLMessage message) {
		String log = message.toString_() + "\n" + "#####\n";

		try {
			lock.lock();
			sentBufferOut.write(log.getBytes());
			lock.unlock();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	/* ***************************** */

	public void logReceivedMessage(int id, String message) {
		String log = String.valueOf(id) + ": " + message + "\n";

		try {
			lock.lock();
			receivedBufferOut.write(log.getBytes());
			lock.unlock();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {

		}

	}

	/* ***************************** */

	public Session initSession(String sessionID) {
		Session session = new Session();

		BufferedReader reader = null;

		// reading the ftsl file

		try {
			String log;
			reader = new BufferedReader(new FileReader(ftsl + "_" + sessionID));
			if (reader == null)
				return null;

			log = reader.readLine();
			if (log == null)
				return null;

			while (log != null) {

				session.setSessionID(log);
				log = reader.readLine();
				String socketInfo = log; // should create the socket and
				log = reader.readLine();
				log = log.substring(log.indexOf(" ") + 1);
				session.setLastSentPacketID(Integer.valueOf(log));
				log = reader.readLine();
				log = log.substring(log.indexOf(" ") + 1);
				session.setLastRecievedPacketID(Integer.valueOf(log));
				log = reader.readLine();
				log = log.substring(log.indexOf(" ") + 1);
				session.setSendMessageID(Integer.valueOf(log));

				log = reader.readLine();
				if (log.compareTo("SentMessagesInfo") == 0) {
					Vector<MessageInfo> sentMessagesInfo = new Vector<MessageInfo>();

					while (log != "sentBuffer") {
						MessageInfo info = new MessageInfo();

						int index = log.indexOf(" ");
						info.setStart(Integer.valueOf(log.substring(0, index)));
						log = log.substring(index + 1);
						index = log.indexOf(" ");
						info.setEnd(Integer.valueOf(log.substring(0, index)));
						info.setId(Integer.valueOf(log.substring(index + 1)));
						sentMessagesInfo.add(info);

						log = reader.readLine();
					}
					session.setSentMessagesInfo(sentMessagesInfo);

				}

				log = reader.readLine();
				Vector<FTSLMessage> sentBuffer = session.getSentBuffer();

				String str = "";
				while (log != "receivedBuffer") {

					if (log != "#####") {
						str = str + log + "\n";
					} else {
						FTSLMessage packet = FTSLMessage.valueOf_(str);
						sentBuffer.add(packet);
					}
					log = reader.readLine();

				}
				session.setSentBuffer(sentBuffer);

				HashMap<Integer, String> receivedBuffer = new HashMap<Integer, String>();
				log = reader.readLine();

				str = "";
				while (log != null) {

					int id = 0;
					if (log != "#####") {
						if (str == "") {
							int index = log.indexOf(":");
							id = Integer.valueOf(log.substring(0, index));
							log = log.substring(index + 2);
						}
						str = str + log + "\n";
					} else {

						receivedBuffer.put(id, str);
					}
					log = reader.readLine();

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		// /////////////////////////////////////////////////////////////
		// reading session

		reader = null;

		try {
			String log;
			reader = new BufferedReader(new FileReader(session + "_"
					+ sessionID));
			if (reader != null) {

				log = reader.readLine();
				int lastSentPacketID = session.getLastSentPacketID();
				int lastRecievedPacketID = session.getLastRecievedPacketID();
				int sendMessageID = session.getSendMessageID();
				// int recieveMessageID = session.getRecieveMessageID();
				while (log != null) {

					int index = 0;
					if (log.contains("lastSentPacketID")) {
						index = log.indexOf(":");
						lastSentPacketID = Integer.valueOf(log
								.substring(index + 2));
					} else if (log.contains("lastRecievedPacketID")) {
						index = log.indexOf(":");
						lastRecievedPacketID = Integer.valueOf(log
								.substring(index + 2));
					} else if (log.contains("sendMessageID")) {
						index = log.indexOf(":");
						sendMessageID = Integer.valueOf(log
								.substring(index + 2));
					}

				}
				session.setLastSentPacketID(lastSentPacketID);
				session.setLastRecievedPacketID(lastRecievedPacketID);
				session.setLastRecievedPacketID(sendMessageID);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		// /////////////////////////////////////////////////////////////
		// reading sentBuffer

		reader = null;

		try {
			String log;
			reader = new BufferedReader(new FileReader(sentBuffer + "_"
					+ sessionID));
			if (reader != null) {

				log = reader.readLine();
				String str = "";
				while (log != null) {

					if (log != "#####") {
						str = str + log + "\n";
					} else {
						FTSLMessage packet = FTSLMessage.valueOf_(str);
						session.addSentMessage(packet);
					}
					log = reader.readLine();

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// /////////////////////////////////////////////////////////////
		// reading sentMessagesInfo

		reader = null;

		try {
			String log;
			reader = new BufferedReader(new FileReader(sentMessagesInfo + "_"
					+ sessionID));
			if (reader != null) {

				log = reader.readLine();
				String str = "";
				while (log != null) {
					MessageInfo info = new MessageInfo();

					int index = log.indexOf(" ");
					info.setStart(Integer.valueOf(log.substring(0, index)));
					log = log.substring(index + 1);
					index = log.indexOf(" ");
					info.setEnd(Integer.valueOf(log.substring(0, index)));
					info.setId(Integer.valueOf(log.substring(index + 1)));
					session.addMessageInfo(info);

					log = reader.readLine();

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// /////////////////////////////////////////////////////////////
		// reading receivedBuffer
		reader = null;

		try {
			String log;
			reader = new BufferedReader(new FileReader(receivedBuffer + "_"
					+ sessionID));
			if (reader != null) {

				log = reader.readLine();
				String str = "";
				while (log != null) {

					int id = 0;
					if (log != "#####") {
						if (str == "") {
							int index = log.indexOf(":");
							id = Integer.valueOf(log.substring(0, index));
							log = log.substring(index + 2);
						}
						str = str + log + "\n";
					} else {

						session.addreceivedMessage(id, str);
					}
					log = reader.readLine();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return session;
	}

	/* ***************************** */

	public class Lock {

		private boolean isLocked = false;

		public synchronized void lock() throws InterruptedException {
			while (isLocked) {
				wait();
			}
			isLocked = true;
		}

		public synchronized void unlock() {
			isLocked = false;
			notify();
		}
	}
}
