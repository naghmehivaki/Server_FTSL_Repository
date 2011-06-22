package ftsl;

public class MessageInfo {
	
	int start=0;
	int end=0;
	int index=0;
	int id=0;
	
	
	public int getStart() {
		return start;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getEnd() {
		return end;
	}
	public void setEnd(int end) {
		this.end = end;
	}
	public int getRequestID() {
		return id;
	}
	public void setRequestID(int requestID) {
		this.id = requestID;
	}
	
	public String toString_(){
		String str="";
		str=str+String.valueOf(start)+" ";
		str=str+String.valueOf(end)+" ";
		str=str+String.valueOf(index)+" ";
		str=str+String.valueOf(id);
		return str;
	}
	public static MessageInfo valueOf_(String str){
		MessageInfo info=new MessageInfo();
		int index=str.indexOf(" ");
		info.setStart(Integer.valueOf(str.substring(0,index)));
		str=str.substring(index+1);
		index=str.indexOf(" ");
		info.setEnd(Integer.valueOf(str.substring(0,index)));
		str=str.substring(index+1);
		index=str.indexOf(" ");
		info.setIndex(Integer.valueOf(str.substring(0,index)));
		info.setId(Integer.valueOf(str.substring(index+1)));

		return info;
	}
	
	
	

}
