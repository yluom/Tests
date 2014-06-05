/**
 * 
 */
package fr.ups.m2dl.sma.dm.system.process;

/**
 * Port to engage one process cycle
 * @author SERIN Kevin
 *
 */
public interface ICycle {
	/**
	 * Called to engage the cycle.
	 */
	void doCyle();
	
	/**
	 * Called after the end of a cycle.
	 */
	void afterCycle();
}
