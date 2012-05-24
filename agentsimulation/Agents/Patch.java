/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agentsimulation.Agents;

import agentsimulation.Messages.EnterPatch;
import agentsimulation.Messages.LeavePatch;
import agentsimulation.Messages.Message;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author Z98
 */
public class Patch extends Agent {
    HashMap<Class, HashMap<Integer, Agent>> presentAgents;
    AtomicInteger food;
    
    public static int MAX_FOOD = 50;
    public static int MAX_GROW = 5;
    private static Random patchRandom =  null;
    
    public Patch(int x, int y)
    {
        super(x,y);
        if(patchRandom == null)
            patchRandom = new Random();
        food = new AtomicInteger(patchRandom.nextInt(MAX_FOOD));
    }
    
    @Override
    protected void Execute() {
        food.addAndGet(MAX_GROW);
    }

    @Override
    protected void ExecuteMessage(Message message) {
        //throw new UnsupportedOperationException("Not supported yet.");
        if(message instanceof EnterPatch)
        {
            EnterPatch enter = (EnterPatch)message;
            HashMap<Integer, Agent> agents = presentAgents.get(enter.sendingAgent.getClass());
            if(agents == null)
            {
                agents = new HashMap<>();
                presentAgents.put(enter.sendingAgent.getClass(), agents);
            }
            
            agents.put(enter.sendingAgent.getID(), enter.sendingAgent);
        }
        
        if(message instanceof LeavePatch)
        {
            LeavePatch leave = (LeavePatch)message;
            HashMap<Integer, Agent> agents = presentAgents.get(leave.sendingAgent.getClass());
            if(agents != null)
            {
                agents.remove(leave.sendingAgent.getID());
                if(agents.isEmpty())
                {
                    presentAgents.remove(leave.sendingAgent.getClass());
                }
            }
        }
    }
    
    public int Eat(int amount)
    {
        return food.addAndGet(-amount);
    }
    
    public LinkedBlockingQueue<Agent> GetAgents(Class agentClass)
    {
        LinkedBlockingQueue<Agent> list = (LinkedBlockingQueue<Agent>)presentAgents.get(agentClass).clone();
        
        return list;
    }
    
    public int GetFood()
    {
        return food.intValue();
    }
}
