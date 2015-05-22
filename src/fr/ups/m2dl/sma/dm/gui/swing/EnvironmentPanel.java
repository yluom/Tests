/**
 * 
 */
package fr.ups.m2dl.sma.dm.gui.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import fr.ups.m2dl.sma.dm.system.environment.Environment;
import fr.ups.m2dl.sma.dm.system.environment.Environment.ColorType;
import fr.ups.m2dl.sma.dm.system.environment.Environment.TypeElement;

/**
 * @author SERIN Kevin
 *
 */
public class EnvironmentPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private final int SQUARE_SIZE = 16;
	private final int MARGIN = SQUARE_SIZE;
	private Environment environment;
	private Image robApple, robMicro, robPlein, apple, micro;
	
	public EnvironmentPanel(Environment environment) {
		this.environment = environment;
		try {
			BufferedImage buffImg = null;
			buffImg = ImageIO.read(new File("src/fr/ups/m2dl/sma/dm/res/img/robot3.png"));
			this.robApple = buffImg.getScaledInstance(-1, 16, Image.SCALE_SMOOTH);
			buffImg = ImageIO.read(new File("src/fr/ups/m2dl/sma/dm/res/img/robot2.png"));
			this.robMicro = buffImg.getScaledInstance(-1, 16, Image.SCALE_SMOOTH);
			buffImg = ImageIO.read(new File("src/fr/ups/m2dl/sma/dm/res/img/robot1.png"));
			this.robPlein = buffImg.getScaledInstance(-1, 16, Image.SCALE_SMOOTH);
			buffImg = ImageIO.read(new File("src/fr/ups/m2dl/sma/dm/res/img/apple.png"));
			this.apple = buffImg.getScaledInstance(-1, 16, Image.SCALE_SMOOTH);
			buffImg = ImageIO.read(new File("src/fr/ups/m2dl/sma/dm/res/img/micro.png"));
			this.micro = buffImg.getScaledInstance(-1, 16, Image.SCALE_SMOOTH);
		} catch (IOException e) {
			e.printStackTrace();
		}
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
				TypeElement element = environment.getElementAtPosition(x, y);
				if(element.getElement() != null){
					switch (element.getElement()) {
					case AGENT:
						this.drawAgent(g, element, x, y);
						break;
					case AGENT_WITH_BOX:
						this.drawAgent(g, element, x, y);
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
				} else {
					this.drawBox(g, x, y, element);
					break;
				}
			}
		}
	}
	
	private void drawAgent(Graphics g, TypeElement element, int x, int y) {
		if(element.isABox()) {
			g.drawImage(this.robPlein, x*SQUARE_SIZE+MARGIN, y*SQUARE_SIZE+MARGIN, this);
		}
		else {
			g.drawImage(this.robApple, x*SQUARE_SIZE+MARGIN, y*SQUARE_SIZE+MARGIN, this);
		}
	}
	
	private void drawBox(Graphics g, int x, int y, TypeElement element) {
		g.drawImage(getImage(element.getColorType()), x*SQUARE_SIZE+MARGIN, y*SQUARE_SIZE+MARGIN, this);
	}
	
	private Image getImage(ColorType colorType) {
		switch(colorType){
		case BOXA : return this.apple;
		case BOXB : return this.micro;
		}
		return null;
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
