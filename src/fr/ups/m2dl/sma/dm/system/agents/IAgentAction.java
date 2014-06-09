/**
 * 
 */
package fr.ups.m2dl.sma.dm.system.agents;

import fr.ups.m2dl.sma.dm.system.environment.ILocalEnvironmentSet.Direction;

/**
 * Port for possible actions of the agent.
 * @author SERIN Kevin
 *
 */
public interface IAgentAction {
	boolean goUp();
	
	boolean goDown();
	
	boolean goLeft();
	
	boolean goRight();
	
	boolean pick(Direction direction);
	
	boolean drop(Direction direction);
	
	boolean suicide();
	
	boolean createAgent();
}
