package tst.jmx.log4j;

import java.util.List;

public interface Log4jConfigurationManagerMXBean {
	/**
	 * list of all the logger names and their levels
	 */
	List<String> getLoggers();

	/**
	 * Get the log level for a given logger
	 */
	String getLogLevel(String logger);

	/**
	 * Set the log level for a given logger
	 */
	void setLogLevel(String logger, String level);
}
