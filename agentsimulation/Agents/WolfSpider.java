/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agentsimulation.Agents;

import agentsimulation.GUI.BoardState;
import agentsimulation.GUI.States;
import agentsimulation.Messages.Die;
import agentsimulation.Messages.Killer;
import agentsimulation.Messages.Message;
import agentsimulation.World;
import java.awt.Point;
import java.util.Random;

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
        }
        
        Patch p = World.patchMap.get(position);
        Ant ant = (Ant)p.GetOneOf(Ant.class);
        if(ant != null)
        {
            Die die = new Die(ant, this);
            this.SendMessage(die);
            stalking = false;
        }
    }

    @Override
    protected void ExecuteMessage(Message message) {
        //throw new UnsupportedOperationException("Not supported yet.");
        if(message instanceof Killer)
        {
            Killer killer = (Killer)message;
            if(killer.IsKiller())
            {
                hunger = 10;
                stalking = false;
            }
            else
            {
                if (hunger == 0) {
                    stalking = true;
                } else {
                    --hunger;
                }
            }
        }
    }
    
}
