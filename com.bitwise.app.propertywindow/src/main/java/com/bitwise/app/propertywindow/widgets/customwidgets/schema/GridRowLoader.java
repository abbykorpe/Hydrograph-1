package com.bitwise.app.propertywindow.widgets.customwidgets.schema;

import java.io.File;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.bitwise.app.common.datastructure.property.FixedWidthGridRow;
import com.bitwise.app.common.datastructure.property.GridRow;
import com.bitwise.app.common.datastructure.property.SchemaGrid;
import com.bitwise.app.common.schema.Field;
import com.bitwise.app.common.schema.Fields;
import com.bitwise.app.propertywindow.widgets.utility.GridWidgetCommonBuilder;

public class GridRowLoader {

	ArrayList<GridRow> schemaGridRowListToImport;
	
	public GridRowLoader(){
		this.schemaGridRowListToImport = new ArrayList<>();
	}
	
	public ArrayList<GridRow> loadGridRowsFromXML(String gridRowType, File schemaFile){
		Fields fields;
		
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(Fields.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			fields = (Fields) jaxbUnmarshaller.unmarshal(schemaFile);
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
					gridRow.setScale(String.valueOf(temp.getScale()));
					//gridRow.setScaleType(GridWidgetCommonBuilder.getScaleTypeByValue(temp.getScaleType().value()));
					gridRow.setDescription(temp.getDescription());
					schemaGridRowListToImport.add(gridRow);
				}
			}
			
			
		} catch (JAXBException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return schemaGridRowListToImport;
	}
}
