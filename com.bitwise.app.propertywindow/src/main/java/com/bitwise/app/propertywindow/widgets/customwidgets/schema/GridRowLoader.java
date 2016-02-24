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

	String gridRowType;
	File schemaFile;
	Fields fields;

	public GridRowLoader(String gridRowType, File schemaFile){
		this.gridRowType = gridRowType;
		this.schemaFile = schemaFile;
	}

	
	
	/**
	 * The method import schema rows from schema file into schema grid.
	 * 
	 */
	public ArrayList<GridRow> importGridRowsFromXML(ListenerHelper helper){

		ArrayList<GridRow> schemaGridRowListToImport = new ArrayList<>();
		InputStream xml, xsd;
		
		ELTGridDetails eltGridDetails = (ELTGridDetails)helper.get(HelperType.SCHEMA_GRID);
		List<GridRow> grids = eltGridDetails.getGrids();
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
					ArrayList<Field> fieldsList = (ArrayList<Field>) fields.getField();
					GridRow gridRow = null;
					schemaGridRowListToImport = new ArrayList<GridRow>();

					if(Messages.GENERIC_GRIDROW.equals(gridRowType)){

						for (Field temp : fieldsList) {
							gridRow = new SchemaGrid();
							populateCommonFields(gridRow, temp);
							addRowToList(eltGridDetails, grids, gridRow, schemaGridRowListToImport);
						}	
						
					}else if(Messages.FIXEDWIDTH_GRIDROW.equals(gridRowType)){

						for (Field temp : fieldsList) {
							gridRow = new FixedWidthGridRow();
							populateCommonFields(gridRow, temp);

							if(temp.getLength()!=null)
								((FixedWidthGridRow) gridRow).setLength(String.valueOf(temp.getLength()));
							else
								((FixedWidthGridRow) gridRow).setLength("");
							addRowToList(eltGridDetails, grids, gridRow, schemaGridRowListToImport);
						}
					}else if(Messages.GENERATE_RECORD_GRIDROW.equals(gridRowType)){

						for (Field temp : fieldsList) {
							gridRow = new GenerateRecordSchemaGridRow();
							populateCommonFields(gridRow, temp);

							if(temp.getLength()!=null)
								((GenerateRecordSchemaGridRow) gridRow).setLength(String.valueOf(temp.getLength()));
							else
								((GenerateRecordSchemaGridRow) gridRow).setLength("");

							if(temp.getDefault()!=null)
								((GenerateRecordSchemaGridRow) gridRow).setDefaultValue((String.valueOf(temp.getDefault())));
							else
								((GenerateRecordSchemaGridRow) gridRow).setDefaultValue((String.valueOf("")));

							if(temp.getRangeFrom()!=null)
								((GenerateRecordSchemaGridRow) gridRow).setRangeFrom(String.valueOf(temp.getRangeFrom()));
							else
								((GenerateRecordSchemaGridRow) gridRow).setRangeFrom("");

							if(temp.getRangeFrom()!=null)
								((GenerateRecordSchemaGridRow) gridRow).setRangeTo(String.valueOf(temp.getRangeFrom()));
							else
								((GenerateRecordSchemaGridRow) gridRow).setRangeTo("");
							
							addRowToList(eltGridDetails, grids, gridRow, schemaGridRowListToImport);
							
						}
						
					}
				
				}
			}else
				throw new Exception("FileName is Empty");

		} catch (JAXBException e1) {
			MessageDialog.openError(new Shell(), "Error", "Error while importing file. XML is not in correct format -\n"+e1.getMessage());
		}
		catch (Exception e) {
			MessageDialog.openError(new Shell(), "Error", "Error while importing file. -\n"+e.getMessage());
		}
		
		return schemaGridRowListToImport;
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
			MessageDialog.openError(new Shell(), "Error", "Error while importing file. XML is not in correct format -\n"+ex.getMessage());
			return false;
		}
	}

	private void addRowToList(ELTGridDetails eltGridDetails, List<GridRow> grids, GridRow gridRow, ArrayList<GridRow> schemaGridRowListToImport) {
		if(!grids.contains(gridRow))
			grids.add(gridRow); 
		eltGridDetails.setGrids(grids);
		schemaGridRowListToImport.add(gridRow);
	}


	private void populateCommonFields(GridRow gridRow, Field temp) {
		gridRow.setFieldName(temp.getName());
		gridRow.setDataType(GridWidgetCommonBuilder.getDataTypeByValue(temp.getType().value()));
		gridRow.setDataTypeValue(GridWidgetCommonBuilder.getDataTypeValue()[GridWidgetCommonBuilder.getDataTypeByValue(temp.getType().value())]);
		gridRow.setDateFormat(temp.getFormat());
		if(temp.getPrecision()!=null)
			gridRow.setPrecision(String.valueOf(temp.getPrecision()));
		else
			gridRow.setPrecision("");

		if(temp.getScale()!=null)
			gridRow.setScale(String.valueOf(temp.getScale()));
		else
			gridRow.setScale("");

		if(temp.getScaleType()!=null){
			gridRow.setScaleType(GridWidgetCommonBuilder.getScaleTypeByValue(temp.getScaleType().value()));	
			gridRow.setScaleTypeValue(GridWidgetCommonBuilder.getScaleTypeValue()[GridWidgetCommonBuilder.getScaleTypeByValue(temp.getType().value())]);
		}else{
			gridRow.setScaleType(GridWidgetCommonBuilder.getScaleTypeByValue(Messages.SCALE_TYPE_NONE));
			gridRow.setScaleTypeValue(GridWidgetCommonBuilder.getScaleTypeValue()[Integer.valueOf(Constants.DEFAULT_INDEX_VALUE_FOR_COMBOBOX)]);
		}
		gridRow.setDescription(temp.getDescription());
	}

	/**
	 * The method exports schema rows from schema grid into schema file.
	 * 
	 */
	public void exportXMLfromGridRows(ArrayList<GridRow> schemaGridRowList){
		JAXBContext jaxbContext;
		Schema schema = new Schema();
		fields= new Fields();

		try {
			if(StringUtils.isNotBlank(schemaFile.getPath())){

				jaxbContext = JAXBContext.newInstance(Schema.class);
				Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
				jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

				for (GridRow temp : schemaGridRowList) {
					Field field = new Field();
					field.setName(temp.getFieldName());
					field.setType(FieldDataTypes.fromValue(temp.getDataTypeValue()));
					if(StringUtils.isNotBlank(temp.getDateFormat()))
						field.setFormat(temp.getDateFormat());
					if(StringUtils.isNotBlank(temp.getPrecision()))
						field.setPrecision(Integer.parseInt(temp.getPrecision()));
					if(StringUtils.isNotBlank(temp.getScale()))
						field.setScale(Integer.parseInt(temp.getScale()));
					if(!temp.getScaleTypeValue().equals("") && !temp.getScaleTypeValue().equals(Messages.SCALE_TYPE_NONE))
						field.setScaleType(ScaleTypes.fromValue(temp.getScaleTypeValue()));
					if(StringUtils.isNotBlank(temp.getDescription()))
						field.setDescription(temp.getDescription());

					if(temp instanceof FixedWidthGridRow){
						if(StringUtils.isNotBlank(((FixedWidthGridRow)temp).getLength())){
							field.setLength(new BigInteger(((FixedWidthGridRow)temp).getLength()));
						}

					}

					if(temp instanceof GenerateRecordSchemaGridRow){
						if(StringUtils.isNotBlank(((GenerateRecordSchemaGridRow)temp).getLength())){
							field.setLength(new BigInteger(((GenerateRecordSchemaGridRow)temp).getLength()));
						}
						if(StringUtils.isNotBlank(((GenerateRecordSchemaGridRow) temp).getRangeFrom()))
							field.setRangeFrom((((GenerateRecordSchemaGridRow) temp).getRangeFrom()));
						if(StringUtils.isNotBlank(((GenerateRecordSchemaGridRow) temp).getRangeTo()))
							field.setRangeTo(((GenerateRecordSchemaGridRow) temp).getRangeTo());
						if(StringUtils.isNotBlank(((GenerateRecordSchemaGridRow) temp).getDefaultValue()))
							field.setDefault(((GenerateRecordSchemaGridRow) temp).getDefaultValue());

					}

					fields.getField().add(field);
					schema.setFields(fields);
					jaxbMarshaller.marshal(schema, schemaFile);
				}
			}else
				throw new Exception("FileName is Empty");

		} catch (JAXBException e) {
			MessageDialog.openError(new Shell(), "Error", "Error while exporting file. -\n"+e.getMessage());
		}catch (Exception e) {
			MessageDialog.openError(new Shell(), "Error", "Error while exporting file. -\n"+e.getMessage());
		}

	}
}
