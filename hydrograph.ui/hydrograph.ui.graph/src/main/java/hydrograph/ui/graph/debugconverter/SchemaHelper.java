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


package hydrograph.ui.graph.debugconverter;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.ui.parts.GraphicalEditor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;

import hydrograph.ui.common.util.Constants;
import hydrograph.ui.common.util.OSValidator;
import hydrograph.ui.datastructure.property.ComponentsOutputSchema;
import hydrograph.ui.datastructure.property.GridRow;
import hydrograph.ui.graph.controller.ComponentEditPart;
import hydrograph.ui.graph.controller.PortEditPart;
import hydrograph.ui.graph.editor.ELTGraphicalEditor;
import hydrograph.ui.graph.execution.tracking.datastructure.SubjobDetails;
import hydrograph.ui.graph.model.Component;
import hydrograph.ui.graph.model.Link;
import hydrograph.ui.graph.schema.propagation.SchemaPropagation;
import hydrograph.ui.graph.utility.ViewDataUtils;
import hydrograph.ui.logging.factory.LogFactory;
import hydrograph.ui.propertywindow.widgets.customwidgets.schema.GridRowLoader;

/**
 * This class is used for schema file operations at watchers
 *  
 * @author  Bitwise
 *
 */
public class SchemaHelper {
	private static final Logger logger = LogFactory.INSTANCE.getLogger(SchemaHelper.class);
	public static SchemaHelper INSTANCE = new SchemaHelper();

	
	private SchemaHelper() {
	}
	
	/**
	 * This function will write schema in xml file
	 * @param schemaFilePath
	 * @throws CoreException 
	 */
	public void exportSchemaFile(String schemaFilePath) throws CoreException{
		Map<String, SubjobDetails> componentNameAndLink = new HashMap();
		List<String> oldComponentIdList = new LinkedList<>();
		File file = null;
		String socketName = null;
		String componentName;
		String component_Id = null;
		ComponentsOutputSchema  componentsOutputSchema = null;
		
		IEditorPart activeEditor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		if(activeEditor instanceof ELTGraphicalEditor){
			ELTGraphicalEditor editor=(ELTGraphicalEditor) activeEditor;
			if(editor!=null){
				GraphicalViewer	graphicalViewer =(GraphicalViewer) ((GraphicalEditor)editor).getAdapter(GraphicalViewer.class);
				for (Object objectEditPart : graphicalViewer.getEditPartRegistry().values()){
					if(objectEditPart instanceof ComponentEditPart){
						List<Link> links = ((ComponentEditPart) objectEditPart).getCastedModel().getSourceConnections();
						for(Link link : links){
							 Component component = link.getSource();
							 if(StringUtils.equalsIgnoreCase(component.getComponentName(), Constants.SUBJOB_COMPONENT)){
								 ViewDataUtils.getInstance().subjobParams(componentNameAndLink, component, new StringBuilder(), link.getSourceTerminal());
								 removeDuplicateKeys(oldComponentIdList, componentNameAndLink);
								 for(Entry<String, SubjobDetails> entry : componentNameAndLink.entrySet()){
									String comp_soc = entry.getKey();
									oldComponentIdList.add(comp_soc);
									String[] split = StringUtils.split(comp_soc, "/.");
									component_Id = split[0];
									for(int i = 1;i<split.length-1;i++){
										component_Id = component_Id + "." + split[i];
									}
									socketName = entry.getValue().getSourceTerminal();
								 }
								 componentName = component_Id;
								}else{
									componentName = link.getSource().getComponentId();
									socketName = link.getSourceTerminal();
								}
							 Object obj = link.getSource().getComponentEditPart();
							 List<PortEditPart> portEditPart = ((EditPart) obj).getChildren();
							 
							 for(AbstractGraphicalEditPart part : portEditPart){
								 if(part instanceof PortEditPart){
									 boolean isWatch = ((PortEditPart)part).getPortFigure().isWatched();
									 if(isWatch && link.getSourceTerminal().equals(((PortEditPart)part).getPortFigure().getTerminal())){
										componentsOutputSchema = SchemaPropagation.INSTANCE.getComponentsOutputSchema(link);
										if(StringUtils.isNotBlank(schemaFilePath)){
											String path = schemaFilePath.trim() + "_" + componentName + "_" + socketName;
											String filePath=((IPath)new Path(path)).addFileExtension(Constants.XML_EXTENSION_FOR_IPATH).toString();
											List<GridRow> gridRowList = componentsOutputSchema.getGridRowList();
											file = new File(filePath);
											GridRowLoader gridRowLoader = new GridRowLoader(Constants.GENERIC_GRID_ROW, file);
											gridRowLoader.exportXMLfromGridRowsWithoutMessage(gridRowList);
											logger.debug("schema file created for : {}, {}", componentName, socketName);
										}
									}
								 }
							 }
						}
					}
				}
			}
		}
		
	}
	 
	private void removeDuplicateKeys(List<String> oldKeys, Map<String, SubjobDetails> componentNameAndLink){
		if(componentNameAndLink.size() > 1){
			oldKeys.forEach(field -> componentNameAndLink.remove(field));
		}
	}
	 
	/**
	 * This function will return file path with validation
	 * @param path
	 * @return validPath
	 */
	public String validatePath(String path){
		String validPath = null;
		if(OSValidator.isWindows()){
			if(StringUtils.endsWith(path, "\\")){
				return path;
			}else{
				validPath = path + "\\";
				return validPath;
			}
		}
		else if(OSValidator.isMac()){
			if(StringUtils.endsWith(path, "/")){
				return path;
			}else{
				validPath = path + "/";
				return validPath;
			}
		}
		else if(OSValidator.isUnix()){
		}
		return validPath;
	}
}
