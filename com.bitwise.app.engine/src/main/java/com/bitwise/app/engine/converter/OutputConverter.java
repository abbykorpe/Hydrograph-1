package com.bitwise.app.engine.converter;

import java.util.List;

import org.slf4j.Logger;

import com.bitwise.app.common.datastructure.property.ComponentsOutputSchema;
import com.bitwise.app.common.datastructure.property.Schema;
import com.bitwise.app.common.util.Constants;
import com.bitwise.app.engine.constants.PropertyNameConstants;
import com.bitwise.app.engine.converter.impl.OutputFileDelimitedConverter;
import com.bitwise.app.engine.exceptions.SchemaException;
import com.bitwise.app.logging.factory.LogFactory;
import com.bitwiseglobal.graph.commontypes.TypeBaseField;
import com.bitwiseglobal.graph.commontypes.TypeBaseRecord;
import com.bitwiseglobal.graph.commontypes.TypeExternalSchema;
import com.bitwiseglobal.graph.commontypes.TypeOutputComponent;
import com.bitwiseglobal.graph.commontypes.TypeOutputInSocket;

public abstract class OutputConverter extends Converter {

	private static final Logger logger = LogFactory.INSTANCE.getLogger(OutputFileDelimitedConverter.class);

	@Override
	public void prepareForXML() {
		super.prepareForXML();
		((TypeOutputComponent) baseComponent).getInSocket().addAll(getOutInSocket());
	}

	/**
	 * Returs the {@link List} of classes of type {@link TypeOutputInSocket}
	 * 
	 * @return {@link TypeOutputInSocket}
	 * @throws SchemaException
	 */
	protected abstract List<TypeOutputInSocket> getOutInSocket();

	/**
	 * Converts String value to {@link TypeBaseRecord}
	 * 
	 * @return {@link TypeBaseRecord}
	 * @throws SchemaException
	 */
	protected TypeBaseRecord getSchema() {
		logger.debug("Genrating TypeBaseRecord data for {}", properties.get(Constants.PARAM_NAME));
		TypeBaseRecord typeBaseRecord = new TypeBaseRecord();
		Schema schema = (Schema) properties.get(PropertyNameConstants.SCHEMA.value());
		if (schema != null) {
			if (schema.getIsExternal()) {
				TypeExternalSchema typeExternalSchema = new TypeExternalSchema();
				typeExternalSchema.setUri(schema.getExternalSchemaPath());
				typeBaseRecord.setName("External");
				typeBaseRecord.getFieldOrRecordOrIncludeExternalSchema().add(typeExternalSchema);
			} else
				typeBaseRecord.setName("Internal");
		}else
			typeBaseRecord.setName("Internal");
		
		if (fetchPropagatedSchema() != null)
			typeBaseRecord.getFieldOrRecordOrIncludeExternalSchema().addAll(getFieldOrRecord(fetchPropagatedSchema()));

		return typeBaseRecord;
	}

	protected ComponentsOutputSchema fetchPropagatedSchema() {
		ComponentsOutputSchema componentsOutputSchema = null;
		if (properties.get(Constants.SCHEMA_TO_PROPAGATE) != null)
			componentsOutputSchema = (ComponentsOutputSchema) properties.get(Constants.SCHEMA_TO_PROPAGATE);
		return componentsOutputSchema;
	}

	/**
	 * Prepare the Fields/Records for shcema
	 * 
	 * @param componentsOutputSchema
	 * @return {@link List}
	 * 
	 */
	protected abstract List<TypeBaseField> getFieldOrRecord(ComponentsOutputSchema componentsOutputSchema);
}
