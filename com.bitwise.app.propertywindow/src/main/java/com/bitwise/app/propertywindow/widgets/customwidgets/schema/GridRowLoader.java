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

 
package com.bitwise.app.propertywindow.widgets.customwidgets.schema;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.slf4j.Logger;

import com.bitwise.app.common.datastructure.property.FixedWidthGridRow;
import com.bitwise.app.common.datastructure.property.GenerateRecordSchemaGridRow;
import com.bitwise.app.common.datastructure.property.GridRow;
import com.bitwise.app.common.datastructure.property.SchemaGrid;
import com.bitwise.app.common.schema.Field;
import com.bitwise.app.common.schema.FieldDataTypes;
import com.bitwise.app.common.schema.Fields;
import com.bitwise.app.common.schema.ScaleTypes;
import com.bitwise.app.common.schema.Schema;
import com.bitwise.app.common.util.Constants;
import com.bitwise.app.logging.factory.LogFactory;
import com.bitwise.app.propertywindow.messages.Messages;
import com.bitwise.app.propertywindow.widgets.listeners.ListenerHelper;
import com.bitwise.app.propertywindow.widgets.listeners.ListenerHelper.HelperType;
import com.bitwise.app.propertywindow.widgets.listeners.grid.ELTGridDetails;
import com.bitwise.app.propertywindow.widgets.utility.GridWidgetCommonBuilder;

/**
 * The Class GridRowLoader.
 * 
 * @author Bitwise
 */

public class GridRowLoader {

	public final static String SCHEMA_CONFIG_XSD_PATH = Platform.getInstallLocation().getURL().getPath() + Messages.SCHEMA_CONFIG_XSD_PATH;
	private static Logger logger = LogFactory.INSTANCE.getLogger(GridRowLoader.class);
	
	private String gridRowType;
	private File schemaFile;
	private Fields fields;

	public GridRowLoader(String gridRowType, File schemaFile){
		this.gridRowType = gridRowType;
		this.schemaFile = schemaFile;
	}

	
	
	/**
	 * The method import schema rows from schema file into schema grid.
	 * 
	 */
	public List<GridRow> importGridRowsFromXML(ListenerHelper helper){

		List<GridRow> schemaGridRowListToImport = null;
		InputStream xml, xsd;
		
		ELTGridDetails gridDetails = (ELTGridDetails)helper.get(HelperType.SCHEMA_GRID);
		List<GridRow> grids = gridDetails.getGrids();
		grids.clear();
		try {
			if(StringUtils.isNotBlank(schemaFile.getPath())){
				
				xml = new FileInputStream(schemaFile);
				
				xsd = new FileInputStream(SCHEMA_CONFIG_XSD_PATH);
				
				if(validateXML(xml, xsd)){
					
					JAXBContext jaxbContext = JAXBContext.newInstance(Schema.class);
					Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
					
					Schema schema= (Schema) jaxbUnmarshaller.unmarshal(schemaFile);
					fields = schema.getFields();
					List<Field> fieldsList = fields.getField();
					GridRow gridRow = null;
					schemaGridRowListToImport = new ArrayList<GridRow>();

					if(Constants.GENERIC_GRID_ROW.equals(gridRowType)){

						for (Field field : fieldsList) {
							addRowToList(gridDetails, grids, getBasicSchemaGridRow(field), schemaGridRowListToImport);
						}	
						
					}else if(Constants.FIXEDWIDTH_GRID_ROW.equals(gridRowType)){

						for (Field field : fieldsList) {
							addRowToList(gridDetails, grids, getFixedWidthGridRow(field), schemaGridRowListToImport);
						}
					}else if(Constants.GENERATE_RECORD_GRID_ROW.equals(gridRowType)){

						for (Field field : fieldsList) {
							
							addRowToList(gridDetails, grids, getGenerateRecordGridRow(field), schemaGridRowListToImport);
							
						}
						
					}
				
				}
			}else{
				
				logger.error(Messages.EXPORT_XML_EMPTY_FILENAME);
				throw new Exception(Messages.EXPORT_XML_EMPTY_FILENAME);
			}

		} catch (JAXBException e1) {
			grids.clear();
			MessageDialog.openError(new Shell(), "Error", Messages.IMPORT_XML_FORMAT_ERROR + " -\n"+e1.getMessage());
			logger.error(Messages.IMPORT_XML_FORMAT_ERROR);
			return null;
		}catch (DuplicateFieldException e1) {
			grids.clear();
			MessageDialog.openError(new Shell(), "Error", e1.getMessage());
			logger.error(e1.getMessage());
			return null;
		}
		catch (Exception e) {
			grids.clear();
			MessageDialog.openError(new Shell(), "Error", Messages.IMPORT_XML_ERROR+" -\n"+e.getMessage());
			logger.error(Messages.IMPORT_XML_ERROR);
			return null;
		}
		
		return schemaGridRowListToImport;
	}


