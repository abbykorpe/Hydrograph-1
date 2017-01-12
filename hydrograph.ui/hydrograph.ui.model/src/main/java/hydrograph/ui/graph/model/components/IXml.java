package hydrograph.ui.graph.model.components;

import hydrograph.ui.graph.model.Component;

/**
 * Model class for Input XML component 
 * @author Bitwise
 */
public class IXml extends Component {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2171620373618501184L;

	@Override
	public String getConverter() {
		return "hydrograph.ui.engine.converter.impl.InputXMLConverter";
	}

}
