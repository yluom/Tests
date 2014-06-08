package fr.ups.m2dl.sma.dm.system.agents;

import fr.ups.m2dl.sma.dm.system.environment.Environment;

public class AgentPerceptionImpl extends AgentPerception{
	
	String agent;
	
	public AgentPerceptionImpl(String agent){
		// TODO Auto-generated constructor stub
		this.agent=agent;
	}

	@Override
	protected IAgentPerception make_perception() {
		// TODO Auto-generated method stub
		return new IAgentPerception() {
			
			@Override
			public Environment perceive() {
				// TODO Auto-generated method stub
				return requires().localGet().get(agent);
			}
		};
	}

}
