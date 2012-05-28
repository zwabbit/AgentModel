package agentsimulation.GUI;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReferenceArray;

public class BoardState {
	
	protected static AtomicReferenceArray<AtomicReferenceArray<States>> rows;
	
	public BoardState(int x, int y){
		rows = new AtomicReferenceArray<AtomicReferenceArray<States>>(y);
		for(int i=0;i<y;i++){
			rows.set(i, new AtomicReferenceArray<States>(x));
		}
	}
	
	public static States getState(int x, int y){
		return rows.get(y).get(x);
	}
	
	public static void setState(int x, int y, States s){
		rows.get(y).set(x, s);
	}
	
}
