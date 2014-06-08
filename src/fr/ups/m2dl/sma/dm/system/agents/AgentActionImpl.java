package fr.ups.m2dl.sma.dm.system.agents;

import fr.ups.m2dl.sma.dm.system.components.environment.EnvironmentRepresentation;
import fr.ups.m2dl.sma.dm.system.environment.Environment;
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

				return false;
			}
			
			@Override
			public boolean pick(Direction direction) {
				requires().localSet().pickBox(agent, direction);
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
			public boolean drop(Direction direction) {

			}
			
			@Override
			public boolean createAgent() {
				// TODO Auto-generated method stub
				return false;
			}
		};
	}

}
