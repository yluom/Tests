/**
 * 
 */
package fr.ups.m2dl.sma.dm.system.environment;

/**
 * Port for getting local information about the environment
 * (usually used by agents)
 * @author SERIN Kevin
 *
 */
public interface ILocalEnvironmentGet {
	/**
	 * Return the environment as it's perceived by an agent
	 * @param agentIdentifier The unique identifier of the agent
	 */
	Environment get(String agentIdentifier);
}
