/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agentsimulation;

import agentsimulation.Agents.Patch;
import java.awt.Point;
import java.util.HashMap;

/**
 *
 * @author Z98
 */
public class World {
    public static HashMap<Point, Patch> patchMap;
    
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
}
