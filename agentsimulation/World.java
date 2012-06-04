/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agentsimulation;

import agentsimulation.Agents.Agent;
import agentsimulation.Agents.Ant;
import agentsimulation.Agents.Patch;
import agentsimulation.Agents.WolfSpider;
import agentsimulation.GUI.BoardState;
import agentsimulation.GUI.States;
import agentsimulation.Spatial.Envelope;
import agentsimulation.Spatial.Quadtree;
import java.awt.Point;
import java.util.*;

/**
 *
 * @author Z98
 */
public class World {
    public static HashMap<Point, Patch> patchMap;
   //public static QuadTree<Integer, Patch> patchTree;
    public static Quadtree patchTree;
   // public static KDTreeC patchTree;
    public static HashMap<Integer, Ant> antList;
    public static HashMap<Class<? extends Agent>, Quadtree> agentTrees;
    public static boolean started = false;
    public static int xDim;
    public static int yDim;
    
    public World(int x, int y)
    {
        if(patchMap == null) patchMap = new HashMap<>();
        if(antList == null) antList = new HashMap<>();
        if(patchTree == null) patchTree = new Quadtree();
        //if(patchTree == null) patchTree = new KDTreeC(2);
        if(agentTrees == null) agentTrees = new HashMap<>();
        for(int yIndex = 0; yIndex < y; ++yIndex)
        {
            for(int xIndex = 0; xIndex < x; ++xIndex)
            {
                Patch p = new Patch(xIndex, yIndex);
                patchMap.put(p.GetPosition(), p);
            
                patchTree.insert(new Envelope(xIndex, yIndex, xIndex + 1, yIndex + 1), p);
                            
                /*
                double[] loc = new double[2];
                loc[0] = xIndex;
                loc[1] = yIndex;
                patchTree.add(loc, p);
                */
                
                
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
    	Quadtree antTree = new Quadtree();
    	agentTrees.put(Ant.class, antTree);
    	for(int i = 0;i<num;i++){
    		int x = antRandom.nextInt(xDim);
    		int y = antRandom.nextInt(yDim);
    		Ant newAnt = new Ant(x, y);
    		antTree.insert(new Envelope(x, x+1, y, y+1), newAnt);
    		antList.put(newAnt.getID(), newAnt);
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
    
    public static ArrayList<Agent> agentsInRadiusGreaterThanE(int x, int y, Class<? extends Agent> type, int r, int level, String attribute)
    {
        ArrayList<Agent> agents = agentsInRadius(x, y, type, r);
        ArrayList<Agent> matches = new ArrayList<>();
        for(Agent a : agents)
        {
            if(a.GetAttribute(attribute) >= level)
            {
                matches.add(a);
            }
        }
        
        return matches;
    }
    
    public static ArrayList<Agent> agentsInRadiusLessThanE(int x, int y, Class<? extends Agent> type, int r, int level, String attribute)
    {
        ArrayList<Agent> agents = agentsInRadius(x, y, type, r);
        ArrayList<Agent> matches = new ArrayList<>();
        for(Agent a : agents)
        {
            if(a.GetAttribute(attribute) <= level)
            {
                matches.add(a);
            }
        }
        
        return matches;
    }
    
    public static ArrayList<Agent> agentsInRadiusCompare(int x, int y, Class<? extends Agent> type, int r, Comparator<Agent> comparator, Agent baseline)
    {
        ArrayList<Agent> agents = agentsInRadius(x, y, type, r);
        ArrayList<Agent> matches = new ArrayList<>();
        for(Agent a : agents)
        {
            if(comparator.compare(a, baseline) >= 0)
            {
                matches.add(a);
            }
        }
        
        return matches;
    }
    
    public static ArrayList<Agent> agentsInRadius(int x, int y, Class<? extends Agent> type, int r){
    	ArrayList<Agent> L = new ArrayList<>();
    	Quadtree thisTree = agentTrees.get(type);
    	List<Agent> rectangle = thisTree.query(new Envelope(x-r,x+r,y-r,y+r));
    	for(Agent a:rectangle){
    		//if(Math.abs(x - a.GetPosition().x) <= r && Math.abs(y - a.GetPosition().y) <= r){
    		if(Point.distance(x, y, a.GetPosition().x, a.GetPosition().y) <= r){
                    L.add(a);
    		}
    		//}
    	}
    	/*
    	for(Patch p:patches){
    		if(p.GetAgents(type) != null){
    			Iterator<Agent> iter = p.GetAgents(type).iterator();
    			while(iter.hasNext()){
    				L.add(iter.next());
    			}
    		}
    	}*/
    	return L;
    }
    
    public static List<Patch> patchesInRadius(Patch p, int r){
    	int pX = p.GetPosition().x;
    	int pY = p.GetPosition().y;
    	return patchesInRadius(pX, pY, r);
    }
    
    public static List<Patch> patchesInRadius(int pX, int pY, int r){
    	List<Patch> L = new ArrayList<>();
   	
    
    	List<Patch> rets = patchTree.query(new Envelope(pX - r, pX + r, pY - r, pY + r));
    	for(Patch pat:rets){
    		if(Point.distance(pX, pY, pat.GetPosition().x, pat.GetPosition().y) <=r){
    			L.add(pat);
    		}
    	}
    	    	
    	
    	/*double[] myLoc = new double[2];
    	myLoc[0] = p.GetPosition().x;
    	myLoc[1] = p.GetPosition().y;
    	double[] endLoc = new double[2];
    	double[] startLoc = new double[2];
    	startLoc[0] = myLoc[0] - r;
    	startLoc[1] = myLoc[1] - r;
    	endLoc[0] = myLoc[0] + r;
    	endLoc[1] = myLoc[1] + r;
    	for(int i=0;i<2;i++){
    		if(startLoc[i] < 0) {
    			startLoc[i] = 0;
    		}
    	}
    	if(endLoc[0] >= xDim){
			endLoc[0] = xDim - 1;
		}
    	if(endLoc[1] >= yDim){
			endLoc[1] = yDim - 1;
		}
    	KDTreeC.Item[] patches = patchTree.getRange(startLoc, endLoc);
    	for(int i = 0;i<patches.length;i++){
    		KDTreeC.Item it = patches[i];
    		double[] locArray = it.pnt;
    		if(Math.abs(myLoc[0] - locArray[0]) > r && Math.abs(myLoc[1] - locArray[1]) > r){
    			L.add((Patch)it.obj);
    		}
    	}*/
    	return L;
    }
}
