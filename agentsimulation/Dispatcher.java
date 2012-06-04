/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agentsimulation;

import agentsimulation.Agents.Agent;
import agentsimulation.Agents.Ant;
import agentsimulation.Agents.Patch;
import agentsimulation.Agents.WolfSpider;
import agentsimulation.GUI.GUIMain;
import agentsimulation.GUI.GUIPatchInfo;
import agentsimulation.Messages.EnterPatch;
import agentsimulation.Messages.LeavePatch;
import agentsimulation.Messages.Message;
import agentsimulation.Spatial.Envelope;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 * @author Z98
 */
public class Dispatcher implements Runnable {
    
    public static LinkedBlockingQueue<Agent> agentList;
    private static LinkedBlockingQueue<Message> messageList;
    private static LinkedBlockingQueue<Message> currentMessageList;
    private static Queue<Message> finalOperations;
    
    private HashMap<Point, Patch> patches;
    
    ExecutorService taskExecutor;
    int cpuCount;
    
    public Dispatcher(HashMap<Point, Patch> patchMap)
    {
        cpuCount = Runtime.getRuntime().availableProcessors();
        if(agentList == null) agentList = new LinkedBlockingQueue<>();
        if(messageList == null) messageList = new LinkedBlockingQueue<>();
        if(currentMessageList == null) currentMessageList = new LinkedBlockingQueue<>();
        if(finalOperations == null) finalOperations = new LinkedBlockingQueue<Message>();
        
        this.patches = patchMap;
    }

    @Override
    public void run() {
        HashMap<Integer, LinkedBlockingQueue<Message>> agentMessages;
        while (true) {
        	
            agentMessages = new HashMap<>();
            currentMessageList.clear();
            currentMessageList.addAll(messageList);
            messageList.clear();
            
            LinkedBlockingQueue<Agent> currentAgents = new LinkedBlockingQueue<>(agentList);
            agentList.clear();
            

            /*
             * All this splitting of work could be done much more efficiently
             * with a countdown latch. Specifically, create a latch equal to
             * the number of threads, once each thread sees that the message/
             * agent queues are empty, decrement the latch and we'll wake up
             * again in this thread.
             */
            while (!currentMessageList.isEmpty() || !currentAgents.isEmpty()) {
                
                if(!currentMessageList.isEmpty())
                {
                    for (Message message : currentMessageList) {
                        LinkedBlockingQueue<Message> mQueue = agentMessages.get(message.receivingAgent.getID());
                        if (mQueue == null) {
                            mQueue = new LinkedBlockingQueue<>();
                            agentMessages.put(message.receivingAgent.getID(), mQueue);
                        }
                        mQueue.add(message);
                    }

                    currentMessageList.clear();

                    LinkedBlockingQueue<LinkedBlockingQueue<Message>> messages = new LinkedBlockingQueue<>(agentMessages.values());
                    
                    taskExecutor = Executors.newFixedThreadPool(cpuCount);
                    for (int index = 0; index < cpuCount; index++) {
                        taskExecutor.execute(new ExecutionThread(null, messages));
                    }

                    taskExecutor.shutdown();
                    while (!taskExecutor.isTerminated()) {
                    }
                }
                
                if(!currentAgents.isEmpty())
                {
                    taskExecutor = Executors.newFixedThreadPool(cpuCount);
                    for (int index = 0; index < cpuCount; index++) {
                        taskExecutor.execute(new ExecutionThread(currentAgents, null));
                    }
                    
                    taskExecutor.shutdown();
                    while (!taskExecutor.isTerminated()) {
                    }
                }
            }

            taskExecutor = Executors.newFixedThreadPool(cpuCount);
            LinkedBlockingQueue<Patch> patchList = new LinkedBlockingQueue<>(patches.values());
            for(int index = 0; index < cpuCount; index++) {
                taskExecutor.execute(new PatchExecutionThread(patchList));
            }
            GUIPatchInfo.updateAgentInfo(World.agentsInRadius(50, 50, Ant.class, 3).toString());

            taskExecutor.shutdown();
            while (!taskExecutor.isTerminated()) {}
            executeFinals();
            GUIMain.drawNext();
        }
    }
    
    public static void SendMessage(Message message)
    {
        if(message.GetPriority() == Message.Priority.HIGH)
        {
            currentMessageList.add(message);
        }
        else
        {
            messageList.add(message);
        }
    }
    
    public static void addFinalOperation(Message m){
    	finalOperations.add(m);
    }
    
    private static void executeFinals(){
    	HashMap<Agent, Point> enter = new HashMap<>();
		HashMap<Agent, Point> leave = new HashMap<>();
    	while(!finalOperations.isEmpty()){
    		Message current = finalOperations.poll();
    		
    		if(current instanceof EnterPatch){
    			EnterPatch en = (EnterPatch)current;
    			enter.put(en.sendingAgent, en.receivingAgent.GetPosition());
    		}
    		else if(current instanceof LeavePatch){
    			leave.put(((LeavePatch)current).sendingAgent, ((LeavePatch)current).receivingAgent.GetPosition());
    		}
    		else{
    			throw new UnsupportedOperationException("Final not supported yet.");
    		}
    	}
    	
    	for(Agent l:leave.keySet()){
    		Point loc = leave.get(l);
    		World.agentTrees.get(l.getClass()).remove(new Envelope(loc.x, loc.x+1, loc.y, loc.y+1), l);
    	}
    	for(Agent e:enter.keySet()){
    		Point loc = enter.get(e);
    		World.agentTrees.get(e.getClass()).insert(new Envelope(loc.x, loc.x+1, loc.y, loc.y+1), e);
    	}
    }
}
