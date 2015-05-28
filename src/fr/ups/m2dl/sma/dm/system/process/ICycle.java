/**
 * 
 */
package fr.ups.m2dl.sma.dm.system.process;

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
