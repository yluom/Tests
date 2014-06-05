/**
 * 
 */
package fr.ups.m2dl.sma.dm.system.environment;

/**
 * Port for environment persistence.
 * @author SERIN Kevin
 *
 */
public interface IPersist {
	/**
	 * Persists the entire representation of the environment.
	 * @param fileName File name where to store the representation.
	 * @return True if succeed, false otherwise.
	 */
	boolean persist(String fileName);
}
