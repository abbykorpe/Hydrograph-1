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
import hydrograph.engine.transformation.userfunctions.base.CumulateTransformBase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cascading.flow.FlowProcess;
import cascading.management.annotation.Property;
import cascading.management.annotation.PropertyDescription;
import cascading.management.annotation.Visibility;
import cascading.operation.BaseOperation;
import cascading.operation.Buffer;
import cascading.operation.BufferCall;
import cascading.operation.OperationCall;
import cascading.tuple.Fields;
import cascading.tuple.TupleEntry;

public class CumulateCustomHandler extends
		BaseOperation<CustomHandlerContext<CumulateTransformBase>> implements
		Buffer<CustomHandlerContext<CumulateTransformBase>> {

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
	private static Logger LOG = LoggerFactory
			.getLogger(CumulateCustomHandler.class);

	public CumulateCustomHandler(
			FieldManupulatingHandler fieldManupulatingHandler,
			ArrayList<Properties> props, ArrayList<String> transformClassNames) {
		super(fieldManupulatingHandler.getInputFields().size(),
				fieldManupulatingHandler.getOutputFields());
		this.props = props;
		this.transformClassNames = transformClassNames;
		this.fieldManupulatingHandler = fieldManupulatingHandler;

		LOG.trace("CumulateCustomHandler object created for: "
				+ Arrays.toString(transformClassNames.toArray()));
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
	public void prepare(FlowProcess flowProcess,
			OperationCall<CustomHandlerContext<CumulateTransformBase>> call) {

		CustomHandlerContext<CumulateTransformBase> context = new CustomHandlerContext<CumulateTransformBase>(
				fieldManupulatingHandler, transformClassNames);

		int counter = -1;
		for (CumulateTransformBase transformInstance : context
				.getTransformInstances()) {
			counter = counter + 1;
			LOG.trace("calling prepare method of: "
					+ transformInstance.getClass().getName());
			transformInstance
					.prepare(
							props.get(counter),
							context.getInputRow(counter).getFieldNames(),
							context.getOutputRow(counter).getFieldNames(),
							ReusableRowHelper
									.getListFromFields(fieldManupulatingHandler.keyFields));
		}

		call.setContext(context);

	}

	@SuppressWarnings("rawtypes")
	@Override
	public void cleanup(FlowProcess flowProcess,
			OperationCall<CustomHandlerContext<CumulateTransformBase>> call) {

		CustomHandlerContext<CumulateTransformBase> context = call.getContext();

		for (CumulateTransformBase transformInstance : context
				.getTransformInstances()) {
			LOG.trace("calling cleanup method of: "
					+ transformInstance.getClass().getName());
			transformInstance.cleanup();
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void operate(FlowProcess arg0,
			BufferCall<CustomHandlerContext<CumulateTransformBase>> call) {

		CustomHandlerContext<CumulateTransformBase> context = call.getContext();

		// TupleEntry group = call.getGroup();

		// get all the current argument values for this grouping
		Iterator<TupleEntry> currentGroupOfTupleEntry = call
				.getArgumentsIterator();

		// create a Tuple to hold our result values

		while (currentGroupOfTupleEntry.hasNext()) {
			TupleEntry currentTupleEntry = currentGroupOfTupleEntry.next();

			int counter = -1;
			for (CumulateTransformBase transformInstance : context
					.getTransformInstances()) {
				counter = counter + 1;
				LOG.trace("calling cumulate method of: "
						+ transformInstance.getClass().getName());
				try {
					transformInstance.cumulate(ReusableRowHelper
							.extractFromTuple(fieldManupulatingHandler
									.getInputPositions(counter),
									currentTupleEntry.getTuple(), context
											.getInputRow(counter)), context
							.getOutputRow(counter));
				} catch (Exception e) {
					LOG.error("Exception in cumulate method of: "
							+ transformInstance.getClass().getName()
							+ ".\nRow being processed: "
							+ currentTupleEntry, e);
					throw e;
				}
			}

//			// set passthrough row copy pass through fields
//			ReusableRowHelper.extractFromTuple(
//					fieldManupulatingHandler.getInputPassThroughPositions(),
//					currentTupleEntry.getTuple(), context.getPassThroughRow());
//
//			// set map row, copy map fields
//			ReusableRowHelper.extractFromTuple(
//					fieldManupulatingHandler.getMapSourceFieldPositions(),
//					currentTupleEntry.getTuple(), context.getMapRow());
//
//			// set operation row from pass through row and map row
//			ReusableRowHelper.setOperationRowFromPassThroughAndMapRow(
//					fieldManupulatingHandler.getMapFields(),
//					context.getMapRow(), context.getPassThroughRow(),
//					context.getOperationRow());

//	
//
//			// set operation row, copy operation fields
//			ReusableRowHelper.extractOperationRowFromAllOutputRow(
//					context.getAllOutputRow(), context.getOperationRow());
//
//			// Set all output fields in order
//			ReusableRowHelper.setTupleEntryFromResuableRowAndReset(
//					context.getOutputTupleEntry(), context.getOperationRow());

			
			// set output tuple entry with map field values
			TupleHelper.setTupleOnPositions(fieldManupulatingHandler
					.getMapSourceFieldPositions(), currentTupleEntry.getTuple(),
					fieldManupulatingHandler.getMapTargetFieldPositions(), call
							.getContext().getOutputTupleEntry().getTuple());
			
			// set output tuple entry with passthrough field values
			TupleHelper.setTupleOnPositions(fieldManupulatingHandler
					.getInputPassThroughPositions(),
					currentTupleEntry.getTuple(), fieldManupulatingHandler
							.getOutputPassThroughPositions(), call.getContext()
							.getOutputTupleEntry().getTuple());
			
			// set output tuple entry with operation output Fields
			ReusableRowHelper.setTupleEntryFromResuableRowsAndReset(call
					.getContext().getOutputTupleEntry(), context.getAllOutputRow(),
					fieldManupulatingHandler.getAllOutputPositions());
			
			// return the result Tuple
			call.getOutputCollector().add(context.getOutputTupleEntry());

		}
		for (CumulateTransformBase transformInstance : context
				.getTransformInstances()) {
			LOG.trace("calling onCompleteGroup method of: "
					+ transformInstance.getClass().getName());
			transformInstance.onCompleteGroup();
		}
	}
	
	@Property(name = "Operation Classes", visibility = Visibility.PUBLIC)
	@PropertyDescription(value = "Cumulate Operations executed by this Component")
	public String[] getOperationClasses() {

		String[] classes = new String[transformClassNames.size()];
		classes = transformClassNames.toArray(classes);
		return classes;
	}
}