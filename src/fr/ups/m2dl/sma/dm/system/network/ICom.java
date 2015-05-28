/**
 * 
 */
package fr.ups.m2dl.sma.dm.system.network;

import fr.ups.m2dl.sma.dm.system.environment.Environment;

public interface ICom {
	/**
	 * Merge my environment representation with all other environments from the network.
	 */
	Environment mergeEnvironment(Environment myEnvironment);
}
