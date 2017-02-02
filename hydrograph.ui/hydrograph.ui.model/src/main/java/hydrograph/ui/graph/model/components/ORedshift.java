package hydrograph.ui.graph.model.components;

import hydrograph.ui.graph.model.categories.OutputCategory;

public class ORedshift extends OutputCategory {

	private static final long serialVersionUID = 4823508502009659559L;

	/**
	 * Instantiates a new Output Oracle.
	 */
	public ORedshift() {
		super();
	}

	public String getConverter() {
		return "hydrograph.ui.engine.converter.impl.OutputRedshiftConverter";

	}
}