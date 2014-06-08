/**
 * 
 */
package fr.ups.m2dl.sma.dm.system.environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import fr.ups.m2dl.sma.dm.system.components.environment.EnvironmentRepresentation;
import fr.ups.m2dl.sma.dm.system.environment.Environment.Element;
import fr.ups.m2dl.sma.dm.system.environment.Environment.PassageWay;
import fr.ups.m2dl.sma.dm.system.log.ILog;
import fr.ups.m2dl.sma.dm.system.log.Log;

/**
 * @author SERIN Kevin
 *
 */
public class EnvironmentRepresentationImpl extends EnvironmentRepresentation {
	private Environment environment;
	private ArrayList<Log> logs = new ArrayList<>();
	
	
	@Override
	protected IGlobalEnvironmentGet make_globalGet() {
		return new IGlobalEnvironmentGet() {
			@Override
			public Environment get() {
				return EnvironmentRepresentationImpl.this.environment;
			}
		};
	}

	@Override
	protected IGlobalEnvironmentSet make_globalSet() {
		return new IGlobalEnvironmentSet() {
			@Override
			public void set(Environment environment) {
				EnvironmentRepresentationImpl.this.logs.add(new Log("Environment", "Global change of the environment"));
				EnvironmentRepresentationImpl.this.environment = environment;
			}
		};
	}

	@Override
	protected ILocalEnvironmentGet make_localGet() {
		return new ILocalEnvironmentGet() {
			@Override
			public Environment get(String agentIdentifier) {
				int x = EnvironmentRepresentationImpl.this.environment.getRobotX(agentIdentifier);
				int y = EnvironmentRepresentationImpl.this.environment.getRobotY(agentIdentifier);
				return EnvironmentRepresentationImpl.this.environment.getLocalRepresentation(x, y);
			}
		};
	}

	@Override
	protected ILocalEnvironmentSet make_localSet() {
		return new ILocalEnvironmentSet() {
			@Override
			public boolean pickBox(String agentIdentifier, Direction direction) {
				int x = EnvironmentRepresentationImpl.this.environment.getRobotX(agentIdentifier);
				int y = EnvironmentRepresentationImpl.this.environment.getRobotY(agentIdentifier);
				switch (direction) {
				case EAST:
					x += 1;
					break;
				case NORTH:
					y -= 1;
					break;
				case SOUTH:
					y+= 1;
					break;
				case WEST:
					x -= 1;
					break;
				}
				Element element = EnvironmentRepresentationImpl.this.environment.getElementAtPosition(x, y);
				if(element != null && element.equals(Element.BOX)) {
					if(EnvironmentRepresentationImpl.this.environment.removeBox(x, y)) {
						EnvironmentRepresentationImpl.this.environment.setRobotState(agentIdentifier, true);
						EnvironmentRepresentationImpl.this.logs.add(new Log("Environment", "pick box at "+x+";"+y));
						return true;
					}
				}
				EnvironmentRepresentationImpl.this.logs.add(new Log("Environment", "fail pick box at "+x+";"+y));
				return false;
			}
			
			@Override
			public boolean dropBox(String agentIdentifier, Direction whereToDrop) {
				int x = EnvironmentRepresentationImpl.this.environment.getRobotX(agentIdentifier);
				int y = EnvironmentRepresentationImpl.this.environment.getRobotY(agentIdentifier);
				switch (whereToDrop) {
				case EAST:
					x += 1;
					break;
				case NORTH:
					y -= 1;
					break;
				case SOUTH:
					y+= 1;
					break;
				case WEST:
					x -= 1;
					break;
				}
				Element element = EnvironmentRepresentationImpl.this.environment.getElementAtPosition(x, y);
				if(element != null && element.equals(Element.EMPTY)) {
					if(EnvironmentRepresentationImpl.this.environment.addBox(x, y)) {
						EnvironmentRepresentationImpl.this.environment.setRobotState(agentIdentifier, false);
						EnvironmentRepresentationImpl.this.logs.add(new Log("Environment", "drop box at "+x+";"+y));
						return true;
					}
				}
				EnvironmentRepresentationImpl.this.logs.add(new Log("Environment", "fail drop box at "+x+";"+y));
				return false;
			}
			
			@Override
			public boolean changePosition(String agentIdentifier, Direction direction) {
				int x = EnvironmentRepresentationImpl.this.environment.getRobotX(agentIdentifier);
				int y = EnvironmentRepresentationImpl.this.environment.getRobotY(agentIdentifier);
				int newX = x;
				int newY = y;
				switch (direction) {
				case EAST:
					newX += 1;
					break;
				case NORTH:
					newY -= 1;
					break;
				case SOUTH:
					newY += 1;
					break;
				case WEST:
					newX -= 1;
					break;
				}
				Element element = EnvironmentRepresentationImpl.this.environment.getElementAtPosition(newX, newY);
				if(element != null && element.equals(Element.EMPTY)) {
					if(EnvironmentRepresentationImpl.this.environment.moveRobot(x, y, newX, newY)) {
						EnvironmentRepresentationImpl.this.logs.add(new Log("Environment", "move "+x+";"+y+" to "+newX+";"+newY));
						return true;
					}
				}
				EnvironmentRepresentationImpl.this.logs.add(new Log("Environment", "fail move "+x+";"+y+" to "+newX+";"+newY));
				return false;
			}
		};
	}

	@Override
	protected IConfigEnvironment make_config() {
		return new IConfigEnvironment() {
			
			@Override
			public boolean moveUpPassageWay(PassageWay passage) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean moveDownPassageWay(PassageWay passage) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public Environment initialize(int nbAgent, int nbBox) {
				EnvironmentRepresentationImpl.this.environment = new Environment(nbAgent, nbBox);
				EnvironmentRepresentationImpl.this.logs.add(new Log("Environment", "Initialize with "+nbAgent+" agents and "+nbBox+" boxes"));
				return EnvironmentRepresentationImpl.this.environment;
			}
		};
	}

	@Override
	protected ILog make_log() {
		return new ILog() {
			
			@Override
			public List<Log> getTrace() {
				return EnvironmentRepresentationImpl.this.logs;
			}
			
			@Override
			public void clear() {
				EnvironmentRepresentationImpl.this.logs.clear();
			}
		};
	}

	@Override
	protected IPersist make_persist() {
		return new IPersist() {
			
			@Override
			public boolean persist(String fileName) {
				FileOutputStream fos = null;
				ObjectOutputStream out = null;
				try {
					
					File file = new File(fileName);
					fos = new FileOutputStream(file);
					out = new ObjectOutputStream(fos);
					out.writeObject(EnvironmentRepresentationImpl.this.environment);
				} catch (FileNotFoundException e) {
					return false;
				} catch (IOException e1) {
					return false;
				} finally {
					try {
						if(out != null) {
							out.close();
						}
						if(fos != null) {
							fos.close();
						}
					} catch (IOException e) {
					}
				}
				return true;
			}
		};
	}

}
