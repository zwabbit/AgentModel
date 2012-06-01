/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agentsimulation;

import agentsimulation.GUI.BoardState;
import agentsimulation.GUI.GUIMain;

/**
 *
 * @author Z98
 */
public class AgentSimulation {
	static Thread t;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    	//start world
    	//start dispatcher
    	
    	int x = 100, y = 100;
    	new BoardState(x,y);
    	World world = new World(x, y);
    	t = new Thread(new Dispatcher(World.patchMap));
    	world.initializeAnts(9);
    	world.initializeWolfSpiders(2);
    	new GUIMain(x, y);   	
    }
    
    public static void startSim(){
    	t.start();
    }
}
