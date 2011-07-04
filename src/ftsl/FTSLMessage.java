package ftsl;

import java.io.Serializable;

public class FTSLMessage implements Serializable {

	FTSLHeader header;
	MessageProperties properties;
	byte[] data;

	public FTSLMessage(byte[] d, FTSLHeader header, MessageProperties p) {
		this.data = d;
		this.header = header;
		this.properties=p;

	}
	
	public FTSLMessage(byte[] d, FTSLHeader header) {
		this.data = d;
		this.header = header;
		properties=new MessageProperties();

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
	public MessageProperties getProperties() {
		return properties;
	}

	public void setProperties(MessageProperties properties) {
		this.properties = properties;
	}
	////////////////////////////// operations

	
	public String toString_() {

		String msgBody="";
		if (this.data != null)
			msgBody = new String(this.data);
		String msgHeader = header.toString_();
		String msgProperties = properties.toString_();
		String message= msgHeader + " | "+ msgProperties + "\n" + msgBody;

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
		message.setProperties(MessageProperties.valueOf_(str.substring(0,index)));
		message.setData(str.substring(index + 1).getBytes());
		return message;
	}

	public static FTSLMessage valueOf_(byte[] b) {
		String str = new String(b);
		return valueOf_(str);
	}

}
