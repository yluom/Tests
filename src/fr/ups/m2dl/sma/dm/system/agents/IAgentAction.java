/**
 * 
 */
package fr.ups.m2dl.sma.dm.system.agents;

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
	
	boolean pick();
	
	boolean drop();
	
	boolean suicide();
	
	boolean createAgent();
}
