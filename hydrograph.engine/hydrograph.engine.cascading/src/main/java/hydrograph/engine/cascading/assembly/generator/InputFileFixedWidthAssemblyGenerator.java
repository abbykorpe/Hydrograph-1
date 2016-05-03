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

import hydrograph.engine.assembly.entity.InputFileFixedWidthEntity;
import hydrograph.engine.assembly.entity.utils.InputEntityUtils;
import hydrograph.engine.cascading.assembly.InputFileFixedWidthAssembly;
import hydrograph.engine.cascading.assembly.base.BaseComponent;
import hydrograph.engine.cascading.assembly.generator.base.InputAssemblyGeneratorBase;
import hydrograph.engine.cascading.assembly.infra.ComponentParameters;
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent;
import hydrograph.engine.jaxb.commontypes.TypeProperties.Property;
import hydrograph.engine.jaxb.inputtypes.TextFileFixedWidth;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cascading.tap.Tap;

public class InputFileFixedWidthAssemblyGenerator extends
		InputAssemblyGeneratorBase {
	private InputFileFixedWidthAssembly inputFileFixedWidthAssembly;
	private TextFileFixedWidth jaxbInputFileFixedWidth;
	private InputFileFixedWidthEntity inputFileFixedWidthEntity;
	private static Logger LOG = LoggerFactory
			.getLogger(InputFileFixedWidthAssemblyGenerator.class);

	public InputFileFixedWidthAssemblyGenerator(TypeBaseComponent baseComponent) {
		super(baseComponent);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map<String, Tap> getSourceTap() {
		return null;
	}

	@Override
	public void castComponentFromBase(TypeBaseComponent baseComponent) {
		jaxbInputFileFixedWidth = (TextFileFixedWidth) baseComponent;
	}

	@Override
	public void createEntity() {
		inputFileFixedWidthEntity = new InputFileFixedWidthEntity();
	}

	@Override
	public void initializeEntity() {

		LOG.trace("Initializing input file fixed width entity for component: "
				+ jaxbInputFileFixedWidth.getId());
		inputFileFixedWidthEntity.setComponentId(jaxbInputFileFixedWidth
				.getId());
		inputFileFixedWidthEntity.setPhase(jaxbInputFileFixedWidth.getPhase()
				.intValue());
		inputFileFixedWidthEntity.setPath(jaxbInputFileFixedWidth.getPath()
				.getUri());
		inputFileFixedWidthEntity
				.setSafe(jaxbInputFileFixedWidth.getSafe() != null ? jaxbInputFileFixedWidth
						.getSafe().isValue() : false);
		inputFileFixedWidthEntity
				.setStrict(jaxbInputFileFixedWidth.getStrict() != null ? jaxbInputFileFixedWidth
						.getStrict().isValue() : true);
		inputFileFixedWidthEntity.setCharset(jaxbInputFileFixedWidth
				.getCharset() != null ? jaxbInputFileFixedWidth.getCharset()
				.getValue().value() : "UTF-8");

		inputFileFixedWidthEntity.setFieldsList(InputEntityUtils
				.extractInputFields(jaxbInputFileFixedWidth.getOutSocket()
						.get(0).getSchema()
						.getFieldOrRecordOrIncludeExternalSchema()));
		inputFileFixedWidthEntity.setRuntimeProperties(InputEntityUtils
				.extractRuntimeProperties(jaxbInputFileFixedWidth
						.getRuntimeProperties()));

		inputFileFixedWidthEntity.setOutSocketList(InputEntityUtils
				.extractOutSocket(jaxbInputFileFixedWidth.getOutSocket()));
	}

	@Override
	public void createAssembly(ComponentParameters componentParameters) {
		inputFileFixedWidthAssembly = new InputFileFixedWidthAssembly(
				inputFileFixedWidthEntity, componentParameters);
	}

	@Override
	public BaseComponent getAssembly() {
		return inputFileFixedWidthAssembly;
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
