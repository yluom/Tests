/**
 * 
 */
package fr.ups.m2dl.sma.dm.system.network;

import fr.ups.m2dl.sma.dm.system.environment.Environment;

/**
 * Port for the conflict management 
 * @author SERIN Kevin
 *
 */
public interface IConflict {
	/**
	 * Merge the representation of two environment in one.
	 */
	Environment merge(Environment env1, Environment env2);
}
