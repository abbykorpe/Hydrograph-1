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
import hydrograph.engine.assembly.entity.OutputFileDelimitedEntity;
import hydrograph.engine.assembly.entity.utils.OutputEntityUtils;
import hydrograph.engine.cascading.assembly.OutputFileDelimitedAssembly;
import hydrograph.engine.cascading.assembly.base.BaseComponent;
import hydrograph.engine.cascading.assembly.generator.base.OutputAssemblyGeneratorBase;
import hydrograph.engine.cascading.assembly.infra.ComponentParameters;
import hydrograph.engine.jaxb.commontypes.TrueFalse;
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent;
import hydrograph.engine.jaxb.outputtypes.TextFileDelimited;
import hydrograph.engine.utilities.GeneralUtilities;

public class OutputFileDelimitedAssemblyGenerator extends OutputAssemblyGeneratorBase {

	private OutputFileDelimitedEntity outputFileDelimitedEntity;
	private TextFileDelimited jaxbOutputFileDelimited;
	private OutputFileDelimitedAssembly outputFileDelimitedAssembly;
	private static Logger LOG = LoggerFactory.getLogger(OutputFileDelimitedAssemblyGenerator.class);

	public OutputFileDelimitedAssemblyGenerator(TypeBaseComponent baseComponent) {
		super(baseComponent);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Map<String, Tap> getSinkTap() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void castComponentFromBase(TypeBaseComponent baseComponent) {
		jaxbOutputFileDelimited = (TextFileDelimited) baseComponent;
	}

	@Override
	public void createEntity() {
		outputFileDelimitedEntity = new OutputFileDelimitedEntity();
	}

	@Override
	public void initializeEntity() {

		LOG.trace("Initializing output file delimited entity for component: " + jaxbOutputFileDelimited.getId());
		outputFileDelimitedEntity.setComponentId(jaxbOutputFileDelimited.getId());
		outputFileDelimitedEntity.setBatch(jaxbOutputFileDelimited.getBatch());
		outputFileDelimitedEntity.setPath(jaxbOutputFileDelimited.getPath().getUri());
		outputFileDelimitedEntity.setSafe(
				jaxbOutputFileDelimited.getSafe() != null ? jaxbOutputFileDelimited.getSafe().isValue() : false);
		outputFileDelimitedEntity
				.setDelimiter(GeneralUtilities.parseHex(jaxbOutputFileDelimited.getDelimiter().getValue()));
		outputFileDelimitedEntity.setCharset(jaxbOutputFileDelimited.getCharset() != null
				? jaxbOutputFileDelimited.getCharset().getValue().value() : "UTF-8");

		outputFileDelimitedEntity.setFieldsList(OutputEntityUtils.extractOutputFields(
				jaxbOutputFileDelimited.getInSocket().get(0).getSchema().getFieldOrRecordOrIncludeExternalSchema()));

		outputFileDelimitedEntity.setRuntimeProperties(
				OutputEntityUtils.extractRuntimeProperties(jaxbOutputFileDelimited.getRuntimeProperties()));
		outputFileDelimitedEntity.setQuote(
				jaxbOutputFileDelimited.getQuote() != null ? jaxbOutputFileDelimited.getQuote().getValue() : null);

		outputFileDelimitedEntity.setHasHeader(jaxbOutputFileDelimited.getHasHeader() != null
				? jaxbOutputFileDelimited.getHasHeader().isValue() : false);
		outputFileDelimitedEntity.setStrict(
				jaxbOutputFileDelimited.getStrict() != null ? jaxbOutputFileDelimited.getStrict().isValue() : true);
		if (jaxbOutputFileDelimited.getOverWrite() != null
				&& (TrueFalse.FALSE).equals(jaxbOutputFileDelimited.getOverWrite().getValue()))
			outputFileDelimitedEntity.setOverWrite(false);
		else
			outputFileDelimitedEntity.setOverWrite(true);
	}

	@Override
	public void createAssembly(ComponentParameters componentParameters) {
		outputFileDelimitedAssembly = new OutputFileDelimitedAssembly(outputFileDelimitedEntity, componentParameters);
	}

	@Override
	public BaseComponent<OutputFileDelimitedEntity> getAssembly() {
		return outputFileDelimitedAssembly;
	}
}