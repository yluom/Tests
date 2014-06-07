/**
 * 
 */
package fr.ups.m2dl.sma.dm.system.process;

import fr.ups.m2dl.sma.dm.utils.Dispatcher2;

/**
 * @author SERIN Kevin
 *
 */
public class CycleDispatcherImpl extends Dispatcher2<ICycle> {

	@Override
	protected ICycle make_from() {
		return new ICycle() {
			
			@Override
			public void doCyle() {
				requires().to1().doCyle();
				requires().to2().doCyle();
			}
			
			@Override
			public void afterCycle() {
				requires().to1().afterCycle();
				requires().to2().afterCycle();
			}
		};
	}
}
