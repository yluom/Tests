/**
 * 
 */
package fr.ups.m2dl.sma.dm.system.environment;

/**
 * Port for changing local information on the environment
 * (usually used by agents)
 * @author SERIN Kevin
 *
 */
public interface ILocalEnvironmentSet {
	public enum Direction {
		NORTH, SOUTH, EAST, WEST;
	}
	
	/**
	 * Move the agent to a new position in the environment. 
	 * @return True if the action succeed, false if the action is not possible.
	 */
	boolean changePosition(String agentIdentifier, Direction direction);
	
	/**
	 * Drop a box owned by the agent
	 * @return True if the action succeed, false if the action is not possible.
	 */
	boolean dropBox(String agentIdentifier, Direction whereToDrop);
	
	/**
	 * Pick a box. 
	 * @return True if the action succeed, false if the action is not possible.
	 */
	boolean pickBox(String agentIdentifier, Direction direction);
	
}
