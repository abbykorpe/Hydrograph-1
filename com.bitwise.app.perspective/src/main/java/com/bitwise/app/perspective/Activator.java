package com.bitwise.app.perspective;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;

import com.bitwise.app.logging.factory.LogFactory;

// TODO: Auto-generated Javadoc
/**
 * The activator class controls the plug-in life cycle
 */

public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.bitwise.app.perspective"; //$NON-NLS-1$
	private Logger logger = LogFactory.INSTANCE.getLogger(Activator.class);

	// The shared instance
	private static Activator plugin;

	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;

		logger.info("----System Properties----");
		logger.info("Operating System : {}", System.getProperty("os.name"));
		logger.info("JVM : {}", System.getProperty("java.vm.name"));
		logger.info("java specification version : {}",
				System.getProperty("java.specification.version"));
		logger.info("Java Version : {}", System.getProperty("java.version"));
		logger.info("Osgi OS : {}", System.getProperty("osgi.os"));
		logger.info("Operating System Version : {}",
				System.getProperty("os.version"));
		logger.info("Operating System Architecture : {}",
				System.getProperty("os.arch"));
		/*Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();
		logger.debug("Screen Width : {}", Double.toString(width));
		double height = screenSize.getHeight();
		logger.debug("Screen Height : {}", Double.toString(height));

		int resolution = Toolkit.getDefaultToolkit().getScreenResolution();
		logger.debug("Screen Resolution :{}", Integer.toString(resolution)
				.concat(" dpi"));*/

		Runtime runtime = Runtime.getRuntime();

		long maxMemory = runtime.maxMemory();
		logger.debug("Max Memory: {}", Long.toString(maxMemory / 1024));
		long allocatedMemory = runtime.totalMemory();
		logger.debug("Allocated Memory:  {}",
				Long.toString(allocatedMemory / 1024));
		long freeMemory = runtime.freeMemory();
		logger.debug("Free Memory: {}", Long.toString(freeMemory / 1024));
		logger.debug("Total free memory: {}", Long
				.toString((freeMemory + (maxMemory - allocatedMemory)) / 1024));
		long used = Runtime.getRuntime().totalMemory()
				- Runtime.getRuntime().freeMemory();
		logger.debug("Used Memory : {}", Long.toString(used));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given plug-in
	 * relative path
	 * 
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
}
