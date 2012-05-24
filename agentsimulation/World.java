/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agentsimulation;

import agentsimulation.Agents.Ant;
import agentsimulation.Agents.Patch;
import java.awt.Point;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Z98
 */
public class World {
    public static HashMap<Point, Patch> patchMap;
    public static List<Ant> antList;
    protected int xDim;
    protected int yDim;
    
    public World(int x, int y)
    {
        if(patchMap == null) patchMap = new HashMap<>();
        for(int yIndex = 0; yIndex < y; ++yIndex)
        {
            for(int xIndex = 0; xIndex < x; ++xIndex)
            {
                Patch p = new Patch(xIndex, yIndex);
                patchMap.put(p.GetPosition(), p);
            }
        }
    }
    
    public void initializeAnts(int num){
    	Random antRandom = new Random();
    	for(int i = 0;i<num;i++){
    		Ant newAnt = new Ant(antRandom.nextInt(xDim), antRandom.nextInt(yDim));
    		antList.add(newAnt);
    	}
    }
}
