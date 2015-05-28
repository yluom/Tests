/**
 * 
 */
package fr.ups.m2dl.sma.dm.system.network;

import fr.ups.m2dl.sma.dm.system.components.network.Com;
import fr.ups.m2dl.sma.dm.system.environment.Environment;

public class DummyComImpl extends Com {

	@Override
	protected ICom make_com() {
		return new ICom() {
			
			@Override
			public Environment mergeEnvironment(Environment myEnvironment) {
				return myEnvironment;	//no network communication
			}
		};
	}

	@Override
	protected IConfigNetwork make_config() {
		return new IConfigNetwork() {
			
			@Override
			public void removeSlave(String ip, int port) {
				//nothing in this implementation
			}
			
			@Override
			public void configureMaster(String ip, int port) {
				//nothing in this implementation
			}
			
			@Override
			public void addSlave(String ip, int port) {	
				//nothing in this implementation
			}
		};
	}

}
