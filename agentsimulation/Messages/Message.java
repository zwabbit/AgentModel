/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agentsimulation.Messages;

import agentsimulation.Agents.Agent;

/**
 *
 * @author Z98
 */
public abstract class Message {
    public enum Priority { HIGH, MEDIUM, LOW };
    public Agent receivingAgent;
    public Agent sendingAgent;
    
    protected Priority messagePriority = Priority.MEDIUM;
    
    public Message(Agent rAgent, Agent sAgent)
    {
        this.receivingAgent = rAgent;
        this.sendingAgent = sAgent;
    }
    
    public Priority GetPriority()
    {
        return messagePriority;
    }
}
