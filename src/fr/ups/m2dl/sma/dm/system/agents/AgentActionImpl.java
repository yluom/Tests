package fr.ups.m2dl.sma.dm.system.agents;

import fr.ups.m2dl.sma.dm.system.environment.ILocalEnvironmentSet.Direction;

public class AgentActionImpl extends AgentAct{
	String agent;
	
	public AgentActionImpl(String agent) {
		// TODO Auto-generated constructor stub
		this.agent=agent;
	}

	@Override
	protected IAgentAction make_actions() {
		// TODO Auto-generated method stub
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
				// TODO Auto-generated method stub
				return requires().localSet().changePosition(agent, Direction.NORTH);
			}
			
			@Override
			public boolean goRight() {
				// TODO Auto-generated method stub
				return requires().localSet().changePosition(agent, Direction.WEST);
			}
			
			@Override
			public boolean goLeft() {
				// TODO Auto-generated method stub
				return requires().localSet().changePosition(agent, Direction.WEST);
			}
			
			@Override
			public boolean goDown() {
				// TODO Auto-generated method stub
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
