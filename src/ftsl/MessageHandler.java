package ftsl;

public class MessageHandler {

	int messageID;
	int size=0;
	boolean eom=false;
	boolean reSent=false;
	Session sessionRef;

	///////////////////////////////////// Constructors
	public MessageHandler() {
	}
	public MessageHandler(Session s) {
		sessionRef=s;
	}
	public MessageHandler(int size) {
		this.size = size;
	}
	public MessageHandler(int size, boolean eom) {
		this.size = size;
		this.eom = eom;
	}
	public MessageHandler(int size, boolean eom, boolean reSent) {
		this.size = size;
		this.eom = eom;
		this.reSent = reSent;
	}
	
	///////////////////////////////////// Setters and getters
	public boolean getEom(){
		return eom;
	}
	public void setEom(boolean eom) {
		this.eom = eom;
	}
	public boolean getReSent(){
		return reSent;
	}
	public void setReSent(boolean reSent) {
		this.reSent = reSent;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public int getMessageID() {
		return messageID;
	}
	public void setMessageID(int messageID) {
		this.messageID = messageID;
	}
	public Session getSessionRef() {
		return sessionRef;
	}
	public void setSessionRef(Session sessionRef) {
		this.sessionRef = sessionRef;
	}
	
	//////////////////////////// Operations 
	
	public boolean isEom() {
		return eom;
	}
	public boolean isReSent() {
		return reSent;
	}
	
	public String toString_(){
		String str="";
		str=String.valueOf(size);
		if (eom)
			str=str+" "+"1";
		if (reSent)
			str=str+" "+"1";
		
		return str;
		
	}
	
	public byte[] toBytes_(){
		return toString_().getBytes();
	}
	
	public static MessageHandler valueOf_(String str){
		MessageHandler mp=new MessageHandler();
		int index=str.indexOf(" ");
		if (index!=-1){
			mp.setSize(Integer.valueOf(str.substring(0,index)));
			mp.setEom(true);
			str=str.substring(index+1);
			index=str.indexOf(" ");
			if (index!=-1){
				mp.setReSent(true);
			}
		}
		else {
			mp.setSize(Integer.valueOf(str));
		}
		
		return mp;
	}
	
	public MessageHandler valueOf_(byte[] b){
		return	valueOf_(new String(b));	
	}
	
	//////////////////////////////////////////// Transaction Support
	
	public void commit(){
		sessionRef.commit(messageID);
	}
	public void abort(){
		sessionRef.abort(messageID);
	}
	public void confirm(){
		sessionRef.confirm(messageID);
	}

}
