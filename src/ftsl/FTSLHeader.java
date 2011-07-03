package ftsl;

import java.io.Serializable;


public class FTSLHeader implements Serializable {

	
	static String protocol="FTSL-1.0";
	
	String SID;
	
	/* 0- id flag="APP" then the message is from the application layer
	 * 1- if Flag="NTF" then the message is for informing about the new session is replaced with the old 
	 * 2- if Flag="ACK" then the message is the ACK for informing about the session replacement 
	 * 3- if Flag="REQ" then the message is a FTSL request
	 * 4- if Flag="REP" then the message is a FTSL reply
	 * 5- if Flag="NAK" then it means that a reply is received but there are some messages waiting for their reply. server should send the replies again*/
	
	String FLAG = "";
	int PID = 1;
	int rPID = 0;
	//int MessageSize=0;
	
	public FTSLHeader(String sid, String flag, int pid, int rpid) {
		this.SID = sid;
		this.FLAG=flag;
		this.PID = pid;
		this.rPID = rpid;
	//	this.MessageSize=size;

	}
	
	public FTSLHeader(String sid, String flag, int rpid) {
		this.SID = sid;
		this.FLAG=flag;
		this.PID = 0;
		this.rPID = rpid;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
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
	
	public String toString_() {
		String sid = this.SID;
		String pid = String.valueOf(this.PID);
		String rpid = String.valueOf(this.rPID);

		String header = protocol+" "+sid + " " +this.FLAG+" "+ pid + " " + rpid;
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
		header.setrPID(Integer.valueOf(str.substring(index+1)));
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
