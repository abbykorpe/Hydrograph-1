package hydrograph.ui.engine.converter.impl;

import java.util.List;

import hydrograph.engine.jaxb.commontypes.TypeBaseField;
import hydrograph.engine.jaxb.commontypes.TypeInputOutSocket;
import hydrograph.ui.datastructure.property.GridRow;
import hydrograph.ui.engine.converter.InputConverter;
import hydrograph.ui.graph.model.Component;

/**
 * Converter for Xml Input component
 * @author Bitwise
 */
public class InputXmlConverter extends InputConverter {

	public InputXmlConverter(Component comp) {
		super(comp);
	}

	@Override
	protected List<TypeInputOutSocket> getInOutSocket() {
		return null;
	}

	@Override
	protected List<TypeBaseField> getFieldOrRecord(List<GridRow> list) {
		return null;
	}
}
