package ftsl.FTHTTP;

import ftsl.FTTCP.*;

public class AppInterfaceImp extends Thread implements AppInterface {

	String sendMessageType = "REPLY";
	String receiveMessageType = "REQUEST";

	public boolean isNewIncomingMessage(byte[] packet) {
		String str = new String(packet);

		if (receiveMessageType == "REQUEST") {
			if (str.startsWith("GET") || str.startsWith("OPTIONS") || str.startsWith("HEAD") || str.startsWith("POST") || str.startsWith("PUT") || str.startsWith("DELETE") || str.startsWith("TRACE") || str.startsWith("CONNECT")) 
				return true;
			else
				return false;

		} else if (receiveMessageType == "REPLY") {
			if (str.startsWith("HTTP/1.1")) {
				return true;
			} else
				return false;

		}else 
			return false;

	}

	public boolean isNewOutgoingMessage(byte[] packet) {
		String str = new String(packet);
		
		if (sendMessageType == "REQUEST") {
			if (str.startsWith("GET") || str.startsWith("OPTIONS") || str.startsWith("HEAD") || str.startsWith("POST") || str.startsWith("PUT") || str.startsWith("DELETE") || str.startsWith("TRACE") || str.startsWith("CONNECT")) 
				return true;
			else
				return false;

		} else if (sendMessageType == "REPLY") {
			if (str.startsWith("HTTP/1.1")) {
				return true;
			} else
				return false;

		}else 
			return false;
	}

	@Override
	public String getSendMessageType() {
		return sendMessageType;
	}

	@Override
	public String getRecieveMessageType() {
		return receiveMessageType;
	}

}
