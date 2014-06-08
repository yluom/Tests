package fr.ups.m2dl.sma.dm.system.agents;

import fr.ups.m2dl.sma.dm.system.environment.Environment;

public class AgentPerceptionImpl extends AgentPerception{
	
	String agent;
	
	public AgentPerceptionImpl(String agent){
		this.agent=agent;
	}

	@Override
	protected IAgentPerception make_perception() {
		return new IAgentPerception() {
			
			@Override
			public Environment perceive() {
				return requires().localGet().get(agent);
			}
		};
	}

}
