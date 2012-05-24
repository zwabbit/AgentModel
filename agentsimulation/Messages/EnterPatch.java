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
public class EnterPatch extends Message {
    
    public EnterPatch(Agent rAgent, Agent sAgent)
    {
        super(rAgent, sAgent);
    }
}
