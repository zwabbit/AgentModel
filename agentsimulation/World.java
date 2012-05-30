/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agentsimulation;

import agentsimulation.Agents.Ant;
import agentsimulation.Agents.Patch;
import agentsimulation.Agents.WolfSpider;
import agentsimulation.GUI.BoardState;
import agentsimulation.GUI.States;
import java.awt.Point;
import java.util.HashMap;
import java.util.Random;

/**
 *
 * @author Z98
 */
public class World {
    public static HashMap<Point, Patch> patchMap;
    public static HashMap<Integer, Ant> antList;
    public static boolean started = false;
    public static int xDim;
    public static int yDim;
    
    public World(int x, int y)
    {
        if(patchMap == null) patchMap = new HashMap<>();
        if(antList == null) antList = new HashMap<>();
        for(int yIndex = 0; yIndex < y; ++yIndex)
        {
            for(int xIndex = 0; xIndex < x; ++xIndex)
            {
                Patch p = new Patch(xIndex, yIndex);
                patchMap.put(p.GetPosition(), p);
                if(p.GetFood() > 0){
                	BoardState.setState(xIndex, yIndex, States.FOOD);
                }
                else{
                	BoardState.setState(xIndex, yIndex, States.EMPTY);
                }
            }
        }
        xDim = x;
        yDim = y;
    }
    
    public void initializeAnts(int num){
    	Random antRandom = new Random();
    	for(int i = 0;i<num;i++){
    		Ant newAnt = new Ant(antRandom.nextInt(xDim), antRandom.nextInt(yDim));
    		antList.put(newAnt.getID(),newAnt);
    	}
 //       System.out.println(num + " ants at " + antList.get(0).GetPosition());
        Dispatcher.agentList.addAll(antList.values());
    }
    
    public void initializeWolfSpiders(int num)
    {
        Random sRandom = new Random();
        for(int index = 0; index < num; index++)
        {
            WolfSpider spider = new WolfSpider(sRandom.nextInt(xDim), sRandom.nextInt(yDim));
            Dispatcher.agentList.add(spider);
        }
    }
    
    public void initializeAgent(Class agentClass, int num)
    {
        if(agentClass == Ant.class)
        {
            initializeAnts(num);
            return;
        }
        
        if(agentClass == WolfSpider.class)
        {
            return;
        }
    }
}
