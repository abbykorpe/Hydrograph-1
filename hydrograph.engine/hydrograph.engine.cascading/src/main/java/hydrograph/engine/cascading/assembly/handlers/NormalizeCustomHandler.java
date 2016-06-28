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
import hydrograph.engine.cascading.utilities.ReusableRowHelper;
import hydrograph.engine.cascading.utilities.TupleHelper;
import hydrograph.engine.transformation.userfunctions.base.NormalizeTransformBase;
import hydrograph.engine.transformation.userfunctions.base.OutputDispatcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cascading.flow.FlowProcess;
import cascading.management.annotation.Property;
import cascading.management.annotation.PropertyDescription;
import cascading.management.annotation.Visibility;
import cascading.operation.BaseOperation;
import cascading.operation.Function;
import cascading.operation.FunctionCall;
import cascading.operation.OperationCall;
import cascading.tuple.Fields;

public class NormalizeCustomHandler extends
		BaseOperation<CustomHandlerContext<NormalizeTransformBase>> implements
		Function<CustomHandlerContext<NormalizeTransformBase>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 155032640399766097L;
	private ArrayList<Properties> userProperties;
	private ArrayList<String> transformClassName;
	private FieldManupulatingHandler fieldManupulatingHandler;
	private static Logger LOG = LoggerFactory
			.getLogger(NormalizeCustomHandler.class);

	public NormalizeCustomHandler(
			FieldManupulatingHandler fieldManupulatingHandler,
			Properties userProperties, String transformClassName) {

		super(fieldManupulatingHandler.getInputFields().size(),
				fieldManupulatingHandler.getOutputFields());

		this.userProperties = new ArrayList<>();
		this.userProperties.add(userProperties);

		this.transformClassName = new ArrayList<>();
		this.transformClassName.add(transformClassName);

		this.fieldManupulatingHandler = fieldManupulatingHandler;
		LOG.trace("NormalizeCustomHandler object created for: "
				+ transformClassName);
	}

	public NormalizeCustomHandler(
			FieldManupulatingHandler fieldManupulatingHandler,
			ArrayList<Properties> props, ArrayList<String> transformClassNames) {
		super(fieldManupulatingHandler.getInputFields().size(),
				fieldManupulatingHandler.getOutputFields());
		this.userProperties = props;
		this.transformClassName = transformClassNames;
		this.fieldManupulatingHandler = fieldManupulatingHandler;
		LOG.trace("AggregateCustomHandler object created for: "
				+ Arrays.toString(transformClassNames.toArray()));
	}

	public Fields getInputFields() {
		return fieldManupulatingHandler.getInputFields();
	}

	public Fields getOutputFields() {
		return fieldManupulatingHandler.getOutputFields();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void prepare(
			FlowProcess flowProcess,
			OperationCall<CustomHandlerContext<NormalizeTransformBase>> operationCall) {

		CustomHandlerContext<NormalizeTransformBase> context = new CustomHandlerContext<NormalizeTransformBase>(
			fieldManupulatingHandler, transformClassName);

		context.setUserObject(new NormalizeOutputDispatcher(operationCall));

		LOG.trace("calling prepare method of: "
				+ context.getSingleTransformInstance().getClass().getName());
		try {
			context.getSingleTransformInstance().prepare(userProperties.get(0));
		} catch (Exception e) {
			LOG.error(
					"Exception in prepare method of: "
							+ context.getSingleTransformInstance().getClass().getName()
							+ ".\nArguments passed to prepare() method are: \nProperties: "
							+ userProperties, e);
			throw new RuntimeException(
					"Exception in prepare method of: "
							+ context.getSingleTransformInstance().getClass().getName()
							+ ".\nArguments passed to prepare() method are: \nProperties: "
							+ userProperties, e);
		}

		operationCall.setContext(context);

	}

	@SuppressWarnings("rawtypes")
	@Override
	public void operate(FlowProcess arg0,
			FunctionCall<CustomHandlerContext<NormalizeTransformBase>> call) {
		CustomHandlerContext<NormalizeTransformBase> context = call
				.getContext();

		LOG.trace("calling normalize method of: "
				+ context.getSingleTransformInstance().getClass().getName());
		try {
			context.getTransformInstances()
					.get(0)
					.Normalize(ReusableRowHelper.extractFromTuple(
									fieldManupulatingHandler
											.getInputPositions(), call
											.getArguments().getTuple(), context
											.getSingleInputRow()),
							context.getSingleOutputRow(),
							(NormalizeOutputDispatcher) context.getUserObject());

		} catch (Exception e) {
			LOG.error(
					"Exception in normalize method of: "
						+ context.getSingleTransformInstance().getClass()
								.getName() + ".\nRow being processed: "
						+ call.getArguments(), e);
			throw new RuntimeException("Exception in normalize method of: "
					+ context.getSingleTransformInstance().getClass()
					.getName() + ".\nRow being processed: "
					+ call.getArguments(), e);
		}

	}

	@SuppressWarnings("rawtypes")
	@Override
	public void cleanup(FlowProcess flowProcess,
			OperationCall<CustomHandlerContext<NormalizeTransformBase>> call) {
		CustomHandlerContext<NormalizeTransformBase> context = call
				.getContext();

		LOG.trace("calling cleanup method of: "
				+ context.getSingleTransformInstance().getClass().getName());
		context.getSingleTransformInstance().cleanup();

	}

	@Property(name = "Operation Class", visibility = Visibility.PUBLIC)
	@PropertyDescription(value = "Normalize Operation executed by this Component")
	public ArrayList<String> getOperationClass() {

		return transformClassName;
	}

	private class NormalizeOutputDispatcher implements OutputDispatcher {

		FunctionCall<CustomHandlerContext<NormalizeTransformBase>> call;

		public NormalizeOutputDispatcher(
				OperationCall<CustomHandlerContext<NormalizeTransformBase>> call) {
			this.call = (FunctionCall<CustomHandlerContext<NormalizeTransformBase>>) call;
		}

		@Override
		public void sendOutput() {
			LOG.trace("entering sendOutput method");
			CustomHandlerContext<NormalizeTransformBase> context = call
					.getContext();

			// set output tuple entry with map field values
			TupleHelper.setTupleOnPositions(fieldManupulatingHandler
					.getMapSourceFieldPositions(), call.getArguments()
					.getTuple(), fieldManupulatingHandler
					.getMapTargetFieldPositions(), call.getContext()
					.getOutputTupleEntry().getTuple());

			// set output tuple entry with passthrough field values
			TupleHelper.setTupleOnPositions(
					fieldManupulatingHandler.getInputPassThroughPositions(),
					call.getArguments().getTuple(),
					fieldManupulatingHandler.getOutputPassThroughPositions(),
					call.getContext().getOutputTupleEntry().getTuple());

			// Set all output fields in order
			ReusableRowHelper.setTupleEntryFromResuableRowAndReset(context
					.getOutputTupleEntry(), context.getSingleOutputRow(),
					fieldManupulatingHandler.getAllOutputPositions().get(0));

			call.getOutputCollector().add(context.getOutputTupleEntry());
		}
	}
}
