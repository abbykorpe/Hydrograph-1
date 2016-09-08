package hydrograph.ui.engine.ui.converter.impl;

import hydrograph.engine.jaxb.commontypes.TypeBaseComponent;
import hydrograph.engine.jaxb.commontypes.TypeOperationsComponent;
import hydrograph.engine.jaxb.operationstypes.Subjob;
import hydrograph.ui.common.util.Constants;
import hydrograph.ui.datastructure.property.ComponentsOutputSchema;
import hydrograph.ui.engine.exceptions.EngineException;
import hydrograph.ui.engine.ui.converter.UiConverter;
import hydrograph.ui.engine.ui.exceptions.ComponentNotFoundException;
import hydrograph.ui.engine.ui.util.SubjobUiConverterUtil;
import hydrograph.ui.engine.ui.util.UiConverterUtil;
import hydrograph.ui.graph.model.Component;
import hydrograph.ui.graph.model.Container;
import hydrograph.ui.graph.model.components.SubjobComponent;
import hydrograph.ui.logging.factory.LogFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.slf4j.Logger;
import org.xml.sax.SAXException;

/**
 * Converter to convert jaxb subjob object of operation type into subjob component
 *
 *@author BITWISE
 */
public class OperationSubJobUiConverter extends UiConverter {
	private static final Logger logger = LogFactory.INSTANCE.getLogger(OperationSubJobUiConverter.class);
	private Subjob subjob;
	
	public OperationSubJobUiConverter(TypeBaseComponent typeBaseComponent, Container container) {
		this.container = container;
		this.typeBaseComponent = typeBaseComponent;
		this.uiComponent = new SubjobComponent();
		this.propertyMap = new LinkedHashMap<>();
		subjob = (Subjob) typeBaseComponent;
	}
	
	public void prepareUIXML() {
		logger.debug("Fetching Input-Delimited-Properties for {}", componentName);
		super.prepareUIXML();
		IPath subJobPath=SubjobUiConverterUtil.getSubjobPath(subjob.getPath().getUri(),propertyMap);
		IPath subJobXMLPath=new Path(subjob.getPath().getUri());
		IPath parameterFilePath=parameterFile.getFullPath().removeLastSegments(1).append(subJobPath.removeFileExtension().lastSegment()).addFileExtension(Constants.PROPERTIES);
		IFile parameterFile = ResourcesPlugin.getWorkspace().getRoot().getFile(parameterFilePath);
		Container subJobContainer=null;
		if (!subJobXMLPath.isAbsolute()) {
			try {
				IFile subJobFile = ResourcesPlugin.getWorkspace().getRoot().getFile(subJobPath);
				IPath importFromPath = new Path(sourceXmlPath.getAbsolutePath());
				importFromPath = importFromPath.removeLastSegments(1).append(subJobXMLPath.lastSegment());
				subJobContainer=SubjobUiConverterUtil.createSubjobInSubjobsFolder(subJobXMLPath, parameterFilePath, parameterFile, subJobFile, importFromPath,subjob.getPath().getUri());
			} catch (ComponentNotFoundException | InstantiationException | IllegalAccessException
					| IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException
					| EngineException | JAXBException | ParserConfigurationException | SAXException | IOException
					| CoreException exception) {
				logger.error("Error while importing subjob",exception);
			}
		}
		else {
			File jobFile = new File(subJobPath.toString());
			File subJobFile = new File(subjob.getPath().getUri());
			UiConverterUtil converterUtil = new UiConverterUtil();
			try {
				subJobContainer=converterUtil.convertSubjobToUiXML(subJobFile, jobFile, parameterFile);
			} catch (ComponentNotFoundException | InstantiationException | IllegalAccessException
					| IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException
					| EngineException | JAXBException | ParserConfigurationException | SAXException | IOException exception) {
				logger.error("Error while importing subjob",exception);
			}

		}
		
		
		SubjobUiConverterUtil.getInPort((TypeOperationsComponent) typeBaseComponent,uiComponent,propertyMap,currentRepository,logger,componentName);
		SubjobUiConverterUtil.getOutPort((TypeOperationsComponent) typeBaseComponent,uiComponent,propertyMap,currentRepository,logger,componentName);
		
		SubjobUiConverterUtil.setUiComponentProperties(uiComponent,container,currentRepository,name_suffix,componentName,propertyMap);
		
		Component inputSubjobComponent=SubjobUiConverterUtil.getInputSubJobConnectorReferance(subJobContainer);
		inputSubjobComponent.getProperties().put(Constants.SUBJOB_COMPONENT,uiComponent);
		inputSubjobComponent.getProperties().put(Constants.SCHEMA_TO_PROPAGATE,new LinkedHashMap<String, ComponentsOutputSchema>());
		
		propertyMap.put(Constants.INPUT_SUBJOB, inputSubjobComponent);
		Component outputSubjobComponent = SubjobUiConverterUtil.getOutputSubJobConnectorReferance(subJobContainer);
		propertyMap.put(Constants.OUTPUT_SUBJOB, outputSubjobComponent);
		outputSubjobComponent.getProperties().put(Constants.SUBJOB_COMPONENT,uiComponent);
		if (outputSubjobComponent.getProperties().get(Constants.SCHEMA_TO_PROPAGATE) != null) {
			propertyMap.put(Constants.SCHEMA_TO_PROPAGATE,
					outputSubjobComponent.getProperties().get(Constants.SCHEMA_TO_PROPAGATE));
		} else {
			outputSubjobComponent.getProperties().put(Constants.SCHEMA_TO_PROPAGATE,
					new LinkedHashMap<String, ComponentsOutputSchema>());
		}
			
	}

	@Override
	protected Map<String, String> getRuntimeProperties() {
		return null;
	}
	
}
