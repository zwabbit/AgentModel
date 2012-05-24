/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agentsimulation.Agents;

import agentsimulation.Dispatcher;
import agentsimulation.Messages.EnterPatch;
import agentsimulation.Messages.LeavePatch;
import agentsimulation.Messages.Message;
import agentsimulation.World;
import java.awt.Point;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author Z98
 */
public abstract class Agent {
    public enum State { ALIVE, DEAD };
    
    protected State state = State.ALIVE;
    protected abstract void Execute();
    protected abstract void ExecuteMessage(Message message);
    
    protected Point position;
    
    private int agentID;
    
    private static AtomicInteger idIndex = null;
    
    protected Agent()
    {
        if(idIndex == null)
            idIndex = new AtomicInteger();
        
        agentID = idIndex.getAndIncrement();
    }
    
    protected Agent(int x, int y)
    {
        super();
        position = new Point(x, y);
    }
    
    public final Point GetPosition()
    {
        return position;
    }
    
    protected final void SendMessage(Message message)
    {
        Dispatcher.SendMessage(message);
    }
    
    public final State GetState()
    {
        return state;
    }
    
    public final int getID()
    {
        return agentID;
    }
    
    public final void SetNext()
    {
        if(this instanceof Patch)
        {
            Execute();
            return;
        }
        if(state == State.ALIVE)
        {
            Dispatcher.agentList.add(this);
            Execute();
        }
    }
    
    public final void SetNextMessage(Message message)
    {
        if(state == State.ALIVE)
        {
            ExecuteMessage(message);
        }
    }
    
    public final void Move(Point next)
    {
        Patch currentPatch = World.patchMap.get(position);
        Patch nextPatch = World.patchMap.get(next);
        position = next;
        EnterPatch enterPatch = new EnterPatch(nextPatch, this);
        LeavePatch leavePatch = new LeavePatch(currentPatch, this);
        
        Dispatcher.SendMessage(enterPatch);
        Dispatcher.SendMessage(leavePatch);
    }
    
    public final void Move(Patch nextPatch)
    {
        Patch currentPatch = World.patchMap.get(position);
        position = nextPatch.GetPosition();
        EnterPatch enterPatch = new EnterPatch(nextPatch, this);
        LeavePatch leavePatch = new LeavePatch(currentPatch, this);
        
        Dispatcher.SendMessage(enterPatch);
        Dispatcher.SendMessage(leavePatch);
    }
}
