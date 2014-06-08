/**
 * 
 */
package fr.ups.m2dl.sma.dm.system.agents;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import fr.ups.m2dl.sma.dm.system.environment.Environment;
import fr.ups.m2dl.sma.dm.system.log.ILog;
import fr.ups.m2dl.sma.dm.system.log.Log;
import fr.ups.m2dl.sma.dm.system.process.ICycle;

/**
 * @author SERIN Kevin
 *
 */
public class RandomAgentDecisionImpl extends AgentDecision {
	private String agentId;
	private List<Log> logs;
	private Random rand;
	
	public RandomAgentDecisionImpl(String agentId) {
		this.agentId = agentId;
		this.rand = new Random();
		this.logs = new ArrayList<>();
	}
	

	@Override
	protected ICycle make_cycle() {
		return new ICycle() {
			
			@Override
			public void doCyle() {
				//perceive
				@SuppressWarnings("unused")
				Environment env = requires().perception().perceive();
				
				//decide (random)
				switch(rand.nextInt(4)) {
				//act
				case 0:
					logs.add(new Log("Agent "+agentId, "try to go down"));
					requires().actions().goDown();
					break;
				case 1:
					logs.add(new Log("Agent "+agentId, "try to go left"));
					requires().actions().goLeft();
					break;
				case 2:
					logs.add(new Log("Agent "+agentId, "try to go right"));
					requires().actions().goRight();
					break;
				case 3:
					logs.add(new Log("Agent "+agentId, "try to go up"));
					requires().actions().goUp();
					break;
				}
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
				return logs;
			}
			
			@Override
			public void clear() {
				logs.clear();
			}
		};
	}

}
