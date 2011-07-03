package ftsl;

public class MessageProperties {

	int size=0;
	boolean eom=false;
	boolean reSent=false;

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
	
	public MessageProperties valueOf_(String str){
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
