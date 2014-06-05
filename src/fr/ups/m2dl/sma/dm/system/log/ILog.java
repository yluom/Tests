/**
 * 
 */
package fr.ups.m2dl.sma.dm.system.log;

import java.util.List;


/**
 * Port for logging
 * @author SERIN Kevin
 *
 */
public interface ILog {
	/**
	 * @return The list of logs for the last cycle. 
	 */
	List<Log> getTrace();
}
