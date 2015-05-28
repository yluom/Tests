/**
 * 
 */
package fr.ups.m2dl.sma.dm.system.environment;


public interface ILocalEnvironmentGet {
	/**
	 * Return the environment as it's perceived by an agent
	 * @param agentIdentifier The unique identifier of the agent
	 */
	Environment get(String agentIdentifier);
}
