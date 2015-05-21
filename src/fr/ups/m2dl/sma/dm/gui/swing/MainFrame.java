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
	
	public MainFrame(Environment environment) {
		super();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.setLayout(new BorderLayout());
		this.add(new EnvironmentPanel(environment), BorderLayout.CENTER);
		this.pack();
		this.setVisible(true);
	}
}
