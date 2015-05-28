/**
 * 
 */
package fr.ups.m2dl.sma.dm.system.network;

import fr.ups.m2dl.sma.dm.system.components.network.Com;
import fr.ups.m2dl.sma.dm.system.components.network.ConflictManager;
import fr.ups.m2dl.sma.dm.system.components.network.NetworkCycleManager;


public class DummyNetworkCycleManagerImpl extends NetworkCycleManager {

	@Override
	protected ConflictManager make_conflictManager() {
		return new DummyConflictManagerImpl();
	}

	@Override
	protected Com make_communicator() {
		return new DummyComImpl();
	}

}
