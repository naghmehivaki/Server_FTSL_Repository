package ftsl;

public class MessageProperties {

	int size=0;
	boolean eom=false;
	boolean reSent=false;

	///////////////////////////////////// Constructors
	public MessageProperties() {
	}
	public MessageProperties(int size) {
		this.size = size;
	}
	public MessageProperties(int size, boolean eom) {
		this.size = size;
		this.eom = eom;
	}
	public MessageProperties(int size, boolean eom, boolean reSent) {
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
			str=str+" "+eom;
		if (reSent)
			str=str+" "+reSent;
		
		return str;
		
	}
	
	public byte[] toBytes_(){
		return toString_().getBytes();
	}
	
	public static MessageProperties valueOf_(String str){
		MessageProperties mp=new MessageProperties();
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
	
	public MessageProperties valueOf_(byte[] b){
		return	valueOf_(new String(b));	
	}

}
