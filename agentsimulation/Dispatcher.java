/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agentsimulation;

import agentsimulation.Agents.Agent;
import agentsimulation.Agents.Patch;
import agentsimulation.Messages.Message;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
    
    private HashMap<Point, Patch> patches;
    
    ExecutorService taskExecutor;
    int cpuCount;
    
    public Dispatcher(HashMap<Point, Patch> patchMap)
    {
        cpuCount = Runtime.getRuntime().availableProcessors();
        if(agentList == null) agentList = new LinkedBlockingQueue<>();
        if(messageList == null) messageList = new LinkedBlockingQueue<>();
        
        this.patches = patchMap;
    }

    @Override
    public void run() {
        HashMap<Integer, LinkedBlockingQueue<Message>> agentMessages;
        while (true) {
            agentMessages = new HashMap<>();

            currentMessageList = messageList;
            messageList = new LinkedBlockingQueue<>();

            /*
             * All this splitting of work could be done much more efficiently
             * with a countdown latch. Specifically, create a latch equal to
             * the number of threads, once each thread sees that the message/
             * agent queues are empty, decrement the latch and we'll wake up
             * again in this thread.
             */
            while (!currentMessageList.isEmpty()) {
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
                for(int index = 0; index < cpuCount; index++)
                {
                    taskExecutor.execute(new ExecutionThread(agentList, messages));
                }

                taskExecutor.shutdown();
                while (!taskExecutor.isTerminated()) {}
            }

            taskExecutor = Executors.newFixedThreadPool(cpuCount);
            LinkedBlockingQueue<Patch> patchList = new LinkedBlockingQueue<>(patches.values());
            for(int index = 0; index < cpuCount; index++) {
                taskExecutor.execute(new PatchExecutionThread(patchList));
            }

            taskExecutor.shutdown();
            while (!taskExecutor.isTerminated()) {}
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
}
