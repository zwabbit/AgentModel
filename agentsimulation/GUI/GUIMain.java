package agentsimulation.GUI;


import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.util.HashMap;
import java.util.Scanner;

import javax.swing.JPanel;

import agentsimulation.World;
import agentsimulation.Agents.Agent;
import agentsimulation.Agents.Ant;
import agentsimulation.Agents.Patch;

public class GUIMain {
	public static GUIBackground background;
	public static GUIControls controls;
	private static int xDim;
	private static int yDim;
	private static HashMap<Class<? extends Agent>, HashMap<Integer, HashMap<String, Object>>> variablesMap;
	
	public GUIMain(int x, int y){
		background = new GUIBackground(x, y);
		controls = new GUIControls();
		variablesMap = new HashMap<Class<? extends Agent>, HashMap<Integer, HashMap<String, Object>>>();
		xDim = x;
		yDim = y;
		System.out.flush();	
	
		background.pack();
		background.setResizable(true);
		background.setLocationRelativeTo(null);
		background.setVisible(true);
		
		//guiTileDetails TD = new guiTileDetails();
		//TD.setVisible(true);
		
		
		controls.pack();
		controls.setResizable(true);
		controls.setLocationRelativeTo(null);
		controls.setVisible(true);
	}
	
	public static void updateBoardState(Class<? extends Agent> c, HashMap<String, Object> vars){
		
		Point loc = (Point) vars.get("location");
		int id = (int) vars.get("id");
		States curr = BoardState.getState(loc.x, loc.y);
		
		
		if (c == Patch.class){
			if(curr == States.NULL){
				//System.out.println("updat p");
				if((int)vars.get("food") > 0){
					
					BoardState.setState(loc.x, loc.y, States.FOOD);
				}
				else{
					BoardState.setState(loc.x, loc.y, States.EMPTY);
				}
			}
		}
		else if (c == Ant.class) {
			//System.out.println(vars);
			BoardState.setState(loc.x, loc.y, States.ANT);
		}
		else{
			BoardState.setState(loc.x, loc.y, States.OTHER);
		}
		
		if(variablesMap.get(c) == null){
			variablesMap.put(c, new HashMap<Integer, HashMap<String, Object>>());
		}
		variablesMap.get(c).put(id, vars);
		
	}
	
	public static void drawNext(){
	
		for(int i = 0;i<yDim;i++){
			for(int j=0;j<xDim;j++){
				//JPanel square = null;
				JPanel square = (JPanel)background.gameBoard.getComponent(i*yDim + j);
				//System.out.println(square + " ");
				switch(BoardState.getState(j, i)){
					case FOOD:
						square.setBackground(Color.GREEN);
					break;
					
					case ANT:
						square.setBackground(Color.RED);
					break;
					
					case EMPTY:
						square.setBackground(Color.gray);
					break;
					
					case NULL:
						square.setBackground(Color.black);
					break;
					
					case OTHER:
						square.setBackground(Color.ORANGE);
					break;
					default:
						square.setBackground(Color.blue);
					break;
				}
				//System.out.println(background.getComponentAt(j, i) + "\t");
				//System.out.print(BoardState.getState(j, i) +"\t");
			}
			//System.out.println();
		}
		clearAgents();
		//System.out.println();
	}
	
	public static void clearAgents(){
		for(int i = 0;i<yDim;i++){
			for(int j=0;j<xDim;j++){
				if(BoardState.getState(j, i) == States.ANT) {
					BoardState.setState(j, i, States.EMPTY);
				}
			}
		}
	}
}
