/********************************************************************************
 * Copyright 2016 Capital One Services, LLC and Bitwise, Inc.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package hydrograph.ui.engine.ui.util;

import hydrograph.engine.jaxb.commontypes.TypeBaseInSocket;
import hydrograph.engine.jaxb.commontypes.TypeOperationsComponent;
import hydrograph.engine.jaxb.commontypes.TypeOperationsOutSocket;
import hydrograph.ui.common.util.Constants;
import hydrograph.ui.engine.ui.converter.LinkingData;
import hydrograph.ui.engine.ui.repository.UIComponentRepo;
import hydrograph.ui.graph.model.Component;
import hydrograph.ui.graph.model.Container;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.slf4j.Logger;
import org.xml.sax.SAXException;

/**
 * The class SubjobUiConverterUtil
 * 
 * @author Bitwise
 * 
 */
public class SubjobUiConverterUtil {
	
	public static Container createSubjobInSubjobsFolder(IPath subJobXMLPath, IPath parameterFilePath, IFile parameterFile,
			IFile subJobFile, IPath importFromPath,String subjobPath) throws InstantiationException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException, JAXBException, ParserConfigurationException,
			SAXException, IOException, CoreException, FileNotFoundException {
		UiConverterUtil converterUtil = new UiConverterUtil();
		Container subJobContainer=null;
		IFile xmlFile = ResourcesPlugin.getWorkspace().getRoot().getFile(subJobXMLPath);
		File file = new File(xmlFile.getLocation().toString());
		if (file.exists()) {
			subJobContainer= converterUtil.convertToUiXML(importFromPath.toFile(), subJobFile, parameterFile);
		} else {
			IProject iProject = ResourcesPlugin.getWorkspace().getRoot().getProject(parameterFilePath.segment(1));
			IFolder iFolder = iProject.getFolder(subjobPath.substring(0, subjobPath.lastIndexOf('/')));
			if (!iFolder.exists()) {
				iFolder.create(true, true, new NullProgressMonitor());
			}
			IFile subjobXmlFile = iProject.getFile(subjobPath);
			subjobXmlFile.create(new FileInputStream(importFromPath.toString()), true, new NullProgressMonitor());
			subJobContainer=converterUtil.convertToUiXML(importFromPath.toFile(), subJobFile, parameterFile);
		}
		return subJobContainer;
	}
	
	public static IPath getSubjobPath(String subjobPath, LinkedHashMap<String, Object> propertyMap) {
		IPath path = null;
		if(StringUtils.isNotBlank(subjobPath)){
			path=new Path(subjobPath);
			path=path.removeFileExtension();
			path=path.addFileExtension(Constants.JOB_EXTENSION_FOR_IPATH);
			propertyMap.put(Constants.PATH,path.toString());
		}
		return path;	
	}
	public static void getInPort(TypeOperationsComponent operationsComponent, Component uiComponent, LinkedHashMap<String, Object> propertyMap, UIComponentRepo currentRepository, Logger logger,String componentName) {
		logger.debug("Generating InPut Ports for -{}", componentName);
		int count=0;
		if (operationsComponent.getInSocket() != null) {
			for (TypeBaseInSocket inSocket : operationsComponent.getInSocket()) {
				uiComponent.engageInputPort(inSocket.getId());
				currentRepository.getComponentLinkList().add(
						new LinkingData(inSocket.getFromComponentId(),
								operationsComponent.getId(), inSocket
										.getFromSocketId(), inSocket.getId()));
				count++;
			}
			propertyMap.put(Constants.INPUT_PORT_COUNT_PROPERTY,count);
			uiComponent.inputPortSettings(count);
		}
	}
	
	public static void getOutPort(TypeOperationsComponent operationsComponent, Component uiComponent, LinkedHashMap<String, Object> propertyMap, UIComponentRepo currentRepository, Logger logger,String componentName) {
		logger.debug("Generating OutPut Ports for -{}", componentName);
		int count=0;
		if (operationsComponent.getOutSocket() != null) {
			for (TypeOperationsOutSocket outSocket : operationsComponent
					.getOutSocket()) {
				uiComponent.engageOutputPort(outSocket.getId());
				count++;
				}
			propertyMap.put(Constants.OUTPUT_PORT_COUNT_PROPERTY,count);
			uiComponent.outputPortSettings(count);
		}
	}
	public static Component getOutputSubJobConnectorReferance(Container subJobContainer) {
		for(Component component:subJobContainer.getChildren()){
			if(StringUtils.equals(Constants.OUTPUT_SOCKET_FOR_SUBJOB, component.getType())){
				return component;
			}
		}
		return null;
	}

	public static Component getInputSubJobConnectorReferance(Container container) {
		for(Component component:container.getChildren()){
			if(StringUtils.equals(Constants.INPUT_SOCKET_FOR_SUBJOB, component.getType())){
				return component;
			}
		}
		return null;
	}
	public static void setUiComponentProperties(Component uiComponent, Container container, UIComponentRepo currentRepository, String name_suffix, String componentName, LinkedHashMap<String, Object> propertyMap) {
		uiComponent.setType(Constants.SUBJOB_ACTION);
		uiComponent.setCategory(Constants.SUBJOB_COMPONENT_CATEGORY);
		container.getComponentNextNameSuffixes().put(name_suffix, 0);
		uiComponent.setComponentLabel(componentName);
		uiComponent.setParent(container);
		currentRepository.getComponentUiFactory().put(componentName, uiComponent);
		uiComponent.setProperties(propertyMap);
	}
}
