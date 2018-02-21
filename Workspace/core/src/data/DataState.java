package data;

public class DataState {
		
	public final static int STAGNANT = 0;
	public final static int SERVER_UPDATED = 1;
	public final static int CLIENT_UPDATED = 2;
		
	private int state;
		
	public DataState(int state) {
		this.state = state;
	}
		
	public int getState() {
		return state;
	}
		
	public void setState(int state) {
		this.state = state;
	}
	
}
