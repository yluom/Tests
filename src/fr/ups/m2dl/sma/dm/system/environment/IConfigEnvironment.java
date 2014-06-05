/**
 * 
 */
package fr.ups.m2dl.sma.dm.system.environment;

import fr.ups.m2dl.sma.dm.system.environment.Environment.PassageWay;

/**
 * Port for the environment configuration
 * @author SERIN Kevin
 *
 */
public interface IConfigEnvironment {
	/** Initialize the environment. 
	 * Must be call before starting the system.
	 * Must not be call after starting the execution.
	 * @param nbAgent Number of agent at the starting of the environment.
	 * @param nbBox Number of box at the starting of the environment.
	 */
	void initialize(int nbAgent, int nbBox);
	
	/**
	 * Move up to position of a passageway 
	 * Can be call during the execution.
	 * @return True if the operation succeed. 
	 */
	boolean moveUpPassageWay(PassageWay passage);
	
	/**
	 * Move down the position of a passageway
	 * Can be call during the execution.
	 * @return True if the operation succeed.
	 */
	boolean moveDownPassageWay(PassageWay passage);
}
