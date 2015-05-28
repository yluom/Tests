/**
 * 
 */
package fr.ups.m2dl.sma.dm.system.network;

import fr.ups.m2dl.sma.dm.system.components.network.ConflictManager;
import fr.ups.m2dl.sma.dm.system.environment.Environment;


public class DummyConflictManagerImpl extends ConflictManager {
	@Override
	protected IConflict make_conflictManagement() {
		return new IConflict() {
			
			@Override
			public Environment merge(Environment env1, Environment env2) {
				return env1;	//no conflict management policy
			}
		};
	}

}
