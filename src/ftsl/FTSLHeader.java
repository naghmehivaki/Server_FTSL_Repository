package ftsl;

import java.io.Serializable;


public class FTSLHeader implements Serializable {

	/**
	 * 
	 */
	static String protocol="FTSL-1.0";
	
	String SID;
	/* flag identifies if a message is request or reply if flag is "req"
	 *  then it is a request and if flag is "rep" it is a reply
	 */
	
	/* 1- if FLAG="REQUEST" then the message is a http request 
	 * 2- if FLAG="REPLY" then the message is a http reply for the last received 
	 * 3- if Flag="FTSL_NOTIFICATION" then the message is for informing about the new session is replaced with the old 
	 * 4- if Flag="FTS_ACK" then the message is the ACK for informing about the session replacement 
	 * 5- if Flag="FTSL_REQUEST" then the message is a FTSL request
	 * 6- if Flag="FTSL_REPLY" then the message is a FTSL reply
	 * 7- if Flag="FTSL_NACK" then it means that a reply is received but there are some
	 *  messages waiting for their reply. server should send the replies again*/
	
	String FLAG = "";
	
	int PID = 1;
	
	// ready reply in the server side and received reply in the client side
	int rPID = 0;



	// this the id of the Http request and reply. it helps to control the requests and replys
	int MID=0;
	
	int MessageSize=0;
	
	
	

	public FTSLHeader(String sid, String flag, int pid, int rpid, int mid, int size) {
		this.SID = sid;
		this.FLAG=flag;
		this.PID = pid;
		this.rPID = rpid;
		this.MID = mid;
		this.MessageSize=size;

	}
	public FTSLHeader(String sid, String flag, int pid, int rpid, int mid) {
		this.SID = sid;
		this.FLAG=flag;
		this.PID = pid;
		this.rPID = rpid;
		this.MID = mid;
		this.MessageSize=0;
	}
	public FTSLHeader(String sid, String flag, int rpid, int mid) {
		this.SID = sid;
		this.FLAG=flag;
		this.PID = 0;
		this.rPID = rpid;
		this.MID = mid;
		this.MessageSize=0;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}


	public int getMID() {
		return MID;
	}

	public void setMID(int rID) {
		MID = rID;
	}

	public String getFLAG() {
		return FLAG;
	}

	public void setFLAG(String fLAG) {
		FLAG = fLAG;
	}

	public int getrPID() {
		return rPID;
	}

	public void setrPID(int rPID) {
		this.rPID = rPID;
	}

	public FTSLHeader() {

	}

	public void setSID(String sid) {
		this.SID = sid;

	}

	public void setPID(int pid) {
		this.PID = pid;

	}

	public int getPID() {
		return this.PID;

	}

	public String getSID() {
		return this.SID;

	}
	

	public int getMessageSize() {
		return MessageSize;
	}

	public void setMessageSize(int messageSize) {
		MessageSize = messageSize;
	}

	public String toString_() {
		String sid = this.SID;
		String pid = String.valueOf(this.PID);
		String rpid = String.valueOf(this.rPID);
		String mid = String.valueOf(this.MID);
		String size = String.valueOf(this.MessageSize);

		String header = protocol+" "+sid + " " +this.FLAG+" "+ pid + " " + rpid+ " " + mid + " " + size;
		return header;
	}

	public byte[] toByte_() {
		String str = this.toString_();
		byte[] header = str.getBytes();
		return header;
	}

	public static FTSLHeader valueOf_(String str) {
		
		FTSLHeader header = new FTSLHeader();
		int index = str.indexOf(" ");
		header.setProtocol(str.substring(0, index));
		str=str.substring(index + 1);
		index = str.indexOf(" ");
		header.setSID(str.substring(0, index));
		str=str.substring(index + 1);
		index = str.indexOf(" ");
		header.setFLAG(str.substring(0, index));
		str=str.substring(index + 1);
		index = str.indexOf(" ");
		header.setPID(Integer.valueOf(str.substring(0, index)));
		str=str.substring(index + 1);
		index = str.indexOf(" ");
		header.setrPID(Integer.valueOf(str.substring(0, index)));
		str=str.substring(index + 1);
		index = str.indexOf(" ");
		header.setMID(Integer.valueOf(str.substring(0, index)));
		header.setMessageSize(Integer.valueOf(str.substring(index + 1)));
		return header;
	}

	public static FTSLHeader valueOf_(byte[] b) {
		String str = new String(b);
		return valueOf_(str);
	}
	
	public static boolean isFTSLHeader(String str){
		int index=str.indexOf(" ");
		if (str.substring(0, index)==protocol)
			return true;
		else 
			return false;
	}

}
