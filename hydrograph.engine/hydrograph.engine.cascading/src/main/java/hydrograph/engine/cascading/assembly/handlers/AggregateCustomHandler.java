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
import hydrograph.engine.transformation.userfunctions.base.AggregateTransformBase;
import hydrograph.engine.transformation.userfunctions.base.ReusableRow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cascading.flow.FlowProcess;
import cascading.management.annotation.Property;
import cascading.management.annotation.PropertyDescription;
import cascading.management.annotation.Visibility;
import cascading.operation.Aggregator;
import cascading.operation.AggregatorCall;
import cascading.operation.BaseOperation;
import cascading.operation.OperationCall;
import cascading.tuple.Fields;
import cascading.tuple.Tuple;

public class AggregateCustomHandler extends BaseOperation<CustomHandlerContext<AggregateTransformBase>>
		implements Aggregator<CustomHandlerContext<AggregateTransformBase>> {

	Logger LOG = LoggerFactory.getLogger(AggregateCustomHandler.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = -5599319878964805478L;
	/**
	 * 
	 */

	private ArrayList<Properties> props;
	private ArrayList<String> transformClassNames;
	private FieldManupulatingHandler fieldManupulatingHandler;

	public AggregateCustomHandler(FieldManupulatingHandler fieldManupulatingHandler, ArrayList<Properties> props,
			ArrayList<String> transformClassNames) {
		super(fieldManupulatingHandler.getInputFields().size(), fieldManupulatingHandler.getOutputFields());
		this.props = props;
		this.transformClassNames = transformClassNames;
		this.fieldManupulatingHandler = fieldManupulatingHandler;
		LOG.trace("AggregateCustomHandler object created for: " + Arrays.toString(transformClassNames.toArray()));
	}

	public Fields getOutputFields() {
		return fieldManupulatingHandler.getOutputFields();
	}

	public Fields getInputFields() {
		return fieldManupulatingHandler.getInputFields();
	}

	public Fields getKeyFields() {
		return fieldManupulatingHandler.keyFields;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void prepare(FlowProcess flowProcess, OperationCall<CustomHandlerContext<AggregateTransformBase>> call) {

		CustomHandlerContext<AggregateTransformBase> context = new CustomHandlerContext<AggregateTransformBase>(
				fieldManupulatingHandler, transformClassNames);

		int counter = -1;
		for (AggregateTransformBase transformInstance : context.getTransformInstances()) {
			counter = counter + 1;
			LOG.trace("calling prepare method of: " + transformInstance.getClass().getName());
			transformInstance.prepare(props.get(counter), context.getInputRow(counter).getFieldNames(),
					context.getOutputRow(counter).getFieldNames(),
					ReusableRowHelper.getListFromFields(fieldManupulatingHandler.keyFields));
		}

		call.setContext(context);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void cleanup(FlowProcess flowProcess, OperationCall<CustomHandlerContext<AggregateTransformBase>> call) {

		CustomHandlerContext<AggregateTransformBase> context = call.getContext();

		for (AggregateTransformBase transformInstance : context.getTransformInstances()) {
			LOG.trace("calling cleanup method of: " + transformInstance.getClass().getName());
			transformInstance.cleanup();
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void aggregate(FlowProcess flowProcess, AggregatorCall<CustomHandlerContext<AggregateTransformBase>> call) {
		CustomHandlerContext<AggregateTransformBase> context = call.getContext();

		int counter = -1;
		for (AggregateTransformBase transformInstance : context.getTransformInstances()) {
			LOG.trace("calling aggregate method of: " + transformInstance.getClass().getName());
			counter = counter + 1;
			ReusableRow reusableRow = ReusableRowHelper.extractFromTuple(
					fieldManupulatingHandler.getInputPositions(counter), call.getArguments().getTuple(),
					context.getInputRow(counter));
			try {
				transformInstance.aggregate(reusableRow);
			} catch (Exception e) {
				LOG.error("Exception in aggregate method of: " + transformInstance.getClass().getName()
						+ ".\nRow being processed: " + call.getArguments(), e);
				throw e;
			}
		}

		call.getContext().setUserObject(call.getArguments().getTuple());

		// // set passthrough row, copy pass through fields
		// ReusableRowHelper.extractFromTuple(fieldManupulatingHandler
		// .getInputPassThroughPositions(), call.getArguments().getTuple(),
		// context.getPassThroughRow());

		// // set map row, copy map fields
		// ReusableRowHelper.extractFromTuple(fieldManupulatingHandler
		// .getMapSourceFieldPositions(), call.getArguments().getTuple(),
		// context.getMapRow());

		// // set operation row from pass through row and map row
		// ReusableRowHelper.setOperationRowFromPassThroughAndMapRow(
		// fieldManupulatingHandler.getMapFields(), context.getMapRow(),
		// context.getPassThroughRow(), context.getOperationRow());
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void complete(FlowProcess flowProcess, AggregatorCall<CustomHandlerContext<AggregateTransformBase>> call) {
		CustomHandlerContext<AggregateTransformBase> context = call.getContext();

		// call on group complete to gather results for earlier group
		int counter = -1;
		for (AggregateTransformBase transformInstance : context.getTransformInstances()) {
			LOG.trace("calling onCompleteGroup method of: " + transformInstance.getClass().getName());
			counter = counter + 1;
			transformInstance.onCompleteGroup(context.getOutputRow(counter));
		}

		// // set operation row, copy operation fields
		// ReusableRowHelper.extractOperationRowFromAllOutputRow(
		// context.getAllOutputRow(), context.getOperationRow());
		//
		//
		// ReusableRowHelper.setTupleEntryFromResuableRowAndReset(call
		// .getContext().getOutputTupleEntry(), context.getOperationRow());

		// set output tuple entry with map field values
		TupleHelper.setTupleOnPositions(fieldManupulatingHandler.getMapSourceFieldPositions(),
				(Tuple) call.getContext().getUserObject(), fieldManupulatingHandler.getMapTargetFieldPositions(),
				call.getContext().getOutputTupleEntry().getTuple());

		// set output tuple entry with passthrough field values
		TupleHelper.setTupleOnPositions(fieldManupulatingHandler.getInputPassThroughPositions(),
				(Tuple) call.getContext().getUserObject(), fieldManupulatingHandler.getOutputPassThroughPositions(),
				call.getContext().getOutputTupleEntry().getTuple());

		// set output tuple entry with operation output Fields
		ReusableRowHelper.setTupleEntryFromResuableRowsAndReset(call.getContext().getOutputTupleEntry(),
				context.getAllOutputRow(), fieldManupulatingHandler.getAllOutputPositions());

		// add output to collector
		call.getOutputCollector().add(call.getContext().getOutputTupleEntry());

	}

	@SuppressWarnings("rawtypes")
	@Override
	public void start(FlowProcess flowProcess, AggregatorCall<CustomHandlerContext<AggregateTransformBase>> call) {

		// not required

	}

	@Property(name = "Operation Classes", visibility = Visibility.PUBLIC)
	@PropertyDescription(value = "Aggregate Operations executed by this Component")
	public String[] getOperationClasses() {

		String[] classes = new String[transformClassNames.size()];
		classes = transformClassNames.toArray(classes);
		return classes;
	}

}