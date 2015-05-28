/**
 * 
 */
package fr.ups.m2dl.sma.dm.gui.swing;

import fr.ups.m2dl.sma.dm.system.components.graphic.Interface;
import fr.ups.m2dl.sma.dm.system.process.ICycle;


/**
 * @author SERIN Kevin
 *
 */
public class GuiInterfaceImpl extends Interface  {
	private MainFrame mainFrame;
	public static final int SPEED = 12;
	
	public void run() {
		requires().config().initialize(100, 100);
		requires().actions().start(SPEED);
		mainFrame = new MainFrame(requires().envGet().get());
	}
	
	@Override
	protected ICycle make_cycle() {
		return new ICycle() {
			
			@Override
			public void doCyle() {
			}
			
			@Override
			public void afterCycle() {
				if(mainFrame != null) {
					mainFrame.repaint();
				}
			}
		};
	}
}
