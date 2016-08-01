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
package hydrograph.engine.cascading.assembly.handlers;

import hydrograph.engine.cascading.assembly.context.CustomHandlerContext;
import hydrograph.engine.cascading.assembly.context.RecordFilterContext;
import hydrograph.engine.cascading.utilities.ReusableRowHelper;
import hydrograph.engine.expression.antlr.custom.visitor.ValidationAPI;
import hydrograph.engine.transformation.userfunctions.base.FilterBase;

import java.util.Arrays;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bsh.Interpreter;
import cascading.operation.FilterCall;
import cascading.operation.OperationCall;
import cascading.tuple.Fields;
import cascading.tuple.TupleEntry;

public class FilterCustomHandler implements RecordFilterHandlerBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8155434168060955013L;
	private Fields inputFields;
	private String transformClass;
	private Properties userProperties;
	private String operationExpression;
	private boolean isUnused = false;

	private static Logger LOG = LoggerFactory
			.getLogger(FilterCustomHandler.class);

	public FilterCustomHandler(Fields inputFields, String transformClass,
			Properties userProperties) {

		this.inputFields = inputFields;
		this.transformClass = transformClass;
		this.userProperties = userProperties;
		LOG.trace("FilterCustomHandler object created for: " + transformClass);
	}

	public FilterCustomHandler(Fields inputFields, String transformClass,
			Properties userProperties, boolean isUnused,String operationExpression) {

		this.inputFields = inputFields;
		this.transformClass = transformClass;
		this.userProperties = userProperties;
		this.isUnused = isUnused;
		this.operationExpression = operationExpression;
		if (operationExpression == null)
			LOG.trace("FilterCustomHandler object created for: " + transformClass);
		else
			LOG.trace("FilterCustomHandler object created for: " + operationExpression);
	}

	@Override
	public Object prepare() {
		if(transformClass != null){
			CustomHandlerContext<FilterBase> context = new CustomHandlerContext<FilterBase>(
					inputFields, transformClass);
	
			LOG.trace("calling prepare method of: "
					+ context.getSingleTransformInstance().getClass().getName());
			try {
				context.getSingleTransformInstance().prepare(userProperties,
						context.getSingleInputRow().getFieldNames());
			} catch (Exception e) {
				LOG.error(
						"Exception in prepare method of: "
								+ context.getSingleTransformInstance().getClass().getName()
								+ ".\nArguments passed to prepare() method are: \nProperties: "
								+ userProperties
								+ "\nInput Fields: "
								+ Arrays.toString(context.getSingleInputRow().getFieldNames().toArray()), e);
				throw new RuntimeException(
						"Exception in prepare method of: "
								+ context.getSingleTransformInstance().getClass().getName()
								+ ".\nArguments passed to prepare() method are: \nProperties: "
								+ userProperties
								+ "\nInput Fields: "
								+ Arrays.toString(context.getSingleInputRow().getFieldNames().toArray()), e);
			}
			return context;
		}
		return null;
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean isRemove(FilterCall<RecordFilterContext> call) {

		CustomHandlerContext<FilterBase> context = (CustomHandlerContext<FilterBase>) call.getContext()
				.getHandlerContext();

		TupleEntry tupleEntry = call.getArguments();
		// LOG.trace("calling isRemove method of: " +
		// context.getSingleTransformInstance().getClass().getName());

		try {
			if (operationExpression != null) {
				Interpreter interpreter = new Interpreter();
				for (int i = 0; i < inputFields.size(); i++) {
					interpreter.set(String.valueOf(inputFields.get(i)), tupleEntry.getObject(inputFields.get(i)));
				}
				return isUnused ? !(boolean) interpreter.eval(ValidationAPI.getValidExpression(operationExpression))
						: (boolean) interpreter.eval(ValidationAPI.getValidExpression(operationExpression));
			} else {
				boolean isRemove = context.getSingleTransformInstance().isRemove(ReusableRowHelper
						.extractFromTuple(call.getArguments().getTuple(), context.getSingleInputRow()));

				return isUnused ? !isRemove : isRemove;
			}
		} catch (Exception e) {
			LOG.error("Exception in isRemove method of: " + context.getSingleTransformInstance().getClass().getName()
					+ ".\nRow being processed: " + call.getArguments(), e);
			throw new RuntimeException(e);
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public void cleanup(OperationCall<RecordFilterContext> call) {

		CustomHandlerContext<FilterBase> context = (CustomHandlerContext<FilterBase>) call
				.getContext().getHandlerContext();
		if (context != null) {
			LOG.trace("calling cleanup method of: "
					+ context.getSingleTransformInstance().getClass().getName());
			context.getSingleTransformInstance().cleanup();
		}

	}

	public String getTransformClass() {
		return transformClass;
	}

}
