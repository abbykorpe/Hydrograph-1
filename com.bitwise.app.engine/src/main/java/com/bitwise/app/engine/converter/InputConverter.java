package com.bitwise.app.engine.converter;

import java.util.List;

import org.slf4j.Logger;

import com.bitwise.app.common.util.LogFactory;
import com.bitwise.app.engine.exceptions.SchemaException;
import com.bitwiseglobal.graph.commontypes.TypeBaseField;
import com.bitwiseglobal.graph.commontypes.TypeBaseRecord;
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
		LOGGER.debug("Genrating TypeBaseRecord data for {}", properties.get(NAME));
		TypeBaseRecord typeBaseRecord = new TypeBaseRecord();
		typeBaseRecord.setName("");
		typeBaseRecord.getFieldOrRecordOrIncludeExternalSchema().addAll(getFieldOrRecord());		
		return typeBaseRecord;
	}

	/**
	 * Prepare the Fields/Records for shcema
	 * @return {@link List}
	 *
	 */
	protected abstract List<TypeBaseField> getFieldOrRecord();
}
