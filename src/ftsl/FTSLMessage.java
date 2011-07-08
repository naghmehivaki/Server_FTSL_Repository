package ftsl;

import java.io.Serializable;

public class FTSLMessage implements Serializable {

	FTSLHeader header;
	MessageHandler properties;
	byte[] data;

	public FTSLMessage(FTSLHeader header, MessageHandler p, byte[] d) {
		this.data = d;
		this.header = header;
		this.properties=p;
	}
	
	public FTSLMessage(byte[] d, FTSLHeader header) {
		this.data = d;
		this.header = header;
		properties=new MessageHandler();
	}
	
	public FTSLMessage() {
	}
	
	/////////////////////////////////////// Setters and getters

	public void setData(byte[] d) {
		this.data = d;
	}

	public byte[] getData() {
		return this.data;
	}

	public void setHeader(FTSLHeader h) {
		header = h;
	}
	public FTSLHeader getHeader() {
		return header;
	}
	public MessageHandler getProperties() {
		return properties;
	}

	public void setProperties(MessageHandler properties) {
		this.properties = properties;
	}
	////////////////////////////// operations

	
	public String toString_() {

		String msgBody="";
		if (this.data != null)
			msgBody = new String(this.data);
		String msgHeader = header.toString_();
		String message="";
		if (properties != null){
			String msgProperties = properties.toString_();
			message= msgHeader + " | "+ msgProperties + "\n" + msgBody;
		}
		else {
			message= msgHeader + "\n" + msgBody;
		}

		return message;
	}

	public byte[] toByte_() {

		String str = this.toString_();
		byte[] message = str.getBytes();
		return message;
	}

	public static FTSLMessage valueOf_(String str) {
		FTSLMessage message = new FTSLMessage();
		int index = str.indexOf("|");
		message.setHeader(FTSLHeader.valueOf_(str.substring(0, index-1)));
		str=str.substring(index+2);
		index=str.indexOf("\n");
		message.setProperties(MessageHandler.valueOf_(str.substring(0,index)));
		message.setData(str.substring(index + 1).getBytes());
		return message;
	}

	public static FTSLMessage valueOf_(byte[] b) {
		String str = new String(b);
		return valueOf_(str);
	}

}
