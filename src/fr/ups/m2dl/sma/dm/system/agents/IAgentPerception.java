/**
 * 
 */
package fr.ups.m2dl.sma.dm.system.agents;

import fr.ups.m2dl.sma.dm.system.environment.Environment;

/**
 * Port for the perception of an agent.
 * @author SERIN Kevin
 *
 */
public interface IAgentPerception {
	/**
	 * Give the environment as its perceived by the agent.
	 * @return Currently an Environment object but it could change according to how
	 * the agent perceives the environment.
	 */
	Environment perceive();
}
