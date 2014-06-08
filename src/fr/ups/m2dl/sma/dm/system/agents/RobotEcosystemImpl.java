package fr.ups.m2dl.sma.dm.system.agents;

import java.util.ArrayList;
import java.util.List;

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
	private List<Robot> agents = new ArrayList<>();
	private List<Log> logs = new ArrayList<>();
	
	@Override
	protected ICycle make_cycle() {
		//TODO: maybe use threads
		return new ICycle() {
			
			@Override
			public void doCyle() {
				logs.add(new Log("RobotEcosystem", "start cycle"));
				for (Robot agent : agents) {
					agent.provides().cycle().doCyle();
				}
				logs.add(new Log("RobotEcosystem", "end cycle"));
			}
			
			@Override
			public void afterCycle() {
				logs.add(new Log("RobotEcosystem", "start after cycle"));
				for (Robot agent : agents) {
					agent.provides().cycle().afterCycle();
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
				return logs;
			}
			
			@Override
			public void clear() {
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
				return new PerceiveDecidActBehaviourImpl(identifier);
			}
		};
		
		agents.add(agent);
		return agent;
	}

}
