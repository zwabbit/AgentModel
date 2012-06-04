/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agentsimulation.Agents;

import agentsimulation.AgentVariable;
import agentsimulation.Dispatcher;
import agentsimulation.GUI.BoardState;
import agentsimulation.GUI.GUIMain;
import agentsimulation.GUI.States;
import agentsimulation.Messages.EnterPatch;
import agentsimulation.Messages.LeavePatch;
import agentsimulation.Messages.Message;
import agentsimulation.Messages.UpdateGUI;
import agentsimulation.World;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;
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
    private static int growthInterval;
    private static Random patchRandom =  null;
    
    public Patch(int x, int y)
    {
        super(x,y);
        if(patchRandom == null)
            patchRandom = new Random();
        if(presentAgents == null) presentAgents = new HashMap<Class, HashMap<Integer, Agent>>();
        food = new AtomicInteger(patchRandom.nextInt(MAX_FOOD));
        growthInterval = World.patchMap.entrySet().size() * 2;
    }
    
    @Override 
    protected void Execute() {
        if(patchRandom.nextInt(growthInterval) == 0) food.addAndGet(patchRandom.nextInt(MAX_GROW));
        updateGUI();
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
            Dispatcher.addFinalOperation(message);
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
            Dispatcher.addFinalOperation(message);
        }
        if(message instanceof UpdateGUI){
        	HashMap<Class, HashMap<Integer, ArrayList<AgentVariable>>> vars = new HashMap<Class, HashMap<Integer, ArrayList<AgentVariable>>>();
        	for(Class c : presentAgents.keySet()){
        		HashMap<Integer, ArrayList<AgentVariable>> curr = new HashMap<Integer, ArrayList<AgentVariable>>();
        		for(Integer i:presentAgents.get(c).keySet()){
        			Agent a = presentAgents.get(c).get(i);
        			ArrayList<AgentVariable> theseVars = new ArrayList<AgentVariable>();
        			theseVars.add(new AgentVariable("ID", a.getID()));
        			theseVars.add(new AgentVariable("position", a.GetPosition()));
        			if(c == Ant.class){
        				theseVars.add(new AgentVariable("foodCarrying", ((Ant)a).foodCarrying));
        			}
        			if(c == WolfSpider.class){
        				
        			}
        			curr.put(i, theseVars);
        		}
        		vars.put(c, curr);
        	}
        	HashMap<Integer, ArrayList<AgentVariable>> curr = new HashMap<Integer, ArrayList<AgentVariable>>();
        	ArrayList<AgentVariable> patchVars = new ArrayList<AgentVariable>();
        	patchVars.add(new AgentVariable("ID", this.getID()));
			patchVars.add(new AgentVariable("position", this.GetPosition()));
			patchVars.add(new AgentVariable("food", this.GetFood()));
			curr.put(0, patchVars);
			vars.put(Patch.class, curr);
			GUIMain.updateInfoPanel(vars);
        }
        
        updateGUI();
    }
    
    public int Eat(int amount)
    {
    	int last = food.get();
    	int got = food.addAndGet(-amount);
        if(got < 0){
        	food.set(0);
        	return last;
        }
    	return amount;
    }
    
    public LinkedBlockingQueue<Agent> GetAgents(Class agentClass)
    {
    	LinkedBlockingQueue<Agent> list = null;
    	if(presentAgents.get(agentClass) != null){
    		list = new LinkedBlockingQueue<Agent>(presentAgents.get(agentClass).values());
        }       
        return list;
    }
    
    public Agent GetOneOf(Class agentClass)
    {
        Agent agent;
        if(presentAgents.get(agentClass) != null)
        {
            try {
                agent = presentAgents.get(agentClass).values().iterator().next();
                return agent;
            }
            catch(NoSuchElementException nse)
            {
                return null;
            }
        }
        
        return null;
    }
    
    public int GetFood()
    {
        return food.intValue();
    }

	protected void updateGUI() {
		States set = States.NULL;
		if(presentAgents.get(WolfSpider.class) != null){
			set = States.SPIDER;
		}
		else if (presentAgents.get(Ant.class) != null){
			set = States.ANT;
		}
		else if (GetFood() > 0){
			set = States.FOOD;
		}
		else {
			set = States.EMPTY;
		}
		BoardState.setState(GetPosition().x, GetPosition().y, set);
		//GUIMain.updateBoardState(this.getClass(), vars);
	}
}
