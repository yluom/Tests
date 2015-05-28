/**
 * 
 */
package fr.ups.m2dl.sma.dm.system.environment;


public interface IPersist {
	/**
	 * Persists the entire representation of the environment.
	 * @param fileName File name where to store the representation.
	 * @return True if succeed, false otherwise.
	 */
	boolean persist(String fileName);
}
