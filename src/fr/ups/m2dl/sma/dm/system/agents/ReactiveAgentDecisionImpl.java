/**
 * 
 */
package fr.ups.m2dl.sma.dm.system.agents;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.ups.m2dl.sma.dm.system.environment.Environment;
import fr.ups.m2dl.sma.dm.system.environment.Environment.Element;
import fr.ups.m2dl.sma.dm.system.environment.Environment.TypeElement;
import fr.ups.m2dl.sma.dm.system.environment.ILocalEnvironmentSet.Direction;
import fr.ups.m2dl.sma.dm.system.log.ILog;
import fr.ups.m2dl.sma.dm.system.log.Log;
import fr.ups.m2dl.sma.dm.system.process.ICycle;

/**
 * @author SERIN Kevin
 *
 */
public class ReactiveAgentDecisionImpl extends AgentDecision {
	private String agentId;
	private Element agentType;
	private boolean wearBox = false;
	private Goal goal;
	private Direction goalDirection;
	private Direction preference;
	private int waitingTime = 0;
	
	public ReactiveAgentDecisionImpl(String agentId, Element agentType) {
		this.agentId = agentId;
		this.agentType = agentType;
	}
	
	private TypeElement getElementAt(Environment env, int myX, int myY, Direction direction) {
		TypeElement element = null;
		switch (direction) {
		case EAST:
			element = env.getElementAtPosition(myX + 1, myY);
			break;
		case NORTH:
			element = env.getElementAtPosition(myX, myY - 1);
			break;
		case SOUTH:
			element = env.getElementAtPosition(myX, myY + 1);
			break;
		case WEST:
			element = env.getElementAtPosition(myX - 1, myY);
			break;
		}
		return element;
	}
	
	private boolean canIGo(Environment env, int myX, int myY, Direction direction) {
		TypeElement element = getElementAt(env, myX, myY, direction);
		return (element != null && (element.equals(env.new TypeElement(Element.EMPTY)) || element.equals(env.new TypeElement(Element.DEPOSIT))));
	}
	
	private Direction canIPick(Environment env, int myX, int myY) {
		if(env.getElementAtPosition(myX+1, myY) != null && env.getElementAtPosition(myX+1, myY).equals(this.agentType)) {
			return Direction.EAST;
		}
		if(env.getElementAtPosition(myX-1, myY) != null && env.getElementAtPosition(myX-1, myY).equals(this.agentType)) {
			return Direction.WEST;
		}
		if(env.getElementAtPosition(myX, myY+1) != null && env.getElementAtPosition(myX, myY+1).equals(this.agentType)) {
			return Direction.SOUTH;
		}
		if(env.getElementAtPosition(myX, myY-1) != null && env.getElementAtPosition(myX, myY-1).equals(this.agentType)) {
			return Direction.NORTH;
		}
		return null;
	}
	
	private Direction canIDrop(Environment env, int myX, int myY) {
		if(env.getElementAtPosition(myX+1, myY) != null && env.getElementAtPosition(myX+1, myY).equals(env.new TypeElement(Element.DEPOSIT))) {
			return Direction.EAST;
		}
		if(env.getElementAtPosition(myX-1, myY) != null && env.getElementAtPosition(myX-1, myY).equals(env.new TypeElement(Element.DEPOSIT))) {
			return Direction.WEST;
		}
		if(env.getElementAtPosition(myX, myY+1) != null && env.getElementAtPosition(myX, myY+1).equals(env.new TypeElement(Element.DEPOSIT))) {
			return Direction.SOUTH;
		}
		if(env.getElementAtPosition(myX, myY-1) != null && env.getElementAtPosition(myX, myY-1).equals(env.new TypeElement(Element.DEPOSIT))) {
			return Direction.NORTH;
		}
		return null;
	}
	
	private Pos findNearest(Environment env, int myX, int myY, Element element) {
		int dist = env.getHeight() + env.getWidth();
		List<Pos> allPos = new ArrayList<>();
		for(int x = 0; x < env.getWidth(); x++) {
			for(int y = 0; y < env.getHeight(); y++) {
				if(env.getElementAtPosition(x, y).equals(element)) {
					int currentDist = Math.abs(x - myX) + Math.abs(y - myY);
					if(currentDist == dist) {
						allPos.add(new Pos(x, y));
					}
					else if(currentDist < dist) {
						allPos.clear();
						allPos.add(new Pos(x, y));
						dist = currentDist;
					}
				}
			}
		}
		
		if(allPos != null && allPos.size() > 0) {
			Collections.shuffle(allPos);
			return allPos.get(0);
		}
		
		return null;
	}
	
