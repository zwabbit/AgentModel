package agentsimulation.Messages;

import agentsimulation.Agents.Agent;

public class Start extends Message {

	public Start(Agent rAgent, Agent sAgent) {
		super(rAgent, sAgent);
		this.messagePriority = Priority.LOW;
	}

}
