package tst.jmx.log4j;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@ManagedResource(objectName = "myapp:type=logging,name=CustomLog4jConfig")
//don't need an MXBean interface if exposing using spring
//public class Log4jConfigurator implements Log4jConfigurationManagerMXBean {
public class Log4jConfigurationManager {
	
	@ManagedAttribute(description="Configured loggers")
	public List<String> getLoggers() {
		List<String> list = new ArrayList<String>();
		for (Enumeration<?> e = LogManager.getCurrentLoggers(); e.hasMoreElements();) {
			Logger log = (Logger) e.nextElement();
			if (log.getLevel() != null) {
				list.add(log.getName() + " = " + log.getLevel().toString());
			}
		}
		return list;
	}

	@ManagedOperation(description="Get logger's log level")
	public String getLogLevel(String logger) {
		String level = "unavailable";

		if (StringUtils.hasText(logger)) {
			Logger log = Logger.getLogger(logger);

			if (log != null) {
				level = log.getLevel().toString();
			}
		}
		return level;
	}

	@ManagedOperation(description="Set logger's log level")
	public void setLogLevel(String logger, String level) {
		if (StringUtils.hasText(logger) && StringUtils.hasText(level)) {
			Logger log = Logger.getLogger(logger);

			if (log != null) {
				log.setLevel(Level.toLevel(level.toUpperCase()));
			}
		}
	}

}