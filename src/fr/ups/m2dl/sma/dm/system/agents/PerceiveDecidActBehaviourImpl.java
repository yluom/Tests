/**
 * 
 */
package fr.ups.m2dl.sma.dm.system.agents;

/**
 * @author SERIN Kevin
 *
 */
public class PerceiveDecidActBehaviourImpl extends AgentBehaviourPDA {
	private String agentId;
	
	public PerceiveDecidActBehaviourImpl(String agentId) {
		this.agentId = agentId;
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
		return new RandomAgentDecisionImpl(agentId);
	}

}
