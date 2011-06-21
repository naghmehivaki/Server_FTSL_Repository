package ftsl.FTTCP;


public interface AppInterface {
	
	public boolean isNewIncomingMessage(byte[] packet);
	public boolean isNewOutgoingMessage(byte[] packet);
	public String getSendMessageType();
	public String getRecieveMessageType();

	
	

}
