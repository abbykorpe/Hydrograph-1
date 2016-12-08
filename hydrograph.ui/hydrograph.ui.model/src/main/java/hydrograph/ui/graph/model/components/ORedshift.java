package hydrograph.ui.graph.model.components;

import hydrograph.ui.graph.model.categories.OutputCategory;

public class ORedshift extends OutputCategory {

	/**
	 * Instantiates a new Output Oracle.
	 */
	// private static final long serialVersionUID = 4125235583848446880L;
	public ORedshift() {
		super();
	}

	public String getConverter() {
		return "hydrograph.ui.engine.converter.impl.OutputRedshiftConverter";

	}
}