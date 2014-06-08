/**
 * 
 */
package fr.ups.m2dl.sma.dm.system.log;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import fr.ups.m2dl.sma.dm.system.components.log.Printer;
import fr.ups.m2dl.sma.dm.system.process.ICycle;

/**
 * Printer for the standard output
 * @author SERIN Kevin
 *
 */
public class SimplePrinterImpl extends Printer {

	@Override
	protected ICycle make_cycle() {
		return new ICycle() {
			
			@Override
			public void doCyle() {
				//Do nothing during the cycle, waiting the end of the cycle to print logs of the passed cycle
			}
			
			@Override
			public void afterCycle() {
				List<Log> traces = requires().logs().getTrace();
				
				//sort logs by date
				Collections.sort(traces, new Comparator<Log>() {
					@Override
					public int compare(Log o1, Log o2) {
						return o1.getDate().compareTo(o2.getDate());
					}
				});
				
				// print all traces
				for (Log log : traces) {
					System.out.println(log.getDate()+"\t"+log.getAuthor()+"\t"+log.getMessage());
				}
				
				// clear printed traces
				requires().logs().clear();
			}
		};
	}
}
