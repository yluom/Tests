/**
 * 
 */
package fr.ups.m2dl.sma.dm.system.environment;



public interface IConfigEnvironment {
	/** Initialize the environment. 
	 * Must be call before starting the system.
	 * Must not be call after starting the execution.
	 * @param nbAgent Number of agent at the starting of the environment.
	 * @param nbBox Number of box at the starting of the environment.
	 */
	Environment initialize(int nbAgent, int nbBox);
}
