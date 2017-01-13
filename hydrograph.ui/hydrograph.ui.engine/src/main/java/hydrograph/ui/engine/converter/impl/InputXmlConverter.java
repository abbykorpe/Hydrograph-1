package hydrograph.ui.engine.converter.impl;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import hydrograph.engine.jaxb.commontypes.TypeBaseField;
import hydrograph.engine.jaxb.commontypes.TypeInputOutSocket;
import hydrograph.engine.jaxb.ifxml.TypeInputXmlOutSocket;
import hydrograph.engine.jaxb.inputtypes.XmlFile;
import hydrograph.engine.jaxb.inputtypes.XmlFile.AbsoluteXPath;
import hydrograph.engine.jaxb.inputtypes.XmlFile.RootTag;
import hydrograph.engine.jaxb.inputtypes.XmlFile.RowTag;
import hydrograph.ui.common.util.Constants;
import hydrograph.ui.datastructure.property.GridRow;
import hydrograph.ui.datastructure.property.XPathGridRow;
import hydrograph.ui.engine.constants.PropertyNameConstants;
import hydrograph.ui.engine.converter.InputConverter;
import hydrograph.ui.graph.model.Component;
import hydrograph.ui.graph.model.Link;
import hydrograph.ui.logging.factory.LogFactory;

/**
 * Converter for Xml Input component
 * @author Bitwise
 */
public class InputXmlConverter extends InputConverter {
	private static final Logger logger = LogFactory.INSTANCE.getLogger(InputXmlConverter.class);
	
	public InputXmlConverter(Component component) {
		super(component);
		this.baseComponent = new XmlFile();
		this.component = component;
		this.properties = component.getProperties();
	}

	@Override
	public void prepareForXML() {
		super.prepareForXML();
		XmlFile xmlFile = (XmlFile) baseComponent;
		
		XmlFile.Path path = new XmlFile.Path();
		path.setUri((String) properties.get(PropertyNameConstants.PATH.value()));
		xmlFile.setPath(path);
		
		XmlFile.AbsoluteXPath absoluteXPath = new AbsoluteXPath();
		absoluteXPath.setValue((String) properties.get(PropertyNameConstants.XPATH_QUERY.value()));
		xmlFile.setAbsoluteXPath(absoluteXPath);
	
		XmlFile.Charset charset = new XmlFile.Charset();
		charset.setValue(getCharset());
		xmlFile.setCharset(charset);
		
		xmlFile.setRuntimeProperties(getRuntimeProperties());
		xmlFile.setStrict(getBoolean(PropertyNameConstants.STRICT.value()));
		xmlFile.setSafe(getBoolean(PropertyNameConstants.IS_SAFE.value()));
	
		XmlFile.RootTag rootTag = new RootTag();
		rootTag.setValue((String) properties.get(PropertyNameConstants.ROOT_TAG.value()));
		xmlFile.setRootTag(rootTag);
		
		XmlFile.RowTag rowTag = new RowTag();
		rowTag.setValue((String) properties.get(PropertyNameConstants.ROW_TAG.value()));
		xmlFile.setRowTag(rowTag);
		
	}
	
	@Override
	protected List<TypeInputOutSocket> getInOutSocket() {
		logger.debug("Generating TypeInputOutSocket data for {}", properties.get(Constants.PARAM_NAME));
		List<TypeInputOutSocket> outSockets = new ArrayList<>();
		for (Link link : component.getSourceConnections()) {
			TypeInputXmlOutSocket outSocket = new TypeInputXmlOutSocket();
			outSocket.setId(link.getSourceTerminal());
			outSocket.setType(link.getSource().getPort(link.getSourceTerminal()).getPortType());
			outSocket.setSchema(getSchema());
			outSocket.getOtherAttributes();
			outSockets.add(outSocket);
		}
		return outSockets;
	}

	@Override
	protected List<TypeBaseField> getFieldOrRecord(List<GridRow> gridList) {
		logger.debug("Generating data for {} for property {}", new Object[] { properties.get(Constants.PARAM_NAME),
				PropertyNameConstants.SCHEMA.value() });

		List<TypeBaseField> typeBaseFields = new ArrayList<>();
		if (gridList != null && gridList.size() != 0) {
			for (GridRow object : gridList) {
				XPathGridRow xPathGridRow = (XPathGridRow) object;
				TypeBaseField gridRow = converterHelper.getSchemaGridTargetData(object);
				if (StringUtils.isNotBlank(xPathGridRow.getXPath())) {
					gridRow.getOtherAttributes().put(new QName(Constants.ABSOLUTE_OR_RELATIVE_XPATH_QNAME), xPathGridRow.getXPath());
				}
				typeBaseFields.add(gridRow);
			}
		}
		return typeBaseFields;
	}
}
