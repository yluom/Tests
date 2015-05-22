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
public class GuiInterfaceImpl extends Interface implements ControlPanelListener {
	private MainFrame mainFrame;
	private int speed = 1;
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

	@Override
	public void onNextAction() {
		requires().actions().nextCycle();
	}

	@Override
	public void onStartAction() {
		requires().actions().start(this.speed);
	}

	@Override
	public void onStopAction() {
		requires().actions().pause();
	}

	@Override
	public void onSpeedUpAction() {
		this.speed++;
		requires().actions().increaseSpeed();
	}

	@Override
	public void onSpeedDownAction() {
		this.speed--;
		if(this.speed <= 0) {
			this.speed = 1;
		}
		requires().actions().decreaseSpeed();
	}

}
