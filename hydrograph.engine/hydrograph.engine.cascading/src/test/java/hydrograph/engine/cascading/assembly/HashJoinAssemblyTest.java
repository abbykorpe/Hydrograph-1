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
package hydrograph.engine.cascading.assembly;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import hydrograph.engine.assembly.entity.HashJoinEntity;
import hydrograph.engine.assembly.entity.elements.JoinKeyFields;
import hydrograph.engine.assembly.entity.elements.MapField;
import hydrograph.engine.assembly.entity.elements.OutSocket;
import hydrograph.engine.assembly.entity.elements.PassThroughField;
import hydrograph.engine.cascading.assembly.HashJoinAssembly;
import hydrograph.engine.cascading.assembly.infra.ComponentParameters;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import cascading.pipe.Pipe;
import cascading.tuple.Fields;
import cascading.tuple.Tuple;

import com.hotels.plunger.Bucket;
import com.hotels.plunger.Data;
import com.hotels.plunger.DataBuilder;
import com.hotels.plunger.Plunger;

/**
 * Test HashJoin sub assembly. The tests are written using plunger framework
 * 
 * @author Prabodh
 */
public class HashJoinAssemblyTest {

	@Before
	public void setup() {
		// TODO: Add setup related code here
	}

	/**
	 * Test simple hashJoin operation
	 */
	@Test
	public void TestSimpleHashJoin() {

		Plunger plunger = new Plunger();

		Data file1 = new DataBuilder(new Fields("col1", "col2", "col3"))
				.addTuple("C1R1", "C2R1", "C3R1")
				.addTuple("C1R2", "C2R2", "C3R2").build();

		// pipe corresponding to an input of hashJoin component
		Pipe pipe1 = plunger.newNamedPipe("pipe1", file1);

		Data file2 = new DataBuilder(new Fields("col1", "col4", "col5"))
				.addTuple("C1R1", "C4R1", "C5R1")
				.addTuple("C1R3", "C4R2", "C5R2").build();

		// pipe corresponding to an input of hashJoin component
		Pipe pipe2 = plunger.newNamedPipe("pipe2", file2);

		ComponentParameters parameters = new ComponentParameters();

		parameters.addInputPipe(pipe1); // first input to hashJoin component
		parameters.addInputPipe(pipe2); // second input to hashJoin component

		parameters.addInputFields(new Fields("col1", "col2", "col3"));
		parameters.addInputFields(new Fields("col1", "col4", "col5"));

		parameters.addinSocketId("in0");
		parameters.addinSocketId("in1");

		HashJoinEntity hashJoinEntity = new HashJoinEntity();

		hashJoinEntity.setComponentId("testHashJoin"); // set the name of the
														// component
		hashJoinEntity.setMatch("first");
		// set key fields
		ArrayList<JoinKeyFields> keyFieldsList = new ArrayList<JoinKeyFields>();
		keyFieldsList.add(new JoinKeyFields("in0", true, new String[] { "col1" }));
		keyFieldsList.add(new JoinKeyFields("in1", true, new String[] { "col1" }));
		hashJoinEntity.setKeyFields(keyFieldsList);

		// create outSocket
		OutSocket outSocket1 = new OutSocket("out0");
		outSocket1.setSocketType("out");

		// set map fields
		List<MapField> mapFieldsList = new ArrayList<>();
		mapFieldsList.add(new MapField("col1", "col1", "in0"));
		outSocket1.setMapFieldsList(mapFieldsList);

		// set pass through fields
		List<PassThroughField> passThroughFieldsList1 = new ArrayList<>();
		passThroughFieldsList1.add(new PassThroughField("col2", "in0"));
		passThroughFieldsList1.add(new PassThroughField("col3", "in0"));
		passThroughFieldsList1.add(new PassThroughField("col4", "in1"));
		passThroughFieldsList1.add(new PassThroughField("col5", "in1"));
		outSocket1.setPassThroughFieldsList(passThroughFieldsList1);

		// add outSocket in list
		List<OutSocket> outSocketList = new ArrayList<>();
		outSocketList.add(outSocket1);
		hashJoinEntity.setOutSocketList(outSocketList);

		HashJoinAssembly hashJoin = new HashJoinAssembly(hashJoinEntity,
				parameters); // create a dummy component to be tested

		// create bucket for the hashJoin sub assembly
		Bucket bucket = plunger.newBucket(new Fields("col1", "col2", "col3",
				"col4", "col5"), hashJoin);

		// get results from bucket
		List<Tuple> actual = bucket.result().asTupleList();

		// assert the actual results with expected results
		assertThat(actual.size(), is(2));

		// Use HashSet so that order of fields does not matter while comparison
		Set<Tuple> output = new HashSet<Tuple>(actual);

		Set<Tuple> expectedOutput = new HashSet<Tuple>();
		expectedOutput.add(new Tuple("C1R1", "C2R1", "C3R1", "C4R1", "C5R1"));
		expectedOutput.add(new Tuple("C1R2", "C2R2", "C3R2", null, null));

		Assert.assertEquals(expectedOutput, output);
	}

