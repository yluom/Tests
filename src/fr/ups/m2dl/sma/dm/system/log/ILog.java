/**
 * 
 */
package fr.ups.m2dl.sma.dm.system.log;

import java.util.List;



public interface ILog {
	/**
	 * @return The list of logs for the last cycle. 
	 */
	List<Log> getTrace();
	
	/**
	 * Clear the logs
	 */
	void clear();
}
