package com.bitwise.app.propertywindow.widgets.customwidgets.schema;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.lang.StringUtils;

import com.bitwise.app.common.datastructure.property.FixedWidthGridRow;
import com.bitwise.app.common.datastructure.property.GridRow;
import com.bitwise.app.common.datastructure.property.SchemaGrid;
import com.bitwise.app.common.schema.Field;
import com.bitwise.app.common.schema.Fields;
import com.bitwise.app.common.schema.Schema;
import com.bitwise.app.propertywindow.widgets.listeners.ListenerHelper;
import com.bitwise.app.propertywindow.widgets.listeners.ListenerHelper.HelperType;
import com.bitwise.app.propertywindow.widgets.listeners.grid.ELTGridDetails;
import com.bitwise.app.propertywindow.widgets.utility.GridWidgetCommonBuilder;

public class GridRowLoader {

	ArrayList<GridRow> schemaGridRowListToImport;
	Fields fields;
	
	public GridRowLoader(){
		this.schemaGridRowListToImport = new ArrayList<>();
	}
	
	public ArrayList<GridRow> loadGridRowsFromXML(ListenerHelper helper, String gridRowType, File schemaFile){
		
		
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(Schema.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			Schema schema= (Schema) jaxbUnmarshaller.unmarshal(schemaFile);
			fields = schema.getFields();
			ArrayList<Field> fieldsList = (ArrayList<Field>) fields.getField();
			
			if(gridRowType.equals("Generic")){
				schemaGridRowListToImport = new ArrayList<GridRow>();
				
				for (Field temp : fieldsList) {
					SchemaGrid gridRow = new SchemaGrid();
					gridRow.setFieldName(temp.getName());
					gridRow.setDataType(GridWidgetCommonBuilder.getDataTypeByValue(temp.getType().value()));
					
					gridRow.setDateFormat(temp.getFormat());
					gridRow.setPrecision(String.valueOf(temp.getPrecision()));
					gridRow.setScale(String.valueOf(temp.getScale()));
					if(temp.getScaleType()!=null)
						gridRow.setScaleType(GridWidgetCommonBuilder.getScaleTypeByValue(temp.getScaleType().value()));	
					else
						gridRow.setScaleType(GridWidgetCommonBuilder.getScaleTypeByValue("none"));
					gridRow.setDescription(temp.getDescription());
					
					ELTGridDetails eltGridDetails = (ELTGridDetails)helper.get(HelperType.SCHEMA_GRID);
					List grids = eltGridDetails.getGrids();
					grids.add(gridRow); 
					eltGridDetails.setGrids(grids);
					schemaGridRowListToImport.add(gridRow);
				}
				
			}else if(gridRowType.equals("FixedWidth")){
				schemaGridRowListToImport = new ArrayList<GridRow>();
				
				for (Field temp : fieldsList) {
					FixedWidthGridRow gridRow = new FixedWidthGridRow();
					gridRow.setFieldName(temp.getName());
					gridRow.setDataType(GridWidgetCommonBuilder.getDataTypeByValue(temp.getType().value()));
					gridRow.setDateFormat(temp.getFormat());
					gridRow.setPrecision(String.valueOf(temp.getPrecision()));
					//if(StringUtils.isNotBlank(temp.getScale()))
						gridRow.setScale(String.valueOf(temp.getScale()));
					if(temp.getScaleType()!=null)
						gridRow.setScaleType(GridWidgetCommonBuilder.getScaleTypeByValue(temp.getScaleType().value()));
					else
						gridRow.setScaleType(GridWidgetCommonBuilder.getScaleTypeByValue("none"));
					gridRow.setDescription(temp.getDescription());
					gridRow.setLength(String.valueOf(temp.getLength()));
					schemaGridRowListToImport.add(gridRow);
				}
			}
			
			
		} catch (JAXBException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return schemaGridRowListToImport;
	}
	
	public void exportXMLfromGridRows(ArrayList<GridRow> schemaGridRowList, String gridRowType, File schemaFile){
		JAXBContext jaxbContext;
		Schema schema = new Schema();
		fields= new Fields();
		try {
			jaxbContext = JAXBContext.newInstance(Schema.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			
			for (GridRow temp : schemaGridRowList) {
				Field field = new Field();
				field.setName(temp.getFieldName());
				//field.setType(temp.)
				field.setFormat(temp.getDateFormat());
				if(temp.getPrecision()!= null)
					field.setPrecision(Integer.parseInt(temp.getPrecision()));
				if(StringUtils.isNotBlank(temp.getScale()))
					field.setScale(Integer.parseInt(temp.getScale()));
				field.setDescription(temp.getDescription());
				
				fields.getField().add(field);
				
			}
			
			schema.setFields(fields);
			
			
			jaxbMarshaller.marshal(schema, schemaFile);
			
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
