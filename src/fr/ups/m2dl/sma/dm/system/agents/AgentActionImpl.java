package fr.ups.m2dl.sma.dm.system.agents;

import fr.ups.m2dl.sma.dm.system.environment.ILocalEnvironmentSet.Direction;

public class AgentActionImpl extends AgentAct{
	String agent;
	
	public AgentActionImpl(String agent) {
		this.agent=agent;
	}

	@Override
	protected IAgentAction make_actions() {
		return new IAgentAction() {
			
			@Override
			public boolean suicide() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean pick() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean goUp() {
				return requires().localSet().changePosition(agent, Direction.NORTH);
			}
			
			@Override
			public boolean goRight() {
				return requires().localSet().changePosition(agent, Direction.EAST);
			}
			
			@Override
			public boolean goLeft() {
				return requires().localSet().changePosition(agent, Direction.WEST);
			}
			
			@Override
			public boolean goDown() {
				return requires().localSet().changePosition(agent, Direction.SOUTH);
			}
			
			@Override
			public boolean drop() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean createAgent() {
				// TODO Auto-generated method stub
				return false;
			}
		};
	}

}
