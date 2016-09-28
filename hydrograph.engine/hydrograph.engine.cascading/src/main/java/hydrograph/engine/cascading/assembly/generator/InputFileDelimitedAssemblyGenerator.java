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

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cascading.tap.Tap;
import hydrograph.engine.assembly.entity.InputFileDelimitedEntity;
import hydrograph.engine.assembly.entity.utils.InputEntityUtils;
import hydrograph.engine.cascading.assembly.InputFileDelimitedAssembly;
import hydrograph.engine.cascading.assembly.base.BaseComponent;
import hydrograph.engine.cascading.assembly.generator.base.InputAssemblyGeneratorBase;
import hydrograph.engine.cascading.assembly.infra.ComponentParameters;
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent;
import hydrograph.engine.jaxb.inputtypes.TextFileDelimited;
import hydrograph.engine.utilities.GeneralUtilities;

public class InputFileDelimitedAssemblyGenerator extends InputAssemblyGeneratorBase {

	private InputFileDelimitedEntity inputFileDelimitedEntity;
	private InputFileDelimitedAssembly inputFileDelimitedAssembly;
	private TextFileDelimited jaxbFileDelimited;
	//private GeneralUtilities generalUtilities ;
	private static Logger LOG = LoggerFactory.getLogger(InputFileDelimitedAssemblyGenerator.class);

	public InputFileDelimitedAssemblyGenerator(TypeBaseComponent baseComponent) {
		super(baseComponent);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map<String, Tap> getSourceTap() {
		return null;
	}

	@Override
	public void castComponentFromBase(TypeBaseComponent baseComponent) {
		jaxbFileDelimited = (TextFileDelimited) baseComponent;
	}

	@Override
	public void createEntity() {
		inputFileDelimitedEntity = new InputFileDelimitedEntity();
	}

	@Override
	public void initializeEntity() {

		LOG.trace("Initializing input file delimited entity for component: " + jaxbFileDelimited.getId());
		inputFileDelimitedEntity.setComponentId(jaxbFileDelimited.getId());
		inputFileDelimitedEntity.setBatch(jaxbFileDelimited.getBatch());
		inputFileDelimitedEntity.setPath(jaxbFileDelimited.getPath().getUri());
		inputFileDelimitedEntity.setDelimiter(GeneralUtilities.parseHex(jaxbFileDelimited.getDelimiter().getValue()));
		inputFileDelimitedEntity.setSafe(jaxbFileDelimited.getSafe() != null ? jaxbFileDelimited.getSafe().isValue() : false);
		inputFileDelimitedEntity
				.setStrict(jaxbFileDelimited.getStrict() != null ? jaxbFileDelimited.getStrict().isValue() : true);
		inputFileDelimitedEntity.setCharset(
				jaxbFileDelimited.getCharset() != null ? jaxbFileDelimited.getCharset().getValue().value() : "UTF-8");
		inputFileDelimitedEntity.setFieldsList(InputEntityUtils.extractInputFields(
				jaxbFileDelimited.getOutSocket().get(0).getSchema().getFieldOrRecordOrIncludeExternalSchema()));

		inputFileDelimitedEntity.setHasHeader(
				jaxbFileDelimited.getHasHeader() != null ? jaxbFileDelimited.getHasHeader().isValue() : false);
		inputFileDelimitedEntity
				.setQuote(jaxbFileDelimited.getQuote() != null ? jaxbFileDelimited.getQuote().getValue() : null);

		inputFileDelimitedEntity.setRuntimeProperties(
				InputEntityUtils.extractRuntimeProperties(jaxbFileDelimited.getRuntimeProperties()));

		inputFileDelimitedEntity.setOutSocketList(InputEntityUtils.extractOutSocket(jaxbFileDelimited.getOutSocket()));

	}

	@Override
	public void createAssembly(ComponentParameters componentParameters) {
		inputFileDelimitedAssembly = new InputFileDelimitedAssembly(inputFileDelimitedEntity, componentParameters);
	}

	@Override
	public BaseComponent<InputFileDelimitedEntity> getAssembly() {
		return inputFileDelimitedAssembly;
	}
}
