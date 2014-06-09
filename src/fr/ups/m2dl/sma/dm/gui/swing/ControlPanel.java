/**
 * 
 */
package fr.ups.m2dl.sma.dm.gui.swing;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * @author SERIN Kevin
 *
 */
public class ControlPanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	private List<ControlPanelListener> listeners;
	private JButton buttonNext;
	private JButton buttonStart;
	private JButton buttonStop;
	private JButton buttonPlus;
	private JButton buttonMinus;
	
	public ControlPanel() {
		this.listeners = new ArrayList<>();
		setLayout(new FlowLayout());
		
		buttonNext = new JButton("Next");
		buttonNext.addActionListener(this);
		buttonStart = new JButton("Start");
		buttonStart.addActionListener(this);
		buttonStop = new JButton("Stop");
		buttonStop.addActionListener(this);
		buttonPlus = new JButton("+");
		buttonPlus.addActionListener(this);
		buttonMinus = new JButton("-");
		buttonMinus.addActionListener(this);
		this.add(buttonNext);
		this.add(buttonStart);
		this.add(buttonStop);
		this.add(buttonMinus);
		this.add(buttonPlus);
		this.setState(State.STOPPED);
	}
	
	public void setState(State state) {
		switch(state) {
		case STOPPED:
			this.buttonNext.setEnabled(true);
			this.buttonStart.setEnabled(true);
			this.buttonStop.setEnabled(false);
			this.buttonMinus.setEnabled(false);
			this.buttonPlus.setEnabled(false);
			break;
		case IN_PROGRESS:
			this.buttonNext.setEnabled(false);
			this.buttonStart.setEnabled(false);
			this.buttonStop.setEnabled(false);
			this.buttonMinus.setEnabled(false);
			this.buttonPlus.setEnabled(false);
			break;
		case STARTED:
			this.buttonNext.setEnabled(false);
			this.buttonStart.setEnabled(false);
			this.buttonStop.setEnabled(true);
			this.buttonMinus.setEnabled(true);
			this.buttonPlus.setEnabled(true);
			break;
		}
	}
	
	public void addListerner(ControlPanelListener listener) {
		this.listeners.add(listener);
	}
	
	public void removeListener(ControlPanelListener listener) {
		this.listeners.remove(listener);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if(source == buttonNext) {
			for (ControlPanelListener listener : listeners) {
				listener.onNextAction();
			}
		} else if(source == buttonStart) {
			for (ControlPanelListener listener : listeners) {
				listener.onStartAction();
			}
		} else if(source == buttonStop) {
			for (ControlPanelListener listener : listeners) {
				listener.onStopAction();
			}
		} else if(source == buttonMinus) {
			for (ControlPanelListener listener : listeners) {
				listener.onSpeedDownAction();
			}
		} else if(source == buttonPlus) {
			for (ControlPanelListener listener : listeners) {
				listener.onSpeedUpAction();
			}
		}
	}
	
	public enum State {
		STOPPED, STARTED, IN_PROGRESS;
	}
	
}
