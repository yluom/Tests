/**
 * 
 */
package fr.ups.m2dl.sma.dm.system.environment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

/**
 * @author SERIN Kevin
 * TODO: refactor this ugly code
 */
public class Environment implements Serializable {
	private static final long serialVersionUID = 1L;
	private final int HEIGHT_DEPOSIT = 30;
	private final int HEIGHT_MARGIN = 10;
	private final int WIDTH_MARGIN = 20;
	
	private int nbBoxesAtStarting;
	private int width;
	private int height;
	private int yPassage1;
	private int yPassage2;
	private int nbCols;
	private Element[][] environment;
	private Map<Position, Robot> robotsPositions;
	
	public Environment(int nbAgents, int nbBoxes) {
		this.robotsPositions = new HashMap<>();
		int nbColsDeposit = (nbBoxes / HEIGHT_DEPOSIT) + (nbBoxes % HEIGHT_DEPOSIT == 0 ? 0 : 1);
		int nbColsAgent = (nbAgents / HEIGHT_DEPOSIT) + (nbAgents % HEIGHT_DEPOSIT == 0 ? 0 : 1);
		this.nbCols = Math.max(nbColsAgent, nbColsDeposit);
//		this.height = HEIGHT_DEPOSIT + 2*HEIGHT_MARGIN;
		this.height = 50;
		this.width = 50;//2*nbCols + 2*WIDTH_MARGIN;
		this.environment = new Element[this.width][this.height];
		this.yPassage1 = 0;
		this.yPassage2 = this.height-1;
		
		this.nbBoxesAtStarting = nbBoxes;
		this.clearEnvironment();
		this.initDeposit(this.nbBoxesAtStarting);
		this.initObstacle();
		
		this.createPassages();
		this.createRobots(nbAgents);
		this.initBoxes(this.nbBoxesAtStarting);

	}
	
	public Collection<String> getRobots() {
		ArrayList<String> result = new ArrayList<>();
		for (Robot robot : this.robotsPositions.values()) {
			result.add(robot.id);
		}
		return result;
	}
	
	public Integer getRobotX(String robotId) {
		for (Entry<Position, Robot> robot : this.robotsPositions.entrySet()) {
			if(robot.getValue().id.equals(robotId)) {
				return robot.getKey().x;
			}
		}
		return null;
	}
	
	public Integer getRobotY(String robotId) {
		for (Entry<Position, Robot> robot : this.robotsPositions.entrySet()) {
			if(robot.getValue().id.equals(robotId)) {
				return robot.getKey().y;
			}
		}
		return null;
	}
	
	public void setRobotState(String robotId, boolean withBox) {
		for (Entry<Position, Robot> robot : this.robotsPositions.entrySet()) {
			if(robot.getValue().id.equals(robotId)) {
				robot.getValue().withBox = withBox;
				break;
			}
		}
	}
	
	public boolean moveRobot(int x, int y, int newX, int newY) {
		Position pos = new Position(x, y);
		Position newPos = new Position(newX, newY);
		if(this.robotsPositions.containsKey(pos) 
				&& !this.robotsPositions.containsKey(newPos)
				&& (this.environment[newX][newY].equals(Element.EMPTY) || this.environment[newX][newY].equals(Element.DEPOSIT))) {
			
			Robot robot = this.robotsPositions.get(pos);
			this.robotsPositions.remove(pos);
			this.robotsPositions.put(newPos, new Robot(robot.id, robot.withBox));
			return true;
		}
		return false;
	}
	
	public boolean removeBox(int x, int y) {
		if(this.environment[x][y].equals(Element.BOX)) {
			this.environment[x][y] = Element.EMPTY;
			return true;
		}
		return false;
	}
	
	public boolean addBox(int x, int y) {
		if(this.environment[x][y].equals(Element.EMPTY)) {
			this.environment[x][y] = Element.BOX;
			return true;
		}
		return false;
	}
	
	public Element getElementAtPosition(int x, int y) {
		if(x >= 0 && x < width && y >= 0 && y < height) {
			Robot robot = this.robotsPositions.get(new Position(x, y));
			if(robot != null) {
				if(robot.withBox) {
					return Element.AGENT_WITH_BOX;
				}
				else {
					return Element.AGENT;
				}
			}
			else {
				return this.environment[x][y];
			}
		}
		return null;
	}
	
