/**
 * 
 */
package fr.ups.m2dl.sma.dm.system;

import fr.ups.m2dl.sma.dm.system.agents.RobotEcosystemImpl;
import fr.ups.m2dl.sma.dm.system.agents.RobotsEcosystem;
import fr.ups.m2dl.sma.dm.system.components.configuration.Configurator;
import fr.ups.m2dl.sma.dm.system.components.environment.EnvironmentRepresentation;
import fr.ups.m2dl.sma.dm.system.components.log.Printer;
import fr.ups.m2dl.sma.dm.system.components.network.NetworkCycleManager;
import fr.ups.m2dl.sma.dm.system.components.process.Controller;
import fr.ups.m2dl.sma.dm.system.components.process.CycleManager;
import fr.ups.m2dl.sma.dm.system.components.system.System;
import fr.ups.m2dl.sma.dm.system.configuration.ConfiguratorImpl;
import fr.ups.m2dl.sma.dm.system.environment.EnvironmentRepresentationImpl;
import fr.ups.m2dl.sma.dm.system.log.ILog;
import fr.ups.m2dl.sma.dm.system.log.PrinterDispatcherImpl;
import fr.ups.m2dl.sma.dm.system.log.SimplePrinterImpl;
import fr.ups.m2dl.sma.dm.system.network.DummyNetworkCycleManagerImpl;
import fr.ups.m2dl.sma.dm.system.process.ControllerImpl;
import fr.ups.m2dl.sma.dm.system.process.CycleDispatcherImpl;
import fr.ups.m2dl.sma.dm.system.process.CycleManagerImpl;
import fr.ups.m2dl.sma.dm.system.process.ICycle;
import fr.ups.m2dl.sma.dm.utils.Dispatcher2;
import fr.ups.m2dl.sma.dm.utils.Dispatcher3;

/**
 * @author SERIN Kevin
 *
 */
public class SystemImpl extends System {

	@Override
	protected NetworkCycleManager make_networkManager() {
		return new DummyNetworkCycleManagerImpl();
	}

	@Override
	protected EnvironmentRepresentation make_environment() {
		return new EnvironmentRepresentationImpl();
	}

	@Override
	protected RobotsEcosystem make_ecox() {
		return new RobotEcosystemImpl();
	}

	@Override
	protected Dispatcher2<ILog> make_logDispatcher() {
		return new PrinterDispatcherImpl();
	}

	@Override
	protected Printer make_printer() {
		return new SimplePrinterImpl();
	}

	@Override
	protected Dispatcher3<ICycle> make_cycleDispatcher() {
		return new CycleDispatcherImpl();
	}

	@Override
	protected CycleManager make_cycleManager() {
		return new CycleManagerImpl();
	}
	
	@Override
	protected Controller make_controller() {
		return new ControllerImpl();
	}

	@Override
	protected Configurator make_configurator() {
		return new ConfiguratorImpl();
	}

}
