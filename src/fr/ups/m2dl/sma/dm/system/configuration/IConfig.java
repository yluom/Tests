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
	 * Initialize the system
	 * @param nbAgent Number of robot at the beginning
	 * @param nbBoxes Number of boxes at the beginning
	 */
	void initialize(int nbAgent, int nbBoxes);
}
