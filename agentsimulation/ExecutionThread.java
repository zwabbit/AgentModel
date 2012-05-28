/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agentsimulation;

import agentsimulation.Agents.Agent;
import agentsimulation.Messages.Message;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 * @author Z98
 */
public class ExecutionThread implements Runnable {

    private LinkedBlockingQueue<Agent> agentQueue = null;
    private LinkedBlockingQueue<LinkedBlockingQueue<Message>> messageQueue = null;
    
    public ExecutionThread(LinkedBlockingQueue<Agent> aQueue, LinkedBlockingQueue<LinkedBlockingQueue<Message>> mQueue)
    {
        this.agentQueue = aQueue;
        this.messageQueue = mQueue;
    }
    @Override
    public void run() {
        LinkedBlockingQueue<Message> messages;
        while(true)
        {
            messages = messageQueue.poll();
            if(messages == null)
                break;
            for(Message message : messages)
                message.receivingAgent.SetNextMessage(message);
        }
        
        while(true)
        {
        	
            Agent agent = agentQueue.poll();
            if(agent == null)
                break;
            agent.SetNext();
        }
    }
    
}
