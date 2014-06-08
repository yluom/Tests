/**
 * 
 */
package fr.ups.m2dl.sma.dm.gui;

import fr.ups.m2dl.sma.dm.system.components.graphic.Interface;
import fr.ups.m2dl.sma.dm.system.process.ICycle;

/**
 * @author SERIN Kevin
 *
 */
public class ConsoleGui extends Interface {

	@Override
	protected ICycle make_cycle() {
		return new ICycle() {
			
			@Override
			public void doCyle() {
			}
			
			@Override
			public void afterCycle() {
				requires().envGet().get().print();
			}
		};
	}

}
