/**
 * 
 */
package fr.ups.m2dl.sma.dm.gui.swing;

import fr.ups.m2dl.sma.dm.gui.swing.ControlPanel.State;
import fr.ups.m2dl.sma.dm.system.components.graphic.Interface;
import fr.ups.m2dl.sma.dm.system.process.ICycle;

/**
 * @author SERIN Kevin
 *
 */
public class GuiInterfaceImpl extends Interface implements ControlPanelListener {
	private InitFrame initFrame;
	private MainFrame mainFrame;
	
	public void run() {
		initFrame = new InitFrame(this);
	}
	
	public void init(int nbAgents, int nbBoxes) {
		// close the init frame
		initFrame.setVisible(false);
		initFrame.dispose();
		initFrame = null;
		
		requires().config().initialize(nbAgents, nbBoxes);
		mainFrame = new MainFrame(requires().envGet().get());
		mainFrame.getControlPanel().addListerner(this);
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
		mainFrame.getControlPanel().setState(State.IN_PROGRESS);
		requires().actions().nextCycle();
		mainFrame.getControlPanel().setState(State.STOPPED);
	}

	@Override
	public void onStartAction() {
		mainFrame.getControlPanel().setState(State.STARTED);
		requires().actions().start(1);
	}

	@Override
	public void onStopAction() {
		mainFrame.getControlPanel().setState(State.IN_PROGRESS);
		requires().actions().pause();
		mainFrame.getControlPanel().setState(State.STOPPED);
	}

	@Override
	public void onSpeedUpAction() {
		requires().actions().increaseSpeed();
	}

	@Override
	public void onSpeedDownAction() {
		requires().actions().decreaseSpeed();
	}

}