	public Environment getLocalRepresentation(int x, int y) {
		Environment localRepresentation = new Environment(0, nbCols*HEIGHT_DEPOSIT);
		localRepresentation.clearEnvironment();
		
		//add all deposits because agents know the their positions
		localRepresentation.initDeposit(this.nbBoxesAtStarting);
		localRepresentation.initBoxes(this.nbBoxesAtStarting);
		
		int minX = Math.max(0, x-3);
		int maxX = Math.min(this.width-1, x+3);
		int minY = Math.max(0, y-3);
		int maxY = Math.min(this.height-1, y+3);
		for(int i = minX; i <= maxX; i++) {
			for(int j = minY; j <= maxY; j++) {
				Robot robot = this.robotsPositions.get(new Position(i, j));
				if(robot != null) {
					localRepresentation.robotsPositions.put(new Position(i, j), new Robot(robot.id, robot.withBox));
				}
				localRepresentation.environment[i][j] = this.environment[i][j];
			}
		}
		
		
		
		return localRepresentation;
	}
	
	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void print() {
		for (int y = 0; y < this.height; y++) {
			for (int x = 0; x < this.width; x++) {
				switch (this.getElementAtPosition(x, y)) {
				case EMPTY:
					System.out.print(" ");
					break;
				case AGENT:
					System.out.print("a");
					break;
				case AGENT_WITH_BOX:
					System.out.print("A");
					break;
				case OBSTACLE:
					System.out.print("O");
					break;
				case BOX:
					System.out.print("*");
					break;
				case DEPOSIT:
					System.out.print("D");
					break;
				}
			}
			System.out.println("");
		}
	}
	
	private void clearEnvironment() {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				this.environment[x][y] = Element.EMPTY;
			}
		}
	}
	
	private void initBoxes(int nbBoxes) {
		int cptBoxes = 0;
		Random r = new Random();
		int rX, rY;
		for(cptBoxes = 0; cptBoxes < nbBoxes; cptBoxes++)
		{	
				rX = nbCols + r.nextInt(this.width-nbCols);
				rY = r.nextInt(this.height);
				this.environment[rX][rY] = Element.BOX;
		}				
	}
	
	private void initDeposit(int nbDeposit) {
		int cptDeposit = 0;
		for(int x = 0; (cptDeposit < nbDeposit &&  x < nbCols); x++) {
			for(int y = HEIGHT_MARGIN; (cptDeposit < nbDeposit && y < HEIGHT_MARGIN+HEIGHT_DEPOSIT); y++) {
				this.environment[x][y] = Element.DEPOSIT;
				cptDeposit++;
			}
		}
	}
	
	private void initObstacle() {
		for(int x = nbCols+WIDTH_MARGIN; x < nbCols+WIDTH_MARGIN; x++) {
			for(int y = 0; y < this.height; y++) {
				this.environment[x][y] = Element.EMPTY; // TODO LEO Element.OBSTACLEs
			}
		}
	}
	
	private void createPassages() {
		for(int x = nbCols+WIDTH_MARGIN; x <= nbCols+WIDTH_MARGIN; x++) {
			this.environment[x][this.yPassage1] = Element.EMPTY;
			this.environment[x][this.yPassage2] = Element.EMPTY;
		}
	}
	
	private void createRobots(int nb) {
		int cptRobot = 0;
		for(int x = 0; (cptRobot < nb &&  x < nbCols); x++) {
			for(int y = HEIGHT_MARGIN; (cptRobot < nb && y < HEIGHT_MARGIN+HEIGHT_DEPOSIT); y++) {
				this.robotsPositions.put(new Position(x, y), new Robot("Robot"+cptRobot));
				cptRobot++;
			}
		}
	}
	
	public enum PassageWay {
		PASSAGE_1, PASSAGE_2;
	}
	
	public enum Element {
		EMPTY, AGENT, AGENT_WITH_BOX, OBSTACLE, BOX, DEPOSIT
	}
	
	private class Position {
		public int x;
		public int y;
		
		public Position(int x, int y) {
			this.x = x;
			this.y = y;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + x;
			result = prime * result + y;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Position other = (Position) obj;
			if (x != other.x)
				return false;
			if (y != other.y)
				return false;
			return true;
		}
	}
	
	private class Robot {
		public String id;
		public boolean withBox;
		
		public Robot(String id) {
			this.id = id;
			this.withBox = false;
		}
		
		public Robot(String id, boolean withBox) {
			this.id = id;
			this.withBox = withBox;
		}
	}
	
	public static void main(String[] args) {
		Environment e = new Environment(15, 60);
		e.print();
		e.getLocalRepresentation(22, 6).print();
		
	}
}
