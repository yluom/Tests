/**
 * 
 */
package fr.ups.m2dl.sma.dm.system.process;

/**
 * Port for action to control the execution.
 * @author SERIN Kevin
 *
 */
public interface IAction {
	/**
	 * Start the execution.
	 * @param speed
	 */
	void start(int speed);
	
	/**
	 * Pause the execution.
	 */
	void pause();
	
	/**
	 * Do the next cycle of the execution.
	 */
	void nextCycle();
	
	/**
	 * Increase the speed of the execution
	 */
	void increaseSpeed();
	
	/**
	 * Decrease the speed of the execution
	 */
	void decreaseSpeed();
}
