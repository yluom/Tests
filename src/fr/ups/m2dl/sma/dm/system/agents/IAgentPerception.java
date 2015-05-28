/**
 * 
 */
package fr.ups.m2dl.sma.dm.system.agents;

import fr.ups.m2dl.sma.dm.system.environment.Environment;


public interface IAgentPerception {
	/**
	 * Give the environment as its perceived by the agent.
	 * @return Currently an Environment object but it could change according to how
	 * the agent perceives the environment.
	 */
	Environment perceive();
}