	/**
	 * Test simple hashJoin operation with 2 inputs. One of the input - output
	 * map is kept empty
	 */
	@Test
	public void TestHashJoinWithEmptyInputOutputMapping() {
		Plunger plunger = new Plunger();

		Data file1 = new DataBuilder(new Fields("col1", "col2", "col3"))
				.addTuple("C1R1", "C2R1", "C3R1")
				.addTuple("C1R2", "C2R2", "C3R2").build();

		// pipe corresponding to an input of hashJoin component
		Pipe pipe1 = plunger.newNamedPipe("pipe1", file1);

		Data file2 = new DataBuilder(new Fields("col1", "col4", "col5"))
				.addTuple("C1R1", "C4R1", "C5R1")
				.addTuple("C1R2", "C4R2", "C5R2").build();

		// pipe corresponding to an input of hashJoin component
		Pipe pipe2 = plunger.newNamedPipe("pipe2", file2);

		ComponentParameters parameters = new ComponentParameters();

		parameters.addInputPipe(pipe1); // first input to hashJoin component
		parameters.addInputPipe(pipe2); // second input to hashJoin component

		parameters.addInputFields(new Fields("col1", "col2", "col3"));
		parameters.addInputFields(new Fields("col1", "col4", "col5"));

		parameters.addinSocketId("in0");
		parameters.addinSocketId("in1");

		HashJoinEntity hashJoinEntity = new HashJoinEntity();

		// set the name of the component
		hashJoinEntity.setComponentId("testHashJoin");
		
		hashJoinEntity.setMatch("first");
		
		// set key fields
		ArrayList<JoinKeyFields> keyFieldsList = new ArrayList<JoinKeyFields>();
		keyFieldsList.add(new JoinKeyFields("in0", true, new String[] { "col1" }));
		keyFieldsList.add(new JoinKeyFields("in1", true, new String[] { "col1" }));
		hashJoinEntity.setKeyFields(keyFieldsList);

		// create outSocket
		OutSocket outSocket1 = new OutSocket("out0");
		outSocket1.setSocketType("out");

		// set map fields
		List<MapField> mapFieldsList = new ArrayList<>();
		outSocket1.setMapFieldsList(mapFieldsList);

		// set pass through fields
		List<PassThroughField> passThroughFieldsList1 = new ArrayList<>();
		passThroughFieldsList1.add(new PassThroughField("*", "in0"));
		outSocket1.setPassThroughFieldsList(passThroughFieldsList1);

		// add outSocket in list
		List<OutSocket> outSocketList = new ArrayList<>();
		outSocketList.add(outSocket1);

		hashJoinEntity.setOutSocketList(outSocketList);

		// create a dummy component to be tested
		HashJoinAssembly hashJoin = new HashJoinAssembly(hashJoinEntity,
				parameters);

		// create bucket for the hashJoin sub assembly
		Bucket bucket = plunger.newBucket(new Fields("col1", "col2", "col3"),
				hashJoin);

		// get results from bucket
		List<Tuple> actual = bucket.result().asTupleList();

		// assert the actual results with expected results
		assertThat(actual.size(), is(2));

		// Use HashSet so that order of fields does not matter while comparison
		Set<Tuple> output = new HashSet<Tuple>(actual);

		Set<Tuple> expectedOutput = new HashSet<Tuple>();
		expectedOutput.add(new Tuple("C1R1", "C2R1", "C3R1"));
		expectedOutput.add(new Tuple("C1R2", "C2R2", "C3R2"));

		Assert.assertEquals(expectedOutput, output);
	}

