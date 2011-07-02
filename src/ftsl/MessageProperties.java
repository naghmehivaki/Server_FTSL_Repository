package ftsl;

public class MessageProperties {
	
	boolean eom=false;
	boolean reSent=false;
	int size=0;
	public boolean isEom() {
		return eom;
	}
	public void setEom(boolean eom) {
		this.eom = eom;
	}
	public boolean isReSent() {
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
	
	

}
