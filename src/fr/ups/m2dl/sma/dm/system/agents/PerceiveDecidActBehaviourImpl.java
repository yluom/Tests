/**
 * 
 */
package fr.ups.m2dl.sma.dm.system.agents;

import fr.ups.m2dl.sma.dm.system.environment.Environment.ColorType;


public class PerceiveDecidActBehaviourImpl extends AgentBehaviourPDA {
	private final String agentId;
	private final ColorType agentType;

	public PerceiveDecidActBehaviourImpl(String agentId, ColorType eltRobot) {
		this.agentId = agentId;
		this.agentType = eltRobot;
	}

	@Override
	protected AgentPerception make_perception() {
		return new AgentPerceptionImpl(agentId);
	}

	@Override
	protected AgentAct make_actions() {
		return new AgentActionImpl(agentId);
	}

	@Override
	protected AgentDecision make_decision() {
		// return new RandomAgentDecisionImpl(agentId);
		return new AgentDecisionFarFromAreasImpl(agentId, agentType);
		// return new ReactiveAgentDecisionImpl(agentId);
	}

}