	/**
	 * For importing engine-XML, this method import schema rows from schema file into schema grid.
	 * 
	 */
	public List<GridRow> importGridRowsFromXML(){

		List<GridRow> schemaGridRowListToImport = new ArrayList<GridRow>();
		InputStream xml, xsd;
		try {
			if(StringUtils.isNotBlank(schemaFile.getPath())){
				
				xml = new FileInputStream(schemaFile);
				
				xsd = new FileInputStream(SCHEMA_CONFIG_XSD_PATH);
				
				if(validateXML(xml, xsd)){
					
					JAXBContext jaxbContext = JAXBContext.newInstance(Schema.class);
					Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
					
					Schema schema= (Schema) jaxbUnmarshaller.unmarshal(schemaFile);
					fields = schema.getFields();
					List<Field> fieldsList = fields.getField();
					GridRow gridRow = null;
					schemaGridRowListToImport = new ArrayList<GridRow>();

					if(Constants.GENERIC_GRID_ROW.equals(gridRowType)){

						for (Field field : fieldsList) {
							gridRow = getBasicSchemaGridRow(field);
							schemaGridRowListToImport.add(gridRow);
						}	
						
					}else if(Constants.FIXEDWIDTH_GRID_ROW.equals(gridRowType)){

						for (Field field : fieldsList) {
							schemaGridRowListToImport.add(getFixedWidthGridRow(field));
						}
					}else if(Constants.GENERATE_RECORD_GRID_ROW.equals(gridRowType)){
						
						for (Field field : fieldsList) {
							schemaGridRowListToImport.add(gridRow = getGenerateRecordGridRow(field));
						}
						
					}
				
				}
			}else{
				
				logger.warn(Messages.EXPORT_XML_EMPTY_FILENAME);
			}

		} catch (JAXBException e1) {
			logger.warn(Messages.IMPORT_XML_FORMAT_ERROR);
			return schemaGridRowListToImport;
		}
		catch (Exception e) {
			logger.warn(Messages.IMPORT_XML_ERROR);
			return schemaGridRowListToImport;
		}
		
		return schemaGridRowListToImport;
	}



	/**
	 * The method exports schema rows from schema grid into schema file.
	 * 
	 */
	public void exportXMLfromGridRows(List<GridRow> schemaGridRowList){
		JAXBContext jaxbContext;
		Schema schema = new Schema();
		fields= new Fields();

		try {
			if(StringUtils.isNotBlank(schemaFile.getPath())){

				jaxbContext = JAXBContext.newInstance(Schema.class);
				Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
				jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

				for (GridRow gridRow : schemaGridRowList) {
					Field field = new Field();
					field.setName(gridRow.getFieldName());
					field.setType(FieldDataTypes.fromValue(gridRow.getDataTypeValue()));
					if(StringUtils.isNotBlank(gridRow.getDateFormat()))
						field.setFormat(gridRow.getDateFormat());
					if(StringUtils.isNotBlank(gridRow.getPrecision()))
						field.setPrecision(Integer.parseInt(gridRow.getPrecision()));
					if(StringUtils.isNotBlank(gridRow.getScale()))
						field.setScale(Integer.parseInt(gridRow.getScale()));
					if(gridRow.getScaleTypeValue()!=null){
						if(!gridRow.getScaleTypeValue().equals("") && !gridRow.getScaleTypeValue().equals(Messages.SCALE_TYPE_NONE)){
							field.setScaleType(ScaleTypes.fromValue(gridRow.getScaleTypeValue()));
						}
					}
					if(StringUtils.isNotBlank(gridRow.getDescription()))
						field.setDescription(gridRow.getDescription());

					if(gridRow instanceof FixedWidthGridRow){
						if(StringUtils.isNotBlank(((FixedWidthGridRow)gridRow).getLength())){
							field.setLength(new BigInteger(((FixedWidthGridRow)gridRow).getLength()));
						}

					}

					if(gridRow instanceof GenerateRecordSchemaGridRow){
						if(StringUtils.isNotBlank(((GenerateRecordSchemaGridRow)gridRow).getLength())){
							field.setLength(new BigInteger(((GenerateRecordSchemaGridRow)gridRow).getLength()));
						}
						if(StringUtils.isNotBlank(((GenerateRecordSchemaGridRow) gridRow).getRangeFrom()))
							field.setRangeFrom((((GenerateRecordSchemaGridRow) gridRow).getRangeFrom()));
						if(StringUtils.isNotBlank(((GenerateRecordSchemaGridRow) gridRow).getRangeTo()))
							field.setRangeTo(((GenerateRecordSchemaGridRow) gridRow).getRangeTo());
						if(StringUtils.isNotBlank(((GenerateRecordSchemaGridRow) gridRow).getDefaultValue()))
							field.setDefault(((GenerateRecordSchemaGridRow) gridRow).getDefaultValue());

					}

					fields.getField().add(field);
					
					
				}
				schema.setFields(fields);
				jaxbMarshaller.marshal(schema, schemaFile);
				MessageDialog.openInformation(new Shell(), "Information", Messages.EXPORTED_SCHEMA);
				
			}else{
				
				logger.error(Messages.EXPORT_XML_EMPTY_FILENAME);
				throw new Exception(Messages.EXPORT_XML_EMPTY_FILENAME);
			}

		} catch (JAXBException e) {
			MessageDialog.openError(new Shell(), "Error", Messages.EXPORT_XML_ERROR+" -\n"+e.getMessage());
			logger.error(Messages.EXPORT_XML_ERROR);
		}catch (Exception e) {
			MessageDialog.openError(new Shell(), "Error", Messages.EXPORT_XML_ERROR+" -\n"+e.getMessage());
			logger.error(Messages.EXPORT_XML_ERROR);
		}

	}
	

