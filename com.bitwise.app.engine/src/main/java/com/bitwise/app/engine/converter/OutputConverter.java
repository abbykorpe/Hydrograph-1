package com.bitwise.app.engine.converter;

import java.util.List;

import org.slf4j.Logger;

import com.bitwise.app.common.util.Constants;
import com.bitwise.app.common.util.LogFactory;
import com.bitwise.app.engine.converter.impl.OutputFileDelimitedConverter;
import com.bitwise.app.engine.exceptions.SchemaException;
import com.bitwiseglobal.graph.commontypes.TypeBaseField;
import com.bitwiseglobal.graph.commontypes.TypeBaseRecord;
import com.bitwiseglobal.graph.commontypes.TypeOutputComponent;
import com.bitwiseglobal.graph.commontypes.TypeOutputInSocket;

public abstract class OutputConverter extends Converter {
	
	private static final Logger logger = LogFactory.INSTANCE.getLogger(OutputFileDelimitedConverter.class);
	@Override
	public void prepareForXML(){
		super.prepareForXML();
		((TypeOutputComponent)baseComponent).getInSocket().addAll(getOutInSocket());
	}
	
	/**
	 * Returs the {@link List} of classes of type {@link TypeOutputInSocket}
	 * @return {@link TypeOutputInSocket}
	 * @throws SchemaException
	 */
	protected abstract List<TypeOutputInSocket> getOutInSocket() ;

	/** Converts String value to {@link TypeBaseRecord}
	 * @return {@link TypeBaseRecord}
	 * @throws SchemaException
	 */
	protected TypeBaseRecord getSchema(){
		logger.debug("Genrating TypeBaseRecord data for {}", properties.get(Constants.PARAM_NAME));
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