	/**
	 * Test simple hashJoin operation with 2 inputs. One of the input has a
	 * wildcard as well as one to one mapping
	 */
	@Test
	public void TestHashJoinWithWildcardAndOneToOneMapping() {
		Plunger plunger = new Plunger();

		Data file1 = new DataBuilder(new Fields("col1", "col2", "col3"))
				.addTuple("C1R1", 21, "C3R1").addTuple("C1R2", 22, "C3R2")
				.build();

		// pipe corresponding to an input of hashJoin component
		Pipe pipe1 = plunger.newNamedPipe("pipe1", file1);

		Data file2 = new DataBuilder(new Fields("col1", "col4", "col5"))
				.addTuple("C1R1", 41, "C5R1").addTuple("C1R2", 42, "C5R2")
				.build();

		// pipe corresponding to an input of hashJoin component
		Pipe pipe2 = plunger.newNamedPipe("pipe2", file2);

		ComponentParameters parameters = new ComponentParameters();

		parameters.addInputPipe(pipe1); // first input to hashJoin component
		parameters.addInputPipe(pipe2); // second input to hashJoin component

		parameters.addInputFields(new Fields("col1", "col2", "col3"));
		parameters.addInputFields(new Fields("col1", "col4", "col5"));

		parameters.addinSocketId("in0");
		parameters.addinSocketId("in1");

		HashJoinEntity hashJoinEntity = new HashJoinEntity();

		// set the name of the component
		hashJoinEntity.setComponentId("testhashJoin");
		hashJoinEntity.setMatch("first");
		// set key fields
		ArrayList<JoinKeyFields> keyFieldsList = new ArrayList<JoinKeyFields>();
		keyFieldsList.add(new JoinKeyFields("in0", true, new String[] { "col1" }));
		keyFieldsList.add(new JoinKeyFields("in1", true, new String[] { "col1" }));
		hashJoinEntity.setKeyFields(keyFieldsList);

		// create outSocket
		OutSocket outSocket1 = new OutSocket("out0");
		outSocket1.setSocketType("out");

		// set map fields
		MapField mapOne = new MapField("col2", "RenamedColumn2", "in0");
		List<MapField> mapFieldsList = new ArrayList<>();
		mapFieldsList.add(mapOne);
		outSocket1.setMapFieldsList(mapFieldsList);

		// set pass through fields
		List<PassThroughField> passThroughFieldsList1 = new ArrayList<>();
		passThroughFieldsList1.add(new PassThroughField("*", "in0"));
		passThroughFieldsList1.add(new PassThroughField("col4", "in1"));
		passThroughFieldsList1.add(new PassThroughField("col5", "in1"));
		outSocket1.setPassThroughFieldsList(passThroughFieldsList1);

		// add outSocket in list
		List<OutSocket> outSocketList = new ArrayList<>();
		outSocketList.add(outSocket1);
		hashJoinEntity.setOutSocketList(outSocketList);

		// create a dummy component to be tested
		HashJoinAssembly hashJoin = new HashJoinAssembly(hashJoinEntity,
				parameters);

		// create bucket for the hashJoin sub assembly
		Bucket bucket = plunger.newBucket(new Fields("RenamedColumn2", "col1",
				"col3", "col4", "col5"), hashJoin);

		// get results from bucket
		List<Tuple> actual = bucket.result().asTupleList();

		// assert the actual results with expected results
		assertThat(actual.size(), is(2));

		// Use HashSet so that order of fields does not matter while comparison
		Set<Tuple> output = new HashSet<Tuple>(actual);

		Set<Tuple> expectedOutput = new HashSet<Tuple>();
		expectedOutput.add(new Tuple(21, "C1R1", "C3R1", 41, "C5R1"));
		expectedOutput.add(new Tuple(22, "C1R2", "C3R2", 42, "C5R2"));

		Assert.assertEquals(expectedOutput, output);
	}

