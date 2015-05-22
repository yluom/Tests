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
	private int nbCols;
	private TypeElement[][] environment;
	private Map<Position, Robot> robotsPositions;

	public Environment(int nbAgents, int nbBoxes) {
		this.robotsPositions = new HashMap<>();
		int nbColsDeposit = (nbBoxes / HEIGHT_DEPOSIT) + (nbBoxes % HEIGHT_DEPOSIT == 0 ? 0 : 1);
		int nbColsAgent = (nbAgents / HEIGHT_DEPOSIT) + (nbAgents % HEIGHT_DEPOSIT == 0 ? 0 : 1);
		this.nbCols = Math.max(nbColsAgent, nbColsDeposit);
		//		this.height = HEIGHT_DEPOSIT + 2*HEIGHT_MARGIN;
		this.height = 50;
		this.width = 50;//2*nbCols + 2*WIDTH_MARGIN;
		this.environment = new TypeElement[this.width][this.height];

		this.nbBoxesAtStarting = nbBoxes;
		this.clearEnvironment();
		this.initDeposit(this.nbBoxesAtStarting);
		this.initObstacle();

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
				&& (this.environment[newX][newY].equals(new TypeElement(Element.EMPTY)) || this.environment[newX][newY].equals(new TypeElement(Element.DEPOSIT)))) {

			Robot robot = this.robotsPositions.get(pos);
			this.robotsPositions.remove(pos);
			this.robotsPositions.put(newPos, new Robot(robot.id, robot.withBox));
			return true;
		}
		return false;
	}

	public boolean removeBox(int x, int y) {
		if(this.environment[x][y].isABox()) {
			this.environment[x][y] = new TypeElement(Element.EMPTY);
			return true;
		}
		return false;
	}

	public boolean addBox(int x, int y) {
		if(this.environment[x][y].equals(new TypeElement(Element.EMPTY))) {
			Random random = new Random();
			if(random.nextBoolean()) {
				this.environment[x][y] = new TypeElement(ColorType.BOXA);
			} else {
				this.environment[x][y] = new TypeElement(ColorType.BOXB);
			}
			return true;
		}
		return false;
	}

	public TypeElement getElementAtPosition(int x, int y) {
		if(x >= 0 && x < width && y >= 0 && y < height) {
			Robot robot = this.robotsPositions.get(new Position(x, y));
			if(robot != null) {
				if(robot.withBox) {
					return new TypeElement(Element.AGENT_WITH_BOX);
				}
				else {
					return new TypeElement(Element.AGENT);
				}
			}
			else {
				return this.environment[x][y];
			}
		}
		return null;
	}

	public Environment getLocalRepresentation(int x, int y) {
		Environment localRepresentation = new Environment(0, 100);//nbCols*HEIGHT_DEPOSIT);
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

	private void clearEnvironment() {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				this.environment[x][y] = new TypeElement(Element.EMPTY);
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
			Random random = new Random();
			if(random.nextBoolean()) {
				this.environment[rX][rY] = new TypeElement(ColorType.BOXA);
			} else {
				this.environment[rX][rY] = new TypeElement(ColorType.BOXB);
			}
		}				
	}

	private void initDeposit(int nbDeposit) {
		int cptDeposit = 0;
		for(int x = 0; (cptDeposit < nbDeposit &&  x < nbCols); x++) {
			for(int y = HEIGHT_MARGIN; (cptDeposit < nbDeposit && y < HEIGHT_MARGIN+HEIGHT_DEPOSIT); y++) {
				this.environment[x][y] = new TypeElement(Element.DEPOSIT);
				cptDeposit++;
			}
		}
	}

	private void initObstacle() {
		for(int x = nbCols+WIDTH_MARGIN; x < nbCols+WIDTH_MARGIN; x++) {
			for(int y = 0; y < this.height; y++) {
				this.environment[x][y] = new TypeElement(Element.EMPTY); // TODO LEO Element.OBSTACLEs
			}
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

	public enum Element {
		EMPTY, AGENT, AGENT_WITH_BOX, OBSTACLE, DEPOSIT
	}

	public enum ColorType {
		BOXA, BOXB
	}

	public class TypeElement {
		private Element element;
		private ColorType color;

		public TypeElement(Element e){
			this.element = e;
		}

		public TypeElement(ColorType c){
			this.color = c;
		}

		public boolean isABox(){
			return element==null;
		}

		public ColorType getColorType(){
			return this.color;
		}

		public Element getElement(){
			return this.element;
		}

		public boolean equals(Object other) {
			if (other == null) return false;
			if (other == this) return true;
			if (!(other instanceof TypeElement))return false;
			TypeElement otherMyClass = (TypeElement)other;
			if(otherMyClass.isABox()) {
				return this.isABox() && this.color.equals(otherMyClass.getColorType());
			} else {
				return !this.isABox() && this.element.equals(otherMyClass.getElement());
			}

		}

		public String toString(){
			return (this.isABox()) ? this.color.name() : this.element.name();
		}

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
	}
}
