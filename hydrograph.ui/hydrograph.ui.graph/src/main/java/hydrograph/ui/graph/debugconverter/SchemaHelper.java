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

import hydrograph.ui.common.schema.Field;
import hydrograph.ui.common.schema.Fields;
import hydrograph.ui.common.util.Constants;
import hydrograph.ui.datastructure.property.ComponentsOutputSchema;
import hydrograph.ui.datastructure.property.GridRow;
import hydrograph.ui.datastructure.property.Schema;
import hydrograph.ui.graph.controller.LinkEditPart;
import hydrograph.ui.graph.model.Link;
import hydrograph.ui.logging.factory.LogFactory;
import hydrograph.ui.propertywindow.widgets.customwidgets.schema.GridRowLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.slf4j.Logger;

/**
 * This class is used for schema file operations
 *  
 * @author  Bitwise
 *
 */
public class SchemaHelper {
	private static final Logger logger = LogFactory.INSTANCE.getLogger(SchemaHelper.class);
	public static SchemaHelper INSTANCE = new SchemaHelper();

	
	
	
	/**
	 * This function will write schema in xml file
	 * @param selectedObjects
	 * @param schemaFilePath
	 */
	public void exportSchemaGridData(List<Object> selectedObjects , String schemaFilePath){
		if(schemaFilePath!=null){
					
		schemaFilePath=((IPath)new Path(schemaFilePath)).removeFileExtension().addFileExtension(Constants.XML_EXTENSION_FOR_IPATH).toString();
		for(Object obj:selectedObjects){
			if(obj instanceof LinkEditPart)	{
				Link link = (Link)((LinkEditPart)obj).getModel();
				Schema viewData = null;
				ComponentsOutputSchema  componentsOutputSchema = null;
				Object hashMap1 = null;
				File file = new File(schemaFilePath);
				Map<String, Object> hashMap = link.getSource().getProperties();
				for(Entry<String, Object> entry : hashMap.entrySet()){
					if(entry.getKey().equalsIgnoreCase(Constants.SCHEMA_PROPERTY_NAME)){
						viewData = (Schema) entry.getValue();
					}else if(entry.getKey().equalsIgnoreCase(Constants.SCHEMA_TO_PROPAGATE)){
						hashMap1 = entry.getValue();
					}
				}
				
				for(Entry<String, Object> entry : ((Map<String, Object>) hashMap1).entrySet()){
					if(entry.getKey().equalsIgnoreCase(Constants.FIXED_OUTSOCKET_ID)){
						componentsOutputSchema = (ComponentsOutputSchema) entry.getValue();
					}
				}
				
				List<GridRow> gridRow1 = componentsOutputSchema.getGridRowList();
				GridRowLoader gridRowLoader = new GridRowLoader(Constants.GENERIC_GRID_ROW, file);
				gridRowLoader.exportXMLfromGridRowsWithoutMessage(gridRow1);
			 
			}
		}
			}
	}
	
	/**
	 * This function will read schema file and return schema fields
	 * @param schemaFilePath
	 * @return
	 */
	public Fields importSchemaXml(String schemaFilePath){
		List<GridRow> schemaGridRowListToImport = new ArrayList<>();
		File file = new File(schemaFilePath);
		try {
			InputStream stream = new FileInputStream(file);
			JAXBContext jaxbContext = JAXBContext.newInstance(hydrograph.ui.common.schema.Schema.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			hydrograph.ui.common.schema.Schema schema = (hydrograph.ui.common.schema.Schema) jaxbUnmarshaller.unmarshal(file);
			Fields fields = schema.getFields();
			for(Field fd : fields.getField()){
				logger.debug("Type:{}, Name:{}, Format:{}"+fd.getType(),fd.getName(),fd.getFormat());
			}
			return fields;
		} catch (FileNotFoundException fileNotFoundException) {
			logger.error("File not found", fileNotFoundException);
		} catch (JAXBException jaxbException) {
			logger.error("Invalid xml file", jaxbException);
		}
		return null;
	}
}
