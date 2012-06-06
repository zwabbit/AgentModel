package agentsimulation.GUI;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReferenceArray;

public class BoardState {
	
	protected static AtomicReferenceArray<AtomicReferenceArray<States>> rows;
	private static int xDim;
	private static int yDim;
	
	public BoardState(int x, int y){
		yDim = y;
		xDim = x;
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
	
	public static void printBoard(){

		for(int y = 0;y<yDim;y++){
			for(int x=0;x<xDim;x++){
				System.out.print(rows.get(y).get(x).toString().charAt(0) + " ");
			}
			System.out.println();
		}
		System.out.println();
	}
	
}
