/**
 * 
 */
package fr.ups.m2dl.sma.dm.utils;

/**
 * @author SERIN Kevin
 *
 */
public class JoiningImpl<T> extends Joining<T> {

	@Override
	protected fr.ups.m2dl.sma.dm.utils.Joining.JoiningEntity<T> make_JoiningEntity() {
		return new JoiningEntity<T>() {

			@Override
			protected T make_prov() {
				return eco_requires().req();
			}
		};
	}

}
