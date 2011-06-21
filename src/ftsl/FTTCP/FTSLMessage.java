package ftsl.FTTCP;

import java.io.Serializable;

public class FTSLMessage implements Serializable {

	byte[] data;
	FTSLHeader aslHeader;

	public FTSLMessage(byte[] d, FTSLHeader header) {
		this.data = d;
		this.aslHeader = header;

	}

	public FTSLMessage() {
	}

	public void setData(byte[] d) {
		this.data = d;
	}

	public void setAslHeader(FTSLHeader header) {
		this.aslHeader = header;
	}

	public byte[] getData() {
		return this.data;
	}

	public FTSLHeader getHeader() {
		return this.aslHeader;
	}

	public String toString_() {

		String msgBody="";
		if (this.data != null)
			msgBody = new String(this.data);
		String msgHeader = this.aslHeader.toString_();
		String message= msgHeader + "\n" + msgBody;

		return message;
	}

	public byte[] toByte_() {

		String str = this.toString_();
		byte[] message = str.getBytes();
		return message;
	}

	public static FTSLMessage valueOf_(String str) {
		FTSLMessage message = new FTSLMessage();
		int index = str.indexOf("\n");
		message.setAslHeader(FTSLHeader.valueOf_(str.substring(0, index)));
		message.setData(str.substring(index + 1).getBytes());
		return message;
	}

	public static FTSLMessage valueOf_(byte[] b) {
		String str = new String(b);
		return valueOf_(str);
	}

}
