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

import hydrograph.ui.common.util.Constants;
import hydrograph.ui.common.util.OSValidator;
import hydrograph.ui.datastructure.property.ComponentsOutputSchema;
import hydrograph.ui.datastructure.property.GridRow;
import hydrograph.ui.graph.controller.ComponentEditPart;
import hydrograph.ui.graph.controller.PortEditPart;
import hydrograph.ui.graph.editor.ELTGraphicalEditor;
import hydrograph.ui.graph.model.Link;
import hydrograph.ui.graph.schema.propagation.SchemaPropagation;
import hydrograph.ui.logging.factory.LogFactory;
import hydrograph.ui.propertywindow.widgets.customwidgets.schema.GridRowLoader;

import java.io.File;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.ui.parts.GraphicalEditor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;

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
	 */
	public void exportSchemaFile(String schemaFilePath){
		File file = null;
		String socketName = null;
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
							 String componentName = link.getSource().getComponentLabel().getLabelContents();
							 Object obj = link.getSource().getComponentEditPart();
							 List<PortEditPart> portEditPart = ((EditPart) obj).getChildren();
							 
							 for(AbstractGraphicalEditPart part : portEditPart){
								 if(part instanceof PortEditPart){
									 boolean isWatch = ((PortEditPart)part).getPortFigure().isWatched();
									 if(isWatch && link.getSourceTerminal().equals(((PortEditPart)part).getPortFigure().getTerminal())){
										socketName = ((PortEditPart)part).getPortFigure().getTerminal();
										componentsOutputSchema = SchemaPropagation.INSTANCE.getComponentsOutputSchema(link);
										if(StringUtils.isNotBlank(schemaFilePath)){
											String path = schemaFilePath.trim() + "_" + componentName + "_" + socketName;
											String filePath=((IPath)new Path(path)).removeFileExtension().addFileExtension(Constants.XML_EXTENSION_FOR_IPATH).toString();
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
