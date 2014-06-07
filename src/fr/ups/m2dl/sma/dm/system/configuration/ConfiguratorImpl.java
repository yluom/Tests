/**
 * 
 */
package fr.ups.m2dl.sma.dm.system.configuration;

import fr.ups.m2dl.sma.dm.system.components.configuration.Configurator;

/**
 * @author SERIN Kevin
 *
 */
public class ConfiguratorImpl extends Configurator {
	
	@Override
	protected IConfig make_config() {
		return new IConfig() {
			
			@Override
			public void initialize(int nbAgent, int nbBoxes) {
				requires().envConfig().initialize(nbAgent, nbBoxes);
			}
		};
	}

}
