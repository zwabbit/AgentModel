/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agentsimulation.Agents;

import agentsimulation.Dispatcher;
import agentsimulation.GUI.GUIMain;
import agentsimulation.Messages.Die;
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
    protected abstract void updateGUI();
    
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
        this();
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
    	if(this.getClass() != Patch.class){

    	}
        if(this instanceof Patch)
        {
            Execute();
            return;
        }
        if(state.equals(State.ALIVE) && this.getClass() != Patch.class)
        {

            Dispatcher.agentList.add(this);
            Execute();
        }
    }
    
    public final void SetNextMessage(Message message)
    {
        if(state == State.ALIVE)
        {
            if(message instanceof Die)
            {
                state = State.DEAD;
                Patch p = World.patchMap.get(position);
                LeavePatch leave = new LeavePatch(p, this);
                SendMessage(leave);
                return;
            }
            ExecuteMessage(message);
        }
    }
    
    public final boolean Move(Point next)
    {
    	Patch currentPatch = World.patchMap.get(position);
        Patch nextPatch = World.patchMap.get(next);
        if(nextPatch == null)
            return false;
        position = next;
        EnterPatch enterPatch = new EnterPatch(nextPatch, this);
        LeavePatch leavePatch = new LeavePatch(currentPatch, this);
        
        Dispatcher.SendMessage(enterPatch);
        Dispatcher.SendMessage(leavePatch);
        
        return true;
    }
    
    public final boolean Move(Patch nextPatch)
    {
        if(nextPatch == null)
            return false;
        Patch currentPatch = World.patchMap.get(position);
        position = nextPatch.GetPosition();
        EnterPatch enterPatch = new EnterPatch(nextPatch, this);
        LeavePatch leavePatch = new LeavePatch(currentPatch, this);
        
        Dispatcher.SendMessage(enterPatch);
        Dispatcher.SendMessage(leavePatch);
        
        return true;
    }
    
}
