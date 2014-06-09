/**
 * 
 */
package fr.ups.m2dl.sma.dm.gui.swing;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

/**
 * @author SERIN Kevin
 *
 */
public class InitFrame extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	private JSpinner nbAgentSpinner;
	private JSpinner nbBoxesSpinner;
	private GuiInterfaceImpl guiInterface;
	
	public InitFrame(GuiInterfaceImpl guiInterface) {
		super();
		this.guiInterface = guiInterface;
		this.setSize(500, 80);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		setLayout(new FlowLayout());
		
		this.add(new JLabel("Nb agent: "));
		this.nbAgentSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 1000, 1));
		this.add(this.nbAgentSpinner);
		this.add(new JLabel("Nb boxes: "));
		this.nbBoxesSpinner = new JSpinner(new SpinnerNumberModel(100, 1, 1000, 1));
		this.add(this.nbBoxesSpinner);
		JButton button = new JButton("OK");
		button.addActionListener(this);
		this.add(button);
		
		this.setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Integer nbAgent = (Integer) this.nbAgentSpinner.getValue();
		Integer nbBoxes = (Integer) this.nbBoxesSpinner.getValue();
		guiInterface.init(nbAgent, nbBoxes);
	}
	
//	public static void main(String[] args) {
//		new InitFrame();
//		System.out.println("ok");
//	}
}