package com.bitwise.app.logging.factory;


import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

import org.eclipse.core.runtime.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.util.Loader;

public class LogFactory {
	final public String CLASSIC_FILE = "logback.xml";
    final public String LOG_DIR = "config/logger/";
    
    private static final Logger loggers = LoggerFactory.getLogger(LogFactory.class);
    public static final LogFactory INSTANCE = new LogFactory();
    
    private LogFactory(){
    	writeLogsOnFileAndConsole();
    }
    
    public Logger getLogger(Class<?> clazz){
    	return LoggerFactory.getLogger(clazz.getName());
    }
    
	private void writeLogsOnFileAndConsole() {
		loggers.debug("****Configuring Logger****");
        try {
	            ClassLoader loader = new URLClassLoader(new URL[]
	            		{new File(Platform.getInstallLocation().getURL().getPath() + LOG_DIR).toURI().toURL()});
	            LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
	            URL url = Loader.getResource(CLASSIC_FILE, loader);
	            if (url != null) {
	                JoranConfigurator configurator = new JoranConfigurator();
	                configurator.setContext(lc);
	                lc.reset();
	                configurator.doConfigure(url);
	                lc.start();
            }
            loggers.debug("****Logger Configured Successfully****");
        } catch(Exception exception){
        	loggers.error("Failed to configure the logger {}", exception);
        }
    }

}

