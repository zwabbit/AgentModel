/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agentsimulation.Agents;

import agentsimulation.Messages.Die;
import agentsimulation.Messages.Message;
import agentsimulation.World;
import java.awt.Point;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 * @author Z98
 */
public class WolfSpider extends Agent {

    int hunger = 10;
    boolean stalking = false;
    @Override
    protected void Execute() {
        if(stalking)
        {
            Random nextPoint = new Random();
            int nextX = 0;
            int nextY = 0;
            while(nextX == 0 || nextX + position.x >= World.xDim || nextX == position.x)
            {
                nextX = nextPoint.nextInt(3);
                --nextX;
                nextX += position.x;
            }
            
            while(nextY < 0 || nextY >= World.xDim || nextY == position.y)
            {
                nextY = nextPoint.nextInt(3);
                --nextY;
                nextY += position.y;
            }
            
            this.Move(new Point(nextX,nextY));
            
            return;
        }
        Patch p = World.patchMap.get(position);
        LinkedBlockingQueue<Agent> ants = p.GetAgents(Ant.class);
        Ant ant = (Ant)ants.poll();
        if(ant != null)
        {
            Die die = new Die(ant, this);
            this.SendMessage(die);
            hunger = 10;
        }
        else
        {
            if(hunger == 0)
            {
                stalking = true;
            }
            else
            {
                --hunger;
            }
        }
    }

    @Override
    protected void ExecuteMessage(Message message) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void updateGUI() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}