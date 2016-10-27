/*******************************************************************************
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
 *******************************************************************************/
package hydrograph.engine.cascading.assembly.generator;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cascading.tap.Tap;
import hydrograph.engine.assembly.entity.InputFileMixedSchemeEntity;
import hydrograph.engine.assembly.entity.utils.InputEntityUtils;
import hydrograph.engine.cascading.assembly.InputFileMixedSchemeAssembly;
import hydrograph.engine.cascading.assembly.base.BaseComponent;
import hydrograph.engine.cascading.assembly.generator.base.InputAssemblyGeneratorBase;
import hydrograph.engine.cascading.assembly.infra.ComponentParameters;
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent;
import hydrograph.engine.jaxb.commontypes.TypeProperties.Property;
import hydrograph.engine.jaxb.inputtypes.TextFileMixedScheme;

public class InputFileMixedSchemeAssemblyGenerator extends
		InputAssemblyGeneratorBase {

	private InputFileMixedSchemeEntity inputFileMixedSchemeEntity;
	private TextFileMixedScheme jaxbTextFileMixedScheme;
	private InputFileMixedSchemeAssembly inputMixedFileAssembly;
	private static Logger LOG = LoggerFactory
			.getLogger(InputFileMixedSchemeAssemblyGenerator.class);

	public InputFileMixedSchemeAssemblyGenerator(TypeBaseComponent baseComponent) {
		super(baseComponent);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map<String, Tap> getSourceTap() {
		return null;
	}

	@Override
	public void castComponentFromBase(TypeBaseComponent baseComponent) {
		jaxbTextFileMixedScheme = (TextFileMixedScheme) baseComponent;
	}

	@Override
	public void createEntity() {
		inputFileMixedSchemeEntity = new InputFileMixedSchemeEntity();
	}

	@Override
	public void initializeEntity() {

		LOG.trace("Initializing input file mixed scheme entity for component: "
				+ jaxbTextFileMixedScheme.getId());
		inputFileMixedSchemeEntity.setComponentId(jaxbTextFileMixedScheme
				.getId());
		inputFileMixedSchemeEntity.setBatch(jaxbTextFileMixedScheme.getBatch());
		inputFileMixedSchemeEntity.setCharset(jaxbTextFileMixedScheme
				.getCharset() != null ? jaxbTextFileMixedScheme.getCharset()
				.getValue().value() : "UTF-8");

		inputFileMixedSchemeEntity.setPath(jaxbTextFileMixedScheme.getPath()
				.getUri());
		inputFileMixedSchemeEntity
				.setSafe(jaxbTextFileMixedScheme.getSafe() != null ? jaxbTextFileMixedScheme
						.getSafe().isValue() : false);

		inputFileMixedSchemeEntity.setFieldsList(InputEntityUtils
				.extractInputFields(jaxbTextFileMixedScheme.getOutSocket()
						.get(0).getSchema()
						.getFieldOrRecordOrIncludeExternalSchema()));

		inputFileMixedSchemeEntity.setRuntimeProperties(InputEntityUtils
				.extractRuntimeProperties(jaxbTextFileMixedScheme
						.getRuntimeProperties()));

		inputFileMixedSchemeEntity.setStrict(jaxbTextFileMixedScheme
				.getStrict() != null ? jaxbTextFileMixedScheme.getStrict()
				.isValue() : true);

		inputFileMixedSchemeEntity.setOutSocketList(InputEntityUtils
				.extractOutSocket(jaxbTextFileMixedScheme.getOutSocket()));
		inputFileMixedSchemeEntity
				.setQuote(jaxbTextFileMixedScheme.getQuote() != null ? jaxbTextFileMixedScheme
						.getQuote().getValue() : "");
	}

	@Override
	public void createAssembly(ComponentParameters componentParameters) {
		inputMixedFileAssembly = new InputFileMixedSchemeAssembly(
				inputFileMixedSchemeEntity, componentParameters);

	}

	@Override
	public BaseComponent<InputFileMixedSchemeEntity> getAssembly() {
		return inputMixedFileAssembly;
	}

	private Properties getRunTimeProperties(List<Property> list) {
		Properties properties = new Properties();
		for (Property property : list) {
			properties.put(property.getName(), property.getValue());
		}
		return properties;
	}

	private Properties getRuntimeProperty() {
		return new Properties();
	}

}
