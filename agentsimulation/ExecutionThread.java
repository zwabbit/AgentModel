/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agentsimulation;

import agentsimulation.Agents.Agent;
import agentsimulation.Messages.Message;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Z98
 */
public class ExecutionThread implements Runnable {

    private final LinkedBlockingQueue<Agent> agentQueue;
    private final LinkedBlockingQueue<LinkedBlockingQueue<Message>> messageQueue;
    private final CyclicBarrier startSignal;
    private final CyclicBarrier doneSignal;
    
    public ExecutionThread(LinkedBlockingQueue<Agent> aQueue, LinkedBlockingQueue<LinkedBlockingQueue<Message>> mQueue, CyclicBarrier startSignal, CyclicBarrier doneSignal)
    {
        this.agentQueue = aQueue;
        this.messageQueue = mQueue;
        this.startSignal = startSignal;
        this.doneSignal = doneSignal;
    }
    
    @Override
    public void run() {
        LinkedBlockingQueue<Message> messages;
        while(true)
        {
            try {
                startSignal.await();
                if (messageQueue != null) {
                    while(true)
                    {
                        long mStart = System.nanoTime();
                        messages = messageQueue.poll();
                        long mG = System.nanoTime();
                        if (messages == null) {
                            break;
                        }
                        for (Message message : messages) {
                            message.receivingAgent.SetNextMessage(message);
                        }
                        long mE = System.nanoTime();
                        /*System.out.println("Time to get message: " + (mG - mStart) +
                                "\nTime to process messages: " + (mE - mG) +
                                " for thread " + Thread.currentThread().getId());
                                */
                    }
                }

                while(true)
                {
                    if (agentQueue != null) {
                        long aStart = System.nanoTime();
                        Agent agent = agentQueue.poll();
                        long aGot = System.nanoTime();
                        if (agent == null) {
                            break;
                        }
                        agent.SetNext();
                        long aEnd = System.nanoTime();
                        
                        /*System.out.println("Time to get agent: " + (aGot - aStart) +
                                "\nTime to process: " + (aEnd - aGot) +
                                "\nfor thread: " + Thread.currentThread().getId());
                                 
                                */
                    }
                }
                
                doneSignal.await();
            } catch (InterruptedException ex) {
                //Logger.getLogger(ExecutionThread.class.getName()).log(Level.SEVERE, null, ex);
                System.err.println(ex.getMessage());
            } catch (BrokenBarrierException ex) {
                System.err.println(ex.getMessage());
            }
        }
    }
}
