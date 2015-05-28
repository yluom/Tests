/**
 * 
 */
package fr.ups.m2dl.sma.dm.system.log;

import java.util.List;

import fr.ups.m2dl.sma.dm.utils.Dispatcher2;


public class PrinterDispatcherImpl extends Dispatcher2<ILog> {

	@Override
	protected ILog make_from() {
		return new ILog() {
			
			@Override
			public List<Log> getTrace() {
				List<Log> trace1 = requires().to1().getTrace();
				List<Log> trace2 = requires().to2().getTrace();
				
				trace1.addAll(trace2);
				return trace1;
			}

			@Override
			public void clear() {
				requires().to1().clear();
				requires().to2().clear();
			}
		};
	}
}
