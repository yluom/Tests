/**
 * 
 */
package fr.ups.m2dl.sma.dm.system.agents;

import java.util.Collection;


public interface IConfigEcosystem {
	/**
	 * Initialize the ecosystem.
	 * @param agentsToManage The identifiers of agent to be managed by the ecosystem.
	 */
	void initialize(Collection<String> agentsToManage);
}
