/**
 * 
 */
package fr.ups.m2dl.sma.dm.system.configuration;

/**
 * Port for the global configuration of the system.
 * @author SERIN Kevin
 *
 */
public interface IConfig {
	/**
	 * @return The number of agent at the beginning of the system
	 */
	int getNbAgent();
	
	/**
	 * @return The number of boxes at the beginning of the system.
	 */
	int getNbBox();
}
