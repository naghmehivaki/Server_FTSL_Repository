package ftsl;

public class MessageHandler {

	int messageID;
	MessageProperties properties;
	Session sessionRef;

	///////////////////////////////////// Constructors
	public MessageHandler() {
		properties=new MessageProperties();
	}
	public MessageHandler(Session s) {
		sessionRef=s;
	}
	public MessageHandler(MessageProperties p){
		this.properties=p;
	}
	public MessageHandler(int id,MessageProperties p){
		this.messageID=id;
		this.properties=p;
	}
	public MessageHandler(Session s,int id,MessageProperties p){
		this.sessionRef=s;
		this.messageID=id;
		this.properties=p;
	}
	///////////////////////////////////// Setters and getters
	
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
	public MessageProperties getProperties() {
		return properties;
	}
	public void setProperties(MessageProperties properties) {
		this.properties = properties;
	}
	//////////////////////////// Operations 
	

	public boolean isEom() {
		return properties.isEom();
	}
	public boolean isReSent() {
		return properties.isReSent();
	}
	
	public int getSize(){
		return properties.getSize();
	}
	
	//////////////////////////////////////////// Transaction Support
	
//	public void commit(){
//		sessionRef.commit(messageID);
//	}
//	public void abort(){
//		sessionRef.abort(messageID);
//	}
	public void acknowledge(){
		sessionRef.confirm(messageID);
	}

}
