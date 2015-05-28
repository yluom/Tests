/**
 * 
 */
package fr.ups.m2dl.sma.dm.system.network;

import fr.ups.m2dl.sma.dm.system.environment.Environment;

public interface IConflict {
	/**
	 * Merge the representation of two environment in one.
	 */
	Environment merge(Environment env1, Environment env2);
}
