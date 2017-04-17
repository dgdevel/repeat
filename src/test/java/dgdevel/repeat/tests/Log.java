package dgdevel.repeat.tests;

import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;

public class Log {
	public static void conf() {
		Properties p = new Properties();
		p.setProperty("log4j.rootLogger","ALL, consoleApp");
		p.setProperty("log4j.appender.consoleApp","org.apache.log4j.ConsoleAppender");
		p.setProperty("log4j.appender.consoleApp.layout","org.apache.log4j.PatternLayout");
		p.setProperty("log4j.appender.consoleApp.layout.ConversionPattern","[%d] %-2p [%t] %c - %m%n");
		PropertyConfigurator.configure(p);
	}

}
