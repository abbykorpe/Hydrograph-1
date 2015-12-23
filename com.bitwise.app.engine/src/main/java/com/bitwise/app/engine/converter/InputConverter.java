package com.bitwise.app.engine.converter;

import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;

import com.bitwise.app.common.util.Constants;
import com.bitwise.app.common.util.LogFactory;
import com.bitwise.app.engine.constants.PropertyNameConstants;
import com.bitwise.app.engine.exceptions.SchemaException;
import com.bitwise.app.propertywindow.widgets.customwidgets.schema.GridRow;
import com.bitwise.app.propertywindow.widgets.customwidgets.schema.Schema;
import com.bitwiseglobal.graph.commontypes.TypeBaseField;
import com.bitwiseglobal.graph.commontypes.TypeBaseRecord;
import com.bitwiseglobal.graph.commontypes.TypeExternalSchema;
import com.bitwiseglobal.graph.commontypes.TypeInputComponent;
import com.bitwiseglobal.graph.commontypes.TypeInputOutSocket;

public abstract class InputConverter extends Converter {
	private static final Logger LOGGER = LogFactory.INSTANCE.getLogger(InputConverter.class);
	
	@Override
	public void prepareForXML() {
		super.prepareForXML();
		((TypeInputComponent)baseComponent).getOutSocket().addAll(getInOutSocket());
		if(getDependsOn()!=null)
		((TypeInputComponent)baseComponent).getDependsOn().add(getDependsOn());
		
	}

	/**
	 * Returs the {@link List} of classes of type {@link TypeInputOutSocket}
	 * @return {@link List}
	 * @throws SchemaException
	 */
	protected abstract List<TypeInputOutSocket> getInOutSocket();

	
	/** Converts String value to {@link TypeBaseRecord}
	 * @return {@link TypeBaseRecord}
	 * @throws SchemaException
	 */
	protected TypeBaseRecord getSchema(){
		LOGGER.debug("Genrating TypeBaseRecord data for {}", properties.get(Constants.PARAM_NAME));
		TypeBaseRecord typeBaseRecord = new TypeBaseRecord();
		Schema schema=  (Schema) properties.get(PropertyNameConstants.SCHEMA.value());
		if(schema!=null){
		if(schema.getIsExternal()){
			TypeExternalSchema typeExternalSchema=new TypeExternalSchema();
			typeExternalSchema.setUri(schema.getExternalSchemaPath());
			typeBaseRecord.setName("External");
			typeBaseRecord.getFieldOrRecordOrIncludeExternalSchema().add(typeExternalSchema);
		}else{
			typeBaseRecord.setName("Internal");
			typeBaseRecord.getFieldOrRecordOrIncludeExternalSchema().addAll(getFieldOrRecord(schema.getGridRow()));	
		}}
		return typeBaseRecord;
	}

	/**
	 * Prepare the Fields/Records for shcema
	 * @param list 
	 * @return {@link List}
	 *
	 */
	protected abstract List<TypeBaseField> getFieldOrRecord(List<GridRow> list);

	
	
}

