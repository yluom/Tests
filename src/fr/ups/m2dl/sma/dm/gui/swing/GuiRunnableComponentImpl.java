/**
 * 
 */
package fr.ups.m2dl.sma.dm.gui.swing;

import fr.ups.m2dl.sma.dm.system.SystemImpl;
import fr.ups.m2dl.sma.dm.system.components.graphic.Interface;
import fr.ups.m2dl.sma.dm.system.components.graphic.RunnableComponent;
import fr.ups.m2dl.sma.dm.system.components.system.System;

/**
 * @author SERIN Kevin
 *
 */
public class GuiRunnableComponentImpl extends RunnableComponent {
	private GuiInterfaceImpl guiInterfaceImpl = new GuiInterfaceImpl();
	
	@Override
	protected Runnable make_run() {
		return new Runnable() {
			
			@Override
			public void run() {
				guiInterfaceImpl.run();
			}
		};
	}

	@Override
	protected Interface make_gui() {
		return guiInterfaceImpl;
	}

	@Override
	protected System make_system() {
		return new SystemImpl();
	}
}
