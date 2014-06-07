/**
 * 
 */
package fr.ups.m2dl.sma.dm.system.process;

import fr.ups.m2dl.sma.dm.system.components.process.CycleManager;
import fr.ups.m2dl.sma.dm.system.environment.Environment;

/**
 * @author SERIN Kevin
 *
 */
public class CycleManagerImpl extends CycleManager {

	@Override
	protected ICycle make_cycle() {
		return new ICycle() {
			
			@Override
			public void doCyle() {
				// do the local cycle
				requires().doCyle().doCyle();
				
				// getting the result from others machines and merge
				Environment localEnvironment = requires().envGet().get();
				Environment newEnvironment = requires().com().mergeEnvironment(localEnvironment);
				requires().envSet().set(newEnvironment);
				
				// call the after cycle method
				requires().doCyle().afterCycle();
			}
			
			@Override
			public void afterCycle() {
				// nothing to do
			}
		};
	}

}