	/*
	 * // Negative test cases start
	 *//**
	 * Test validation of hashJoin component for empty key fields for one of
	 * the input. The component should throw exception as it expects key fields
	 * for each input
	 */
	/*
	 * @Test public void TestHashJoinWithEmptyKeyFields() {
	 * 
	 * Plunger plunger = new Plunger();
	 * 
	 * Data file1 = new DataBuilder(new Fields("col1", "col2", "col3"))
	 * .addTuple("C1R1", "C2R1", "C3R1") .addTuple("C1R2", "C2R2",
	 * "C3R2").build();
	 * 
	 * // pipe corresponding to an input of hashJoin component Pipe pipe1 =
	 * plunger.newNamedPipe("pipe1", file1);
	 * 
	 * Data file2 = new DataBuilder(new Fields("col1", "col4", "col5"))
	 * .addTuple("C1R1", "C4R1", "C5R1") .addTuple("C1R3", "C4R2",
	 * "C5R2").build();
	 * 
	 * // pipe corresponding to an input of hashJoin component Pipe pipe2 =
	 * plunger.newNamedPipe("pipe2", file2);
	 * 
	 * ComponentParameters parameters = new ComponentParameters();
	 * 
	 * parameters.addInputPipe(pipe1); // first input to hashJoin component
	 * parameters.addInputPipe(pipe2); // second input to hashJoin component
	 * 
	 * parameters.addInputFields(new Fields("col1", "col2", "col3"));
	 * parameters.addInputFields(new Fields("col1", "col4", "col5"));
	 * 
	 * parameters.addOutputFields(new Fields("col1", "col2", "col3", "col4",
	 * "col5"));
	 * 
	 * parameters.setUnusedPorts(new ArrayList<Boolean>(Arrays.asList(false,
	 * false))); parameters.addinSocketId("in0");
	 * parameters.addinSocketId("in1");
	 * 
	 * HashJoinEntity hashJoinEntity = new HashJoinEntity();
	 * 
	 * // set the name of the component
	 * hashJoinEntity.setComponentID("testHashJoin");
	 * 
	 * // create outSocket OutSocket outSocket1 = new OutSocket("out0");
	 * outSocket1.setSocketType("out");
	 * 
	 * // set map fields List<MapField> mapFieldsList = new ArrayList<>();
	 * outSocket1.setMapFieldsList(mapFieldsList);
	 * 
	 * // set pass through fields List<PassThroughField> passThroughFieldsList1
	 * = new ArrayList<>(); passThroughFieldsList1.add(new
	 * PassThroughField("*", "in0")); passThroughFieldsList1.add(new
	 * PassThroughField("col4", "in1")); passThroughFieldsList1.add(new
	 * PassThroughField("col5", "in1"));
	 * outSocket1.setPassThroughFieldsList(passThroughFieldsList1);
	 * 
	 * // add outSocket in list List<OutSocket> outSocketList = new
	 * ArrayList<>(); outSocketList.add(outSocket1);
	 * hashJoinEntity.setOutSocketList(outSocketList);
	 * 
	 * try { // The component should throw an exception on validation
	 * HashJoinAssembly hashJoin = new HashJoinAssembly( hashJoinEntity,
	 * parameters); // create a dummy component to // be tested
	 * assertThat("Validation is not working", is("Validation is working")); }
	 * catch (ParametersValidationException e) { assertThat( e.getMessage(),
	 * is("'Key Fields' parameter cannot be null for component 'testHashJoin'"
	 * )); } }
	 *//**
	 * Test validation of hashJoin component for empty key fields for one of
	 * the input. The component should throw exception as it expects key fields
	 * for each input
	 */
	/*
	 * @Test public void TestHashJoinWithEmptyKeyFieldsInOneInput() {
	 * 
	 * Plunger plunger = new Plunger();
	 * 
	 * Data file1 = new DataBuilder(new Fields("col1", "col2", "col3"))
	 * .addTuple("C1R1", "C2R1", "C3R1") .addTuple("C1R2", "C2R2",
	 * "C3R2").build();
	 * 
	 * // pipe corresponding to an input of hashJoin component Pipe pipe1 =
	 * plunger.newNamedPipe("pipe1", file1);
	 * 
	 * Data file2 = new DataBuilder(new Fields("col1", "col4", "col5"))
	 * .addTuple("C1R1", "C4R1", "C5R1") .addTuple("C1R3", "C4R2",
	 * "C5R2").build();
	 * 
	 * // pipe corresponding to an input of hashJoin component Pipe pipe2 =
	 * plunger.newNamedPipe("pipe2", file2);
	 * 
	 * ComponentParameters parameters = new ComponentParameters();
	 * 
	 * parameters.addInputPipe(pipe1); // first input to hashJoin component
	 * parameters.addInputPipe(pipe2); // second input to hashJoin component
	 * 
	 * parameters.addInputFields(new Fields("col1", "col2", "col3"));
	 * parameters.addInputFields(new Fields("col1", "col4", "col5"));
	 * parameters.addinSocketId("in0"); parameters.addinSocketId("in1");
	 * parameters.addOutputFields(new Fields("col1", "col2", "col3", "col4",
	 * "col5")); parameters.setUnusedPorts(new
	 * ArrayList<Boolean>(Arrays.asList(false, false)));
	 * 
	 * HashJoinEntity hashJoinEntity = new HashJoinEntity();
	 * 
	 * // set the name of the component
	 * hashJoinEntity.setComponentID("testHashJoin");
	 * 
	 * // set key fields ArrayList<JoinKeyFields> keyFieldsList = new
	 * ArrayList<JoinKeyFields>(); keyFieldsList.add(new JoinKeyFields("in0", true, new
	 * String[] { "col1" })); hashJoinEntity.setKeyFields(keyFieldsList);
	 * 
	 * // create outSocket OutSocket outSocket1 = new OutSocket("out0");
	 * outSocket1.setSocketType("out");
	 * 
	 * // set map fields List<MapField> mapFieldsList = new ArrayList<>();
	 * outSocket1.setMapFieldsList(mapFieldsList);
	 * 
	 * // set pass through fields List<PassThroughField> passThroughFieldsList1
	 * = new ArrayList<>(); passThroughFieldsList1.add(new
	 * PassThroughField("*", "in0")); passThroughFieldsList1.add(new
	 * PassThroughField("col4", "in1")); passThroughFieldsList1.add(new
	 * PassThroughField("col5", "in1"));
	 * outSocket1.setPassThroughFieldsList(passThroughFieldsList1);
	 * 
	 * // add outSocket in list List<OutSocket> outSocketList = new
	 * ArrayList<>(); outSocketList.add(outSocket1);
	 * hashJoinEntity.setOutSocketList(outSocketList);
	 * 
	 * HashJoinAssembly hashJoin;
	 * 
	 * try { // The component should throw an exception on validation // create
	 * a dummy component to be tested hashJoin = new
	 * HashJoinAssembly(hashJoinEntity, parameters);
	 * assertThat("Validation is not working", is("Validation is working")); }
	 * catch (ParametersValidationException e) { assertThat( e.getMessage(), is(
	 * "Number of input links (2) does not match number of key field instances (1) for component 'testHashJoin'"
	 * )); } }
	 *//**
	 * Test validation of hashJoin component for empty input fields for one
	 * of the input. The component should throw exception as it expects input
	 * fields for each input
	 */
	/*
	 * @Test public void TestHashJoinWithEmptyInputFields() {
	 * 
	 * Plunger plunger = new Plunger();
	 * 
	 * Data file1 = new DataBuilder(new Fields("col1", "col2", "col3"))
	 * .addTuple("C1R1", "C2R1", "C3R1") .addTuple("C1R2", "C2R2",
	 * "C3R2").build();
	 * 
	 * // pipe corresponding to an input of hashJoin component Pipe pipe1 =
	 * plunger.newNamedPipe("pipe1", file1);
	 * 
	 * Data file2 = new DataBuilder(new Fields("col1", "col4", "col5"))
	 * .addTuple("C1R1", "C4R1", "C5R1") .addTuple("C1R3", "C4R2",
	 * "C5R2").build();
	 * 
	 * // pipe corresponding to an input ofhashJoincomponent Pipe pipe2 =
	 * plunger.newNamedPipe("pipe2", file2);
	 * 
	 * ComponentParameters parameters = new ComponentParameters();
	 * 
	 * parameters.addInputPipe(pipe1); // first input to hashJoin component
	 * parameters.addInputPipe(pipe2); // second input to hashJoin component
	 * 
	 * parameters.addInputFields(new Fields("col1", "col2", "col3"));
	 * parameters.addinSocketId("in0"); parameters.addinSocketId("in1");
	 * parameters.addOutputFields(new Fields("col1", "col2", "col3", "col4",
	 * "col5")); parameters.setUnusedPorts(new
	 * ArrayList<Boolean>(Arrays.asList(false, false)));
	 * 
	 * HashJoinEntity hashJoinEntity = new HashJoinEntity();
	 * 
	 * // set the name of the component
	 * hashJoinEntity.setComponentID("testHashJoin");
	 * 
	 * // create outSocket OutSocket outSocket1 = new OutSocket("out0");
	 * outSocket1.setSocketType("out");
	 * 
	 * // set key fields ArrayList<JoinKeyFields> keyFieldsList = new
	 * ArrayList<JoinKeyFields>(); keyFieldsList.add(new JoinKeyFields("in0", true, new
	 * String[] { "col1" })); keyFieldsList.add(new JoinKeyFields("in1", true, new
	 * String[] { "col1" })); hashJoinEntity.setKeyFields(keyFieldsList);
	 * 
	 * // set map fields List<MapField> mapFieldsList = new ArrayList<>();
	 * outSocket1.setMapFieldsList(mapFieldsList);
	 * 
	 * // set pass through fields List<PassThroughField> passThroughFieldsList1
	 * = new ArrayList<>(); passThroughFieldsList1.add(new
	 * PassThroughField("*", "in0")); passThroughFieldsList1.add(new
	 * PassThroughField("col4", "in1")); passThroughFieldsList1.add(new
	 * PassThroughField("col5", "in1"));
	 * outSocket1.setPassThroughFieldsList(passThroughFieldsList1);
	 * 
	 * // add outSocket in list List<OutSocket> outSocketList = new
	 * ArrayList<>(); outSocketList.add(outSocket1);
	 * hashJoinEntity.setOutSocketList(outSocketList);
	 * 
	 * HashJoinAssembly hashJoin;
	 * 
	 * try { // The component should throw an exception on validation // create
	 * a dummy component to be tested hashJoin = new
	 * HashJoinAssembly(hashJoinEntity, parameters);
	 * assertThat("Validation is not working", is("Validation is working")); }
	 * catch (ParametersValidationException e) { assertThat( e.getMessage(), is(
	 * "Number of input links (2) does not match number of input field instances (1) for component 'testHashJoin'"
	 * )); } }
	 *//**
	 * Test validation of hashJoin component for just one input pipe. The
	 * component should throw a validation exception as it expects two input
	 * pipes
	 */
	/*
	 * @Test public void TestHashJoinWithOneInputPipe() {
	 * 
	 * Plunger plunger = new Plunger();
	 * 
	 * Data file1 = new DataBuilder(new Fields("col1", "col2", "col3"))
	 * .addTuple("C1R1", "C2R1", "C3R1") .addTuple("C1R2", "C2R2",
	 * "C3R2").build();
	 * 
	 * // pipe corresponding to an input of hashJoin component Pipe pipe1 =
	 * plunger.newNamedPipe("pipe1", file1);
	 * 
	 * ComponentParameters parameters = new ComponentParameters();
	 * 
	 * parameters.addInputPipe(pipe1); // first input to hashJoin component
	 * 
	 * parameters.addInputFields(new Fields("col1", "col2", "col3"));
	 * parameters.addOutputFields(new Fields("col1", "col2", "col3", "col4",
	 * "col5")); parameters.addinSocketId("in0");
	 * parameters.addinSocketId("in1"); parameters.setUnusedPorts(new
	 * ArrayList<Boolean>(Arrays.asList(false, false)));
	 * 
	 * HashJoinEntity hashJoinEntity = new HashJoinEntity();
	 * 
	 * // set the name of the component
	 * hashJoinEntity.setComponentID("testHashJoin");
	 * 
	 * // create outSocket OutSocket outSocket1 = new OutSocket("out0");
	 * outSocket1.setSocketType("out");
	 * 
	 * // set key fields ArrayList<JoinKeyFields> keyFieldsList = new
	 * ArrayList<JoinKeyFields>(); keyFieldsList.add(new JoinKeyFields("in0", true, new
	 * String[] { "col1" })); keyFieldsList.add(new JoinKeyFields("in1", true, new
	 * String[] { "col1" })); hashJoinEntity.setKeyFields(keyFieldsList);
	 * 
	 * // set map fields List<MapField> mapFieldsList = new ArrayList<>();
	 * outSocket1.setMapFieldsList(mapFieldsList);
	 * 
	 * // set pass through fields List<PassThroughField> passThroughFieldsList1
	 * = new ArrayList<>(); passThroughFieldsList1.add(new
	 * PassThroughField("*", "in0")); passThroughFieldsList1.add(new
	 * PassThroughField("col4", "in1")); passThroughFieldsList1.add(new
	 * PassThroughField("col5", "in1"));
	 * outSocket1.setPassThroughFieldsList(passThroughFieldsList1);
	 * 
	 * // add outSocket in list List<OutSocket> outSocketList = new
	 * ArrayList<>(); outSocketList.add(outSocket1);
	 * hashJoinEntity.setOutSocketList(outSocketList);
	 * 
	 * HashJoinAssembly hashJoin;
	 * 
	 * try { // The component should throw an exception on validation // create
	 * a dummy component to be tested hashJoin = new
	 * HashJoinAssembly(hashJoinEntity, parameters);
	 * assertThat("Validation is not working", is("Validation is working")); }
	 * catch (ParametersValidationException e) { assertThat( e.getMessage(), is(
	 * "Atleast two input links should be provided in input pipes parameter for component 'testHashJoin'"
	 * )); } }
	 * 
	 * // Negative test cases end
	 */
	@After
	public void cleanup() {
		// TODO: Add cleanup related code here
	}
}
