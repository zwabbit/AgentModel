/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agentsimulation;

import agentsimulation.Agents.Agent;
import agentsimulation.Agents.Patch;
import agentsimulation.GUI.GUIMain;
import agentsimulation.Messages.EnterPatch;
import agentsimulation.Messages.LeavePatch;
import agentsimulation.Messages.Message;
import agentsimulation.Spatial.Envelope;
import java.awt.Point;
import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Z98
 */
public class Dispatcher implements Runnable {
    
    public static LinkedBlockingQueue<Agent> agentList = null;
    private static LinkedBlockingQueue<Message> messageList = null;
    private static LinkedBlockingQueue<Message> currentMessageList = null;
    private final LinkedBlockingQueue<LinkedBlockingQueue<Message>> sortedMessages;
    private static Queue<Message> finalOperations;
    LinkedBlockingQueue<Agent> currentAgents;
    
    private HashMap<Point, Patch> patches;

    int cpuCount;
    
    CyclicBarrier startSignal;
    CyclicBarrier doneSignal;
    
    public Dispatcher(HashMap<Point, Patch> patchMap)
    {
        //cpuCount = Runtime.getRuntime().availableProcessors();
        cpuCount = 1;
        if(agentList == null) agentList = new LinkedBlockingQueue<>();
        if(messageList == null) messageList = new LinkedBlockingQueue<>();
        if(currentMessageList == null) currentMessageList = new LinkedBlockingQueue<>();
        sortedMessages = new LinkedBlockingQueue<>();
        if(finalOperations == null) finalOperations = new LinkedBlockingQueue<>();
        currentAgents = new LinkedBlockingQueue<>();
        startSignal = new CyclicBarrier(cpuCount + 1);
        doneSignal = new CyclicBarrier(cpuCount + 1);
        
        this.patches = patchMap;
    }

    @Override
    public void run() {
        for (int index = 0; index < cpuCount; index++) {
            Thread t = new Thread(new ExecutionThread(currentAgents, sortedMessages, startSignal, doneSignal));
            t.start();
        }
        HashMap<Integer, LinkedBlockingQueue<Message>> agentMessages;
        while (true) {
        	
            agentMessages = new HashMap<>();
            currentMessageList.clear();
            currentMessageList.addAll(messageList);
            messageList.clear();
            
            currentAgents.addAll(agentList);
            agentList.clear();
            

            /*
             * All this splitting of work could be done much more efficiently
             * with a countdown latch. Specifically, create a latch equal to
             * the number of threads, once each thread sees that the message/
             * agent queues are empty, decrement the latch and we'll wake up
             * again in this thread.
             */
            long startTime = 0;
            long endTime = 0;
            long runTime = 0;
            if(World.DEBUG)
            {
                startTime = System.currentTimeMillis();
            }
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
                    sortedMessages.addAll(agentMessages.values());
                }
                try {
                    long aTime = System.nanoTime();
                    startSignal.await();
                    doneSignal.await();
                    long eTime = System.nanoTime();
                    System.err.println("Time it took to process agents for iteration " + World.iteration + ": " + (eTime - aTime));
                } catch (InterruptedException | BrokenBarrierException ex) {
                    Logger.getLogger(Dispatcher.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            currentAgents.addAll(patches.values());
            try {
                long aTime = System.nanoTime();
                startSignal.await();
                doneSignal.await();
                long eTime = System.nanoTime();
            } catch (InterruptedException | BrokenBarrierException ex) {
                Logger.getLogger(Dispatcher.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            if(World.DEBUG)
            {
                endTime = System.currentTimeMillis();
                runTime = endTime - startTime;
                System.err.println("Time spent processing messages and executing agent logic: " + runTime + " milliseconds.");
            }
            
            ++World.iteration;

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