	private GridRow getBasicSchemaGridRow(Field field) {
		GridRow gridRow = new SchemaGrid();
		populateCommonFields(gridRow, field);
		return gridRow;
	}
	
	
	private class DuplicateFieldException extends Exception{
		public DuplicateFieldException(String message)
		{
			super(message);
		}
	}
	

	private GridRow getGenerateRecordGridRow(Field field) {
		GridRow gridRow = new GenerateRecordSchemaGridRow();
		populateCommonFields(gridRow, field);

		if(field.getLength()!=null)
			((GenerateRecordSchemaGridRow) gridRow).setLength(String.valueOf(field.getLength()));
		else
			((GenerateRecordSchemaGridRow) gridRow).setLength("");

		if(field.getDefault()!=null)
			((GenerateRecordSchemaGridRow) gridRow).setDefaultValue((String.valueOf(field.getDefault())));
		else
			((GenerateRecordSchemaGridRow) gridRow).setDefaultValue((String.valueOf("")));

		if(field.getRangeFrom()!=null)
			((GenerateRecordSchemaGridRow) gridRow).setRangeFrom(String.valueOf(field.getRangeFrom()));
		else
			((GenerateRecordSchemaGridRow) gridRow).setRangeFrom("");

		if(field.getRangeFrom()!=null)
			((GenerateRecordSchemaGridRow) gridRow).setRangeTo(String.valueOf(field.getRangeTo()));
		else
			((GenerateRecordSchemaGridRow) gridRow).setRangeTo("");
		return gridRow;
	}



	private GridRow getFixedWidthGridRow(Field field) {
		GridRow gridRow = new FixedWidthGridRow();
		populateCommonFields(gridRow, field);
		
		if(field.getLength()!=null)
			((FixedWidthGridRow) gridRow).setLength(String.valueOf(field.getLength()));
		else
			((FixedWidthGridRow) gridRow).setLength("");
		return gridRow;
	}

	private boolean validateXML(InputStream xml, InputStream xsd){
		try
		{
			SchemaFactory factory = 
					SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			javax.xml.validation.Schema schema = factory.newSchema(new StreamSource(xsd));
			Validator validator = schema.newValidator();
			
			validator.validate(new StreamSource(xml));
			return true;
		}
		catch(Exception ex)
		{
			MessageDialog.openError(new Shell(), "Error", Messages.IMPORT_XML_FORMAT_ERROR + "-\n" + ex.getMessage());
			logger.error(Messages.IMPORT_XML_FORMAT_ERROR);
			return false;
		}
	}

	private void addRowToList(ELTGridDetails gridDetails, List<GridRow> grids, GridRow gridRow, List<GridRow> schemaGridRowListToImport) throws Exception {
		if(!grids.contains(gridRow)){
			grids.add(gridRow); 
			gridDetails.setGrids(grids);
			schemaGridRowListToImport.add(gridRow);
		}
		else{
			logger.error(Messages.IMPORT_XML_DUPLICATE_FIELD_ERROR);
			throw new DuplicateFieldException(Messages.IMPORT_XML_DUPLICATE_FIELD_ERROR);
		}
		
	}


	private void populateCommonFields(GridRow gridRow, Field field) {
		gridRow.setFieldName(field.getName());
		gridRow.setDataType(GridWidgetCommonBuilder.getDataTypeByValue(field.getType().value()));
		gridRow.setDataTypeValue(GridWidgetCommonBuilder.getDataTypeValue()[GridWidgetCommonBuilder.getDataTypeByValue(field.getType().value())]);
		gridRow.setDateFormat(field.getFormat());
		if(field.getPrecision()!=null)
			gridRow.setPrecision(String.valueOf(field.getPrecision()));
		else
			gridRow.setPrecision("");

		if(field.getScale()!=null)
			gridRow.setScale(String.valueOf(field.getScale()));
		else
			gridRow.setScale("");

		if(field.getScaleType()!=null){
			gridRow.setScaleType(GridWidgetCommonBuilder.getScaleTypeByValue(field.getScaleType().value()));	
			gridRow.setScaleTypeValue(GridWidgetCommonBuilder.getScaleTypeValue()[GridWidgetCommonBuilder.getScaleTypeByValue(field.getScaleType().value())]);
		}else{
			gridRow.setScaleType(GridWidgetCommonBuilder.getScaleTypeByValue(Messages.SCALE_TYPE_NONE));
			gridRow.setScaleTypeValue(GridWidgetCommonBuilder.getScaleTypeValue()[Integer.valueOf(Constants.DEFAULT_INDEX_VALUE_FOR_COMBOBOX)]);
		}
		gridRow.setDescription(field.getDescription());
	}

}
