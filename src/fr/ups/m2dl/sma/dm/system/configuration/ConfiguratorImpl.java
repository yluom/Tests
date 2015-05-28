/**
 * 
 */
package fr.ups.m2dl.sma.dm.system.configuration;

import fr.ups.m2dl.sma.dm.system.components.configuration.Configurator;
import fr.ups.m2dl.sma.dm.system.environment.Environment;

public class ConfiguratorImpl extends Configurator {
	
	@Override
	protected IConfig make_config() {
		return new IConfig() {
			
			@Override
			public void initialize(int nbAgent, int nbBoxes) {
				Environment environment = requires().envConfig().initialize(nbAgent, nbBoxes);
				requires().agentConfig().initialize(environment.getRobots());
			}
		};
	}

}
