package fr.ups.m2dl.sma.dm.system.agents;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import fr.ups.m2dl.sma.dm.system.environment.Environment.ColorType;
import fr.ups.m2dl.sma.dm.system.environment.Environment.Element;
import fr.ups.m2dl.sma.dm.system.environment.Environment.TypeElement;
import fr.ups.m2dl.sma.dm.system.environment.ILocalEnvironmentGet;
import fr.ups.m2dl.sma.dm.system.environment.ILocalEnvironmentSet;
import fr.ups.m2dl.sma.dm.system.log.ILog;
import fr.ups.m2dl.sma.dm.system.log.Log;
import fr.ups.m2dl.sma.dm.system.process.ICycle;
import fr.ups.m2dl.sma.dm.utils.Joining;
import fr.ups.m2dl.sma.dm.utils.JoiningImpl;

/**
 * @author SERIN Kevin
 *
 */
public class RobotEcosystemImpl extends RobotsEcosystem {
	private List<Robot.Component> agents = new ArrayList<>();
	private List<Log> logs = new ArrayList<>();
	
	@Override
	protected ICycle make_cycle() {
		//TODO: maybe use threads
		return new ICycle() {
			
			@Override
			public void doCyle() {
				logs.add(new Log("RobotEcosystem", "start cycle"));
				for (Robot.Component agent : agents) {
					agent.cycle().doCyle();
				}
				logs.add(new Log("RobotEcosystem", "end cycle"));
			}
			
			@Override
			public void afterCycle() {
				logs.add(new Log("RobotEcosystem", "start after cycle"));
				for (Robot.Component agent : agents) {
					agent.cycle().afterCycle();
				}
				logs.add(new Log("RobotEcosystem", "end after cycle"));
			}
		};
	}

	@Override
	protected ILog make_log() {
		return new ILog() {
			
			@Override
			public List<Log> getTrace() {
				ArrayList<Log> allLogs = new ArrayList<>();
				for (Robot.Component agent : agents) {
					allLogs.addAll(agent.log().getTrace());
				}
				allLogs.addAll(logs);
				return allLogs;
			}
			
			@Override
			public void clear() {
				for (Robot.Component agent : agents) {
					agent.log().clear();
				}
				logs.clear();
			}
		};
	}

	@Override
	protected Joining<ILocalEnvironmentGet> make_joiningGet() {
		return new JoiningImpl<>();
	}

	@Override
	protected Joining<ILocalEnvironmentSet> make_joiningSet() {
		return new JoiningImpl<>();
	}

	@Override
	protected Robot make_Robot(final String identifier) {
		logs.add(new Log("RobotEcosystem", "create agent: "+identifier));
		Robot agent = new Robot() {
			@Override
			protected AgentBehaviourPDA make_behaviour() {
				Random random = new Random();
			    boolean typeOfRobot = random.nextBoolean();
			    ColorType eltRobot = (typeOfRobot) ? ColorType.BOXA : ColorType.BOXB;
				return new PerceiveDecidActBehaviourImpl(identifier, eltRobot);
			}
		};
		return agent;
	}

	@Override
	protected IConfigEcosystem make_config() {
		return new IConfigEcosystem() {
			
			@Override
			public void initialize(Collection<String> agentsToManage) {
				for (String agentId : agentsToManage) {
					Robot.Component robot = newRobot(agentId);
					agents.add(robot);
				}
			}
		};
	}

}
