/**
 * 
 */
package fr.ups.m2dl.sma.dm.gui.swing;

import fr.ups.m2dl.sma.dm.system.components.graphic.Interface;
import fr.ups.m2dl.sma.dm.system.process.ICycle;


public class GuiInterfaceImpl extends Interface  {
	private MainFrame mainFrame;
	public static final int NOMBRE_ROBOTS = 120;
	public static final int NOMBRE_BOITES = 120;
	public static final int SPEED = 80;
	
	public void run() {
		requires().config().initialize(NOMBRE_ROBOTS, NOMBRE_BOITES);
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
