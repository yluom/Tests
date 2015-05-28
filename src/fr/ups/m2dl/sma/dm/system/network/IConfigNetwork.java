/**
 * 
 */
package fr.ups.m2dl.sma.dm.system.network;

public interface IConfigNetwork {
	/**
	 * Configure the machine in charge of the environment merging.
	 */
	void configureMaster(String ip, int port);
	
	/**
	 * Add a new slave machine
	 */
	void addSlave(String ip, int port);
	
	/**
	 * Remove an existing slave machine
	 */
	void removeSlave(String ip, int port);
}
