/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agentsimulation.Agents;

import agentsimulation.GUI.BoardState;
import agentsimulation.GUI.GUIMain;
import agentsimulation.GUI.States;
import agentsimulation.Messages.Die;
import agentsimulation.Messages.Message;
import agentsimulation.World;
import java.awt.Point;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 * @author Z98
 */
public class WolfSpider extends Agent {

    public WolfSpider(int x, int y)
    {
        super(x,y);
        BoardState.setState(x, y, States.SPIDER);
    }
    int hunger = 10;
    boolean stalking = false;
    @Override
    protected void Execute() {
        Patch p = World.patchMap.get(position);
        LinkedBlockingQueue<Agent> ants = p.GetAgents(Ant.class);
        Ant ant = null;
        if(ants != null){
        	ant = (Ant)ants.poll();
        }
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
        
        if(stalking)
        {
            Random nextPoint = new Random();
            int nextX = 0;
            int nextY = 0;
            while(nextX == 0 || nextX >= World.xDim || nextX == position.x)
            {
                nextX = nextPoint.nextInt(3);
                --nextX;
                nextX += position.x;
            }
            
            while(nextY == 0 || nextY >= World.yDim || nextY == position.y)
            {
                nextY = nextPoint.nextInt(3);
                --nextY;
                nextY += position.y;
            }
            
            this.Move(new Point(nextX,nextY));
            
            stalking = false;

            
            return;
        }
    }

    @Override
    protected void ExecuteMessage(Message message) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
