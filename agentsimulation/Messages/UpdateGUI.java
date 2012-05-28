package agentsimulation.Messages;

import agentsimulation.Agents.Agent;

public class UpdateGUI extends Message{

	public UpdateGUI(Agent rAgent, Agent sAgent) {
		super(rAgent, sAgent);
		this.messagePriority = Priority.LOW;
	}
	
}