	private void decide(Environment env, int myX, int myY) {
		// Drop whenever i can
		Direction canIDrop = canIDrop(env, myX, myY);
		if(this.wearBox && canIDrop != null) {
			this.goal = Goal.DROP;
			this.goalDirection = canIDrop;
			this.preference = null;
			return;
		}
		
		// Pick whenever i can
		Direction canIPick = canIPick(env, myX, myY);
		if(!this.wearBox && canIPick != null) {
			this.goal = Goal.PICK;
			this.goalDirection = canIPick;
			this.preference = null;
			return ;
		}
		
		// Move to my goal if i can
		Pos goal = (this.wearBox) ? findNearest(env, myX, myY, Element.DEPOSIT) : findNearest(env, myX, myY, this.agentType);
		Direction direction = findDirectionToReachPosition(env, myX, myY, goal.x, goal.y);
		if(direction != null) {
			this.goal = Goal.GOTO;
			this.goalDirection = direction;
			this.preference = null;
			return;
		}
		
		// Go to my favorite direction if i can
		if(preference != null && canIGo(env, myX, myY, preference)) {
			this.goal = Goal.GOTO;
			this.goalDirection = preference;
			return;
		}
		
		// waiting 1 time if my favorite direction is blocked by an agent
		if(preference != null && waitingTime < 1 &&
				(Element.AGENT_APPLE.equals(getElementAt(env, myX, myY, preference)) || Element.AGENT_MICRO.equals(getElementAt(env, myX, myY, preference)) || Element.AGENT_WITH_BOX.equals(getElementAt(env, myX, myY, preference)))) {
			this.goal = null;
			return;
		}
		
		// Find another direction
		direction = findOtherDirection(env, myX, myY, goal.x, goal.y);
		if(direction != null) {
			this.goal = Goal.GOTO;
			this.goalDirection = direction;
			this.preference = direction;
			return;
		}
		
		// Do nothing
		this.goal = null;
		return;
	}
	
	private Direction findDirectionToReachPosition(Environment env, int myX, int myY, int x, int y) {
		int deltaX = (x - myX != 0) ? (x - myX) / Math.abs(x - myX) : 0;
		int deltaY = (y - myY != 0) ? (y - myY) / Math.abs(y - myY) : 0;
		ArrayList<Direction> possibleDirection = new ArrayList<>();
		
		if(deltaX > 0) {
			possibleDirection.add(Direction.EAST);
		}
		if(deltaX < 0) {
			possibleDirection.add(Direction.WEST);
		}
		if(deltaY > 0) {
			possibleDirection.add(Direction.SOUTH);
		}
		if(deltaY < 0) {
			possibleDirection.add(Direction.NORTH);
		}
		
		if(possibleDirection.size() > 0) {
			Collections.shuffle(possibleDirection);
			for (Direction direction : possibleDirection) {
				if(canIGo(env, myX, myY, direction)) {
					return direction;
				}
			}
		}
		
		return null;
	}
	
	private Direction findOtherDirection(Environment env, int myX, int myY, int x, int y) {
		int deltaX = (x - myX != 0) ? (x - myX) / Math.abs(x - myX) : 0;
		int deltaY = (y - myY != 0) ? (y - myY) / Math.abs(y - myY) : 0;
		ArrayList<Direction> possibleDirection = new ArrayList<>();
		
		if(deltaX <= 0) {
			possibleDirection.add(Direction.EAST);
		}
		if(deltaX >= 0) {
			possibleDirection.add(Direction.WEST);
		}
		if(deltaY <= 0) {
			possibleDirection.add(Direction.SOUTH);
		}
		if(deltaY >= 0) {
			possibleDirection.add(Direction.NORTH);
		}
		
		if(possibleDirection.size() > 0) {
			Collections.shuffle(possibleDirection);
			for (Direction direction : possibleDirection) {
				if(canIGo(env, myX, myY, direction)) {
					return direction;
				}
			}
		}
		
		return null;
	}
	
	private void doGoal() {
		if(this.goal != null) {
			waitingTime = 0;
			switch (this.goal) {
			case DROP:
				requires().actions().drop(this.goalDirection);
				this.wearBox = false;
				break;
			case PICK:
				requires().actions().pick(this.goalDirection);
				this.wearBox = true;
				break;
			case GOTO:
				if (this.goalDirection != null) {
					switch (this.goalDirection) {
					case EAST:
						requires().actions().goRight();
						break;
					case NORTH:
						requires().actions().goUp();
						break;
					case SOUTH:
						requires().actions().goDown();
						break;
					case WEST:
						requires().actions().goLeft();
						break;
					}
				}
				break;
			}
		}
		else {
			waitingTime++;
		}
	}
	
	@Override
	protected ICycle make_cycle() {
		return new ICycle() {
			
			
			@Override
			public void doCyle() {
				//perceive
				Environment env = requires().perception().perceive();
				Integer myRobotX = env.getRobotX(agentId);
				Integer myRobotY = env.getRobotY(agentId);
				
				//decide
				decide(env, myRobotX, myRobotY);
				
				//act
				doGoal();
			}
			
			@Override
			public void afterCycle() {
			}
		};
	}

	@Override
	protected ILog make_log() {
		return new ILog() {
			
			@Override
			public List<Log> getTrace() {
				return new ArrayList<>();
			}
			
			@Override
			public void clear() {
			}
		};
	}
	
	/* internal representation of the position */
	private class Pos {
		public int x;
		public int y;
		
		public Pos(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
	
	private enum Goal {
		GOTO, DROP, PICK;
	}
	

}
