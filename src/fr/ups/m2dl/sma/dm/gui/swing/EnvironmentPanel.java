/**
 * 
 */
package fr.ups.m2dl.sma.dm.gui.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import fr.ups.m2dl.sma.dm.system.environment.Environment;
import fr.ups.m2dl.sma.dm.system.environment.Environment.Element;

/**
 * @author SERIN Kevin
 *
 */
public class EnvironmentPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private final int SQUARE_SIZE = 16;
	private final int MARGIN = SQUARE_SIZE;
	private Environment environment;
	
	public EnvironmentPanel(Environment environment) {
		this.environment = environment;
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(environment.getWidth()*SQUARE_SIZE+2*MARGIN, environment.getHeight()*SQUARE_SIZE+2*MARGIN);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		this.drawMargin(g);
		
		int width = environment.getWidth();
		int height = environment.getHeight();
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				Element element = environment.getElementAtPosition(x, y);
				switch (element) {
				case AGENT:
					this.drawAgent(g, false, x, y);
					break;
				case AGENT_WITH_BOX:
					this.drawAgent(g, true, x, y);
					break;
				case BOX:
					this.drawBox(g, x, y);
					break;
				case DEPOSIT:
					this.drawDeposit(g, x, y);
					break;
				case EMPTY:
					//nothing
					break;
				case OBSTACLE:
					this.drawObstacle(g, x, y);
					break;
				}
			}
		}
	}
	
	private void drawAgent(Graphics g, boolean withBox, int x, int y) {
		if(withBox) {
			g.setColor(Color.ORANGE);
		}
		else {
			g.setColor(Color.GREEN);
		}
		g.fillRoundRect(x*SQUARE_SIZE+MARGIN, y*SQUARE_SIZE+MARGIN, SQUARE_SIZE, SQUARE_SIZE, 2, 2);
		g.setColor(Color.BLACK);
		g.drawRoundRect(x*SQUARE_SIZE+MARGIN, y*SQUARE_SIZE+MARGIN, SQUARE_SIZE, SQUARE_SIZE, 2, 2);
	}
	
	private void drawBox(Graphics g, int x, int y) {
		g.setColor(Color.RED);
		g.fillOval(x*SQUARE_SIZE+MARGIN, y*SQUARE_SIZE+MARGIN, 10, 10);
		g.setColor(Color.BLACK);
		g.drawOval(x*SQUARE_SIZE+MARGIN, y*SQUARE_SIZE+MARGIN, 10, 10);
	}
	
	private void drawDeposit(Graphics g, int x, int y) {
		g.setColor(Color.GRAY);
		g.fillRect(x*SQUARE_SIZE+MARGIN, y*SQUARE_SIZE+MARGIN, SQUARE_SIZE, SQUARE_SIZE);
	}
	
	private void drawObstacle(Graphics g, int x, int y) {
		g.setColor(Color.BLACK);
		g.fillRect(x*SQUARE_SIZE+MARGIN, y*SQUARE_SIZE+MARGIN, SQUARE_SIZE, SQUARE_SIZE);
	}
	
	private void drawMargin(Graphics g) {
		g.setColor(Color.BLACK);
		for(int x = 0; x < environment.getWidth()+2; x++) {
			g.fillRect(x*SQUARE_SIZE, 0, SQUARE_SIZE, SQUARE_SIZE);
			g.fillRect(x*SQUARE_SIZE, (environment.getHeight()+1)*SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
		}
		for(int y = 0; y < environment.getHeight()+2; y++) {
			g.fillRect(0, y*SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
			g.fillRect((environment.getWidth()+1)*SQUARE_SIZE, y*SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
		}
	}
}
