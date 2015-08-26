package com.bitwise.app.project.structure;

import org.eclipse.osgi.util.NLS;

public class CustomMessages extends NLS {
	private static final String BUNDLE_NAME = "com.bitwise.app.project.structure.messages"; //$NON-NLS-1$
	public static String CustomWizard_CREATE_ETL_PROJECT;
	public static String CustomWizard_ENTER_PROJECT_NAME;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, CustomMessages.class);
	}

	private CustomMessages() {
	}
}
