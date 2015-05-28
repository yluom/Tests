package fr.ups.m2dl.sma.dm.system.agents;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import fr.ups.m2dl.sma.dm.system.environment.Environment;
import fr.ups.m2dl.sma.dm.system.environment.Environment.ColorType;
import fr.ups.m2dl.sma.dm.system.environment.Environment.Element;
import fr.ups.m2dl.sma.dm.system.environment.Environment.TypeElement;
import fr.ups.m2dl.sma.dm.system.environment.ILocalEnvironmentSet.Direction;
import fr.ups.m2dl.sma.dm.system.log.ILog;
import fr.ups.m2dl.sma.dm.system.log.Log;
import fr.ups.m2dl.sma.dm.system.process.ICycle;

public class AgentDecisionFarFromAreasImpl extends AgentDecision {

	private final List<Log> logs;
	private final String agentId;
	private final ColorType agentType;
	private final Random rand;

	public AgentDecisionFarFromAreasImpl(String agentId, ColorType agentType) {
		this.agentId = agentId;
		this.agentType = agentType;
		this.rand = new Random();
		this.logs = new ArrayList<>();
	}

	@Override
	protected ICycle make_cycle() {
		return new ICycle() {

			@Override
			public void doCyle() {
				Environment env = requires().perception().perceive();

				Integer myRobotX = env.getRobotX(agentId);
				Integer myRobotY = env.getRobotY(agentId);

				// Si loin et pas caisse, close up de zone des caisses
				// 2 cas: si robot porte pas de casse et si porte
				if (!isRobotCarryingBox(env, myRobotX, myRobotY)) {
					// If far from stocking base go to
//					if (isFarFromBoxStocking(env, myRobotX)) {
//						closeUpToBoxStocking(env, myRobotX, myRobotY);
//					} else {
						// If there is a box near to the robot
						Direction nearBox = directionOfNearBox(env, myRobotX,
								myRobotY);
						if (nearBox != null) {
							requires().actions().pick(nearBox);
						} else {
							randomDeplacement();
						}
//					}
				} else {
					if (isFarFromBaseStocking(env, myRobotX)) {
						closeUpToStockingBase(env, myRobotX, myRobotY);
					} else {

						// If there is a deposit near to the robot
						Direction nearDeposit = directionOfNearDeposit(env,
								myRobotX, myRobotY);
						if (nearDeposit != null) {
							requires().actions().drop(nearDeposit);
						} else {
							randomDeplacement();
						}
					}
				}
			}

			@Override
			public void afterCycle() {
			}
		};
	}

	public boolean isRobotCarryingBox(Environment env, int robotX, int robotY) {
 		return env.getElementAtPosition(robotX, robotY).equals(
				env.new TypeElement(Element.AGENT_WITH_BOX));
	}

	public boolean isFarFromBoxStocking(Environment env, int robotX) {
		if (robotX == 0) {
			return true;
		} else {
			float percentage = (float) env.getWidth() / (float) robotX;
			return percentage > 1.1;
		}
	}

	public boolean isFarFromBaseStocking(Environment env, int robotX) {
		if (robotX == 0)
			return false;
		else {
			float percentage = (float) env.getWidth() / (float) robotX;
			return percentage < 10;
		}
	}

	public void closeUpToBoxStocking(Environment env, int robotX, int robotY) {
		TypeElement rightElement = env.getElementAtPosition(robotX + 1, robotY);
		if (isBlockedQueue(env, robotX, robotY)) {
			requires().actions().goLeft();
		}
		// TODO Leo deplacement random des robots
		/* else if (rightElement.equals(Element.EMPTY)
				|| rightElement.equals(Element.DEPOSIT)) {
			requires().actions().goRight();
		} */else {
			randomDeplacement();
		}
	}

	public void closeUpToStockingBase(Environment env, int robotX, int robotY) {
		TypeElement leftElement = env.getElementAtPosition(robotX - 1, robotY);

		if (leftElement.equals(env.new TypeElement(Element.EMPTY))
				|| leftElement.equals(env.new TypeElement(Element.DEPOSIT))) {
			requires().actions().goLeft();
		} else {
			randomDeplacement();
		}
	}

	// Check if Agents without box are "bloqued"
	public boolean isBlockedQueue(Environment env, int robotX, int robotY) {
		TypeElement rightElement = env.getElementAtPosition(robotX + 1, robotY);
		TypeElement right2Element = env.getElementAtPosition(robotX + 2, robotY);

		return 	(rightElement.equals(env.new TypeElement(Element.AGENT_APPLE))
				&& right2Element.equals(env.new TypeElement(Element.AGENT_APPLE))) ||
				(rightElement.equals(env.new TypeElement(Element.AGENT_MICRO))
						&& right2Element.equals(env.new TypeElement(Element.AGENT_MICRO)));
	}

	// Check if can pick a box
	public Direction directionOfNearBox(Environment env, int robotX, int robotY) {
		TypeElement elmt = env.new TypeElement(this.agentType);
		return directionOfElement(env, robotX, robotY, elmt);
	}

	public Direction directionOfNearDeposit(Environment env, int robotX,
			int robotY) {
		TypeElement elmt = env.new TypeElement(Element.DEPOSIT);
		return directionOfElement(env, robotX, robotY, elmt);
	}

	private Direction directionOfElement(Environment env, int robotX,
			int robotY, TypeElement element) {
		TypeElement leftElement = env.getElementAtPosition(robotX - 1, robotY);
		TypeElement topElement = env.getElementAtPosition(robotX, robotY - 1);
		TypeElement rightElement = env.getElementAtPosition(robotX + 1, robotY);
		TypeElement bottomElement = env.getElementAtPosition(robotX, robotY + 1);

		if (leftElement != null && leftElement.equals(element)) {
			return Direction.WEST;
		}
		if (topElement != null && topElement.equals(element)) {
			return Direction.NORTH;
		}
		if (rightElement != null && rightElement.equals(element)) {
			return Direction.EAST;
		}
		if (bottomElement != null && bottomElement.equals(element)) {
			return Direction.SOUTH;
		}

		return null;
	}

	public void randomDeplacement() {
		switch (rand.nextInt(4)) {
		// act
		case 0:
			logs.add(new Log("Agent " + agentId, "try to go down"));
			requires().actions().goDown();
			break;
		case 1:
			logs.add(new Log("Agent " + agentId, "try to go left"));
			requires().actions().goLeft();
			break;
		case 2:
			logs.add(new Log("Agent " + agentId, "try to go right"));
			requires().actions().goRight();
			break;
		case 3:
			logs.add(new Log("Agent " + agentId, "try to go up"));
			requires().actions().goUp();
			break;
		}
	}

	@Override
	protected ILog make_log() {
		return new ILog() {

			@Override
			public List<Log> getTrace() {
				return logs;
			}

			@Override
			public void clear() {
				logs.clear();
			}
		};
	}

}
