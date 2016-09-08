package hydrograph.ui.engine.ui.converter.impl;

import hydrograph.engine.jaxb.commontypes.TypeBaseComponent;
import hydrograph.engine.jaxb.commontypes.TypeBaseInSocket;
import hydrograph.engine.jaxb.commontypes.TypeOperationsComponent;
import hydrograph.engine.jaxb.commontypes.TypeOutputComponent;
import hydrograph.engine.jaxb.commontypes.TypeOutputInSocket;
import hydrograph.engine.jaxb.outputtypes.SubjobOutput;
import hydrograph.ui.common.util.Constants;
import hydrograph.ui.engine.ui.converter.LinkingData;
import hydrograph.ui.engine.ui.converter.UiConverter;
import hydrograph.ui.engine.ui.helper.ConverterUiHelper;
import hydrograph.ui.engine.ui.repository.UIComponentRepo;
import hydrograph.ui.graph.model.Container;
import hydrograph.ui.graph.model.components.OutputSubjobComponent;
import hydrograph.ui.graph.model.components.SubjobComponent;
import hydrograph.ui.logging.factory.LogFactory;

import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;

/**
 * Converter to convert jaxb SubjobOutput object into output subjob component
 *
 *@author BITWISE
 */
public class OutputComponentSubjobUiConverter extends UiConverter {

	
	private static final Logger logger = LogFactory.INSTANCE.getLogger(OperationSubJobUiConverter.class);
	private SubjobOutput subjobOutput;
	
	public OutputComponentSubjobUiConverter(TypeBaseComponent typeBaseComponent, Container container) {
		this.container = container;
		this.typeBaseComponent = typeBaseComponent;
		this.uiComponent = new OutputSubjobComponent();
		this.propertyMap = new LinkedHashMap<>();
		subjobOutput = (SubjobOutput) typeBaseComponent;
	}
	
	public void prepareUIXML() {
		logger.debug("Fetching Input-Delimited-Properties for {}", componentName);
		super.prepareUIXML();
		
		componentName=typeBaseComponent.getId();
		getInPort((TypeOutputComponent) typeBaseComponent);
		
		uiComponent.setType(Constants.OUTPUT_SOCKET_FOR_SUBJOB);
		uiComponent.setCategory(Constants.SUBJOB_COMPONENT_CATEGORY);
		uiComponent.setComponentLabel(Constants.SUBJOB_OUTPUT_COMPONENT_NAME);
		uiComponent.setParent(container);
		currentRepository.getComponentUiFactory().put(componentName, uiComponent);
		propertyMap.put(NAME,Constants.SUBJOB_OUTPUT_COMPONENT_NAME);
		propertyMap.put(Constants.INPUT_PORT_COUNT_PROPERTY,subjobOutput.getInSocket().size());
		uiComponent.setProperties(propertyMap);
	}

	
	private void getInPort(TypeOutputComponent typeBaseComponent) {
		logger.debug("Generating InPut Ports for -{}", componentName);
		int count=0;
		if (typeBaseComponent.getInSocket() != null) {
			for (TypeOutputInSocket inSocket : typeBaseComponent.getInSocket()) {
				uiComponent.engageInputPort(Constants.INPUT_SOCKET_TYPE+count);
				currentRepository.getComponentLinkList().add(
						new LinkingData(inSocket.getFromComponentId(),
								typeBaseComponent.getId(), inSocket.getFromSocketId(), Constants.INPUT_SOCKET_TYPE+count));
				count++;
			}
			uiComponent.inputPortSettings(count);
		}
	}
	
	@Override
	protected Map<String, String> getRuntimeProperties() {
		return null;
	}

}
