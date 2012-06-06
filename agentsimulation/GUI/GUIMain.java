package agentsimulation.GUI;


import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import javax.swing.JPanel;

import agentsimulation.AgentSimulation;
import agentsimulation.AgentVariable;
import agentsimulation.Dispatcher;
import agentsimulation.World;
import agentsimulation.Agents.Agent;
import agentsimulation.Agents.Ant;

import agentsimulation.Agents.Patch;
import agentsimulation.Messages.UpdateGUI;

public class GUIMain {
	public static GUIBackground background;
	public static GUIControls controls;
	private static int xDim;
	private static int yDim;
	private static Patch infoPatch;
	private static int simSpeed;
	//private static HashMap<Class<? extends Agent>, HashMap<Integer, HashMap<String, Object>>> variablesMap;
	private static HashMap<Class<? extends Agent>, Integer> agentsMap;
	private static GUIPatchInfo patchInfo;
	private static boolean noGUI;
	private static boolean textOutput = false;
	//private static HashMap<Class <? extends Agent>,   

	public GUIMain(int x, int y, boolean ng){
		noGUI = ng;
		if(noGUI){
			AgentSimulation.startSim();
		}
		else{
			background = new GUIBackground(x, y);
			controls = new GUIControls();
			patchInfo = new GUIPatchInfo();
			//variablesMap = new HashMap<Class<? extends Agent>, HashMap<Integer, HashMap<String, Object>>>();
			agentsMap = new HashMap<Class<? extends Agent>, Integer>();
			infoPatch = World.patchMap.get(new Point(0,0));

			xDim = x;
			yDim = y;
			simSpeed = 1;
			System.out.flush();	

			drawNext();
			background.pack();
			background.setResizable(true);
			background.setLocationRelativeTo(null);
			background.setVisible(true);

			//guiTileDetails TD = new guiTileDetails();
			//TD.setVisible(true);


			controls.pack();
			controls.setResizable(true);

			controls.setLocation(new Point(background.getLocationOnScreen().x + background.getSize().width, background.getLocationOnScreen().y));
			controls.setVisible(true);

			patchInfo.pack();
			patchInfo.setResizable(true);
			patchInfo.setLocation(new Point(background.getLocationOnScreen().x - patchInfo.getSize().width, background.getLocationOnScreen().y));
			patchInfo.setVisible(true);
		}
	}

	public static void updateBoardState(Class<? extends Agent> c, HashMap<String, Object> vars){

		Point loc = (Point) vars.get("location");
		int id = (int) vars.get("id");
		States curr = BoardState.getState(loc.x, loc.y);


		if (c == Patch.class){
			//if(curr == States.NULL){

			if((int)vars.get("food") > 0){

				BoardState.setState(loc.x, loc.y, States.FOOD);
			}
			else{
				BoardState.setState(loc.x, loc.y, States.EMPTY);
			}
			//}
		}
		else if (c == Ant.class) {

			BoardState.setState(loc.x, loc.y, States.ANT);
		}
		else{
			BoardState.setState(loc.x, loc.y, States.OTHER);
		}

		//if(variablesMap.get(c) == null){
		//	variablesMap.put(c, new HashMap<Integer, HashMap<String, Object>>());
		//}
		//variablesMap.get(c).put(id, vars);

	}

	public static void setSimSpeed(int s){
		simSpeed = s;
	}

	public static void drawNext(){
		if(noGUI){
			if(textOutput){
				
				BoardState.printBoard();
			}
		}
		else{
			if (infoPatch != null){
				//System.out.println(infoPatch);
				Dispatcher.SendMessage(new UpdateGUI(infoPatch, null));
			}
			for(int i = 0;i<yDim;i++){
				for(int j=0;j<xDim;j++){
					//JPanel square = null;
					JPanel square = (JPanel)background.gameBoard.getComponent(i*yDim + j);
					//System.out.println(square + " ");
					switch(BoardState.getState(j, i)){
					case FOOD:
						square.setBackground(Color.GREEN);
						break;

					case SPIDER:
						square.setBackground(Color.yellow);
						break;

					case ANT:
						square.setBackground(Color.red);
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
		
			waitMillis(simSpeed * 20);
		}
		//clearAgents();
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

	public static void updateInfoPanel(HashMap<Class, HashMap<Integer, ArrayList<AgentVariable>>> vars) {
		// TODO Auto-generated method stub
		//System.out.println(vars.getLocation());
		String pString = new String();
		String aString = new String();
		for(Class c: vars.keySet()){
			if(c == Patch.class){
				pString = "Patch: ";
				for(AgentVariable v:vars.get(c).get(0)){
					pString = pString + "\n" + v.getName() + ": " + v.getStringVal();
				}
			}
			else{
				aString = "Agents: \n";
				for(Integer i:vars.get(c).keySet()){
					aString = c.getName() + " " + i + "\n ";
					for(AgentVariable v:vars.get(c).get(i)){
						aString = aString + "\n" + v.getName() + ": " + v.getStringVal();
					}
				}
			}
		}
		if(!aString.equalsIgnoreCase("")) GUIPatchInfo.updateAgentInfo(aString);
		GUIPatchInfo.updatePatchInfo(pString);
	}

	public static void setInfoPanelTarget(JPanel source) {
		// TODO Auto-generated method stub
		GUIPatchInfo.updateAgentInfo("");
		GUIPatchInfo.updatePatchInfo("");
		Point target = new Point(source.getLocation().x / (500 / xDim), source.getLocation().y / (500 / yDim));
		infoPatch = World.patchMap.get(target);
	}

	public static void waitMillis(int n){
		long t0, t1;
		t0 = System.currentTimeMillis();

		do{
			t1 = System.currentTimeMillis();
		}
		while(t1 - t0 < n);
	}
}
