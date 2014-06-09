/**
 * 
 */
package fr.ups.m2dl.sma.dm.gui.swing;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import fr.ups.m2dl.sma.dm.system.environment.Environment;

/**
 * @author SERIN Kevin
 *
 */
public class MainFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private ControlPanel controlPanel;
	
	public MainFrame(Environment environment) {
		super();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.setLayout(new BorderLayout());
		this.add(new EnvironmentPanel(environment), BorderLayout.CENTER);
		controlPanel = new ControlPanel();
		this.add(controlPanel, BorderLayout.PAGE_END);
		this.pack();
		this.setVisible(true);
	}
	
	public ControlPanel getControlPanel() {
		return controlPanel;
	}
	
//	public static void main(String[] args) {
//		new MainFrame();
//	}
	
}
