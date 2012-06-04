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
public class Killer extends Message {
    boolean isKiller;
    public Killer(Agent rAgent, Agent sAgent, boolean isKiller)
    {
        super(rAgent, sAgent);
        this.isKiller = isKiller;
    }
    
    public boolean IsKiller()
    {
        return isKiller;
    }
}
