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
import hydrograph.engine.assembly.entity.UnionAllEntity;
import hydrograph.engine.assembly.entity.elements.OutSocket;
import hydrograph.engine.cascading.assembly.UnionAllAssembly;
import hydrograph.engine.cascading.assembly.infra.ComponentParameters;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
 * Test unionAll sub assembly. The tests are written using plunger framework
 * 
 * @author Prabodh
 */
public class UnionAllAssemblyTest {

	@Before
	public void setup() {
		// TODO: Add setup related code here

	}

	/**
	 * Test the unionAll component working
	 */
	@Test
	public void TestSimpleUnionAllComponentWorking() {
		Plunger plunger = new Plunger();

		Data file1 = new DataBuilder(new Fields("col1", "col2", "col3")).addTuple("C1R1", "C2R1", "C3R1")
				.addTuple("C1R2", "C2R2", "C3R2").build();
		Pipe pipe1 = plunger.newNamedPipe("pipe1", file1); // pipe corresponding
															// to an input of
															// unionAll
															// component

		Data file2 = new DataBuilder(new Fields("col1", "col2", "col3")).addTuple("C1R3", "C2R3", "C3R3")
				.addTuple("C1R4", "C2R4", "C3R4").build();
		Pipe pipe2 = plunger.newNamedPipe("pipe2", file2); // pipe corresponding
															// to an input of
															// unionAll
															// component
		UnionAllEntity unionAllEntity = new UnionAllEntity();
		unionAllEntity.setComponentId("unionAll");
		unionAllEntity.setOutSocket(new OutSocket("out1"));

		ComponentParameters parameters = new ComponentParameters();

		parameters.addInputPipe(pipe1); // first input to unionAll component
		parameters.addInputPipe(pipe2); // second input to unionAll component
		parameters.addInputFields(new Fields("col1", "col2", "col3")); // list
		parameters.addInputFields(new Fields("col1", "col2", "col3")); // fields
																		// on
																		// input
																		// of
																		// unionAll
																		// component
		parameters.addOutputFields(new Fields("col1", "col2", "col3")); // list
																		// of
																		// fields
																		// on
																		// output
																		// of
																		// unionAll
																		// component

		// parameters.s("testunionAll"); //set the name of the component

		UnionAllAssembly unionAll = new UnionAllAssembly(unionAllEntity, parameters); // create
																						// a
																						// dummy
																						// component
																						// to
																						// be
																						// tested

		Bucket bucket = plunger.newBucket(new Fields("col1", "col2", "col3"), unionAll); // create
																							// bucket
																							// for
																							// the
																							// unionAll
																							// sub
																							// assembly

		List<Tuple> actual = bucket.result().asTupleList(); // get results from
															// bucket

		// assert the actual results with expected results
		assertThat(actual.size(), is(4));
		// assertThat(actual.get(0), is(new Tuple("C1R1", "C2R1", "C3R1")));

		// Use HashSet so that order of fields does not matter while comparison
		Set<Tuple> output = new HashSet<Tuple>(actual);

		Set<Tuple> expectedOutput = new HashSet<Tuple>();
		expectedOutput.add(new Tuple("C1R1", "C2R1", "C3R1"));
		expectedOutput.add(new Tuple("C1R2", "C2R2", "C3R2"));
		expectedOutput.add(new Tuple("C1R3", "C2R3", "C3R3"));
		expectedOutput.add(new Tuple("C1R4", "C2R4", "C3R4"));

		Assert.assertEquals(expectedOutput, output);
	}

	/**
	 * Test unionAll component working with four inputs
	 */
	@Test
	public void TestUnionAllComponentWithFourInputs() {
		Plunger plunger = new Plunger();

		Data file1 = new DataBuilder(new Fields("col1", "col2", "col3")).addTuple("C1R1", "C2R1", "C3R1")
				.addTuple("C1R2", "C2R2", "C3R2").build();
		Pipe pipe1 = plunger.newNamedPipe("pipe1", file1); // pipe corresponding
															// to an input of
															// unionAll
															// component

		Data file2 = new DataBuilder(new Fields("col1", "col2", "col3")).addTuple("C1R3", "C2R3", "C3R3")
				.addTuple("C1R4", "C2R4", "C3R4").build();
		Pipe pipe2 = plunger.newNamedPipe("pipe2", file2); // pipe corresponding
															// to an input of
															// unionAll
															// component

		Data file3 = new DataBuilder(new Fields("col1", "col2", "col3")).addTuple("C1R5", "C2R5", "C3R5")
				.addTuple("C1R6", "C2R6", "C3R6").build();
		Pipe pipe3 = plunger.newNamedPipe("pipe3", file3); // pipe corresponding
															// to an input of
															// unionAll
															// component

		Data file4 = new DataBuilder(new Fields("col1", "col2", "col3")).addTuple("C1R7", "C2R7", "C3R7")
				.addTuple("C1R8", "C2R8", "C3R8").build();
		Pipe pipe4 = plunger.newNamedPipe("pipe4", file4); // pipe corresponding
															// to an input of
															// unionAll
															// component

		// Map<String, String> test = new HashMap<String, String>();
		// test.put("col1", "out");
		UnionAllEntity unionAllEntity = new UnionAllEntity();
		unionAllEntity.setComponentId("1");
		unionAllEntity.setOutSocket(new OutSocket("abcc"));
		ComponentParameters parameters = new ComponentParameters();

		parameters.addInputPipe(pipe1); // first input to unionAll component
		parameters.addInputPipe(pipe2); // second input to unionAll component
		parameters.addInputPipe(pipe3); // third input to unionAll component
		parameters.addInputPipe(pipe4); // fourth input to unionAll component
		parameters.addInputFields(new Fields("col1", "col2", "col3")); // list
		parameters.addInputFields(new Fields("col1", "col2", "col3")); // of
		parameters.addInputFields(new Fields("col1", "col2", "col3")); // fields
		parameters.addInputFields(new Fields("col1", "col2", "col3")); // on
																		// input
																		// of
																		// unionAll
																		// component
		parameters.addOutputFields(new Fields("col1", "col2", "col3")); // list
																		// of
																		// fields
																		// on
																		// output
																		// of
																		// unionAll
																		// component

		// parameters.setComponentName("testunionAll"); //set the name of the
		// component

		UnionAllAssembly unionAllAssembly = new UnionAllAssembly(unionAllEntity, parameters); // create
																								// a
																								// dummy
																								// component
																								// to
																								// be
																								// tested

		Bucket bucket = plunger.newBucket(new Fields("col1", "col2", "col3"), unionAllAssembly); // create
																									// bucket
																									// for
																									// the
																									// unionAll
																									// sub
																									// assembly

		List<Tuple> actual = bucket.result().asTupleList(); // get results from
															// bucket

		// assert the actual results with expected results
		assertThat(actual.size(), is(8));
		// assertThat(actual.get(0), is(new Tuple("C1R1", "C2R1", "C3R1")));

		// Use HashSet so that order of fields does not matter while comparison
		Set<Tuple> output = new HashSet<Tuple>(actual);

		Set<Tuple> expectedOutput = new HashSet<Tuple>();
		expectedOutput.add(new Tuple("C1R1", "C2R1", "C3R1"));
		expectedOutput.add(new Tuple("C1R2", "C2R2", "C3R2"));
		expectedOutput.add(new Tuple("C1R3", "C2R3", "C3R3"));
		expectedOutput.add(new Tuple("C1R4", "C2R4", "C3R4"));
		expectedOutput.add(new Tuple("C1R5", "C2R5", "C3R5"));
		expectedOutput.add(new Tuple("C1R6", "C2R6", "C3R6"));
		expectedOutput.add(new Tuple("C1R7", "C2R7", "C3R7"));
		expectedOutput.add(new Tuple("C1R8", "C2R8", "C3R8"));

		Assert.assertEquals(expectedOutput, output);
	}

	/**
	 * Test unionAll component with different order of fields in input. The
	 * unionAll component should re-align the fields on all the inputs to match
	 * the first input. The first input is on port 0
	 */
	@Test
	public void TestReAligningInputFieldsInUnionAllComponent() {
		Plunger plunger = new Plunger();

		Data file1 = new DataBuilder(new Fields("col1", "col3", "col2")).addTuple("C1R1", "C3R1", "C2R1")
				.addTuple("C1R2", "C3R2", "C2R2").build();
		Pipe pipe1 = plunger.newNamedPipe("pipe1", file1); // pipe corresponding
															// to an input of
															// unionAll
															// component

		Data file2 = new DataBuilder(new Fields("col1", "col2", "col3")).addTuple("C1R3", "C2R3", "C3R3")
				.addTuple("C1R4", "C2R4", "C3R4").build();
		Pipe pipe2 = plunger.newNamedPipe("pipe2", file2); // pipe corresponding
															// to an input of
															// unionAll
															// component

		UnionAllEntity unionAllEntity = new UnionAllEntity();
		unionAllEntity.setComponentId("unionAll");
		unionAllEntity.setOutSocket(new OutSocket("out1"));
		ComponentParameters parameters = new ComponentParameters();

		parameters.addInputPipe(pipe1); // first input to unionAll component
		parameters.addInputPipe(pipe2); // second input to unionAll component

		parameters.addInputFields(new Fields("col1", "col2", "col3")); // list
		parameters.addInputFields(new Fields("col1", "col2", "col3")); // of
																		// fields
																		// on
																		// input
																		// of
																		// unionAll
																		// component

		parameters.addOutputFields(new Fields("col1", "col2", "col3")); // list
																		// of
																		// fields
																		// on
																		// output
																		// of
																		// unionAll
																		// component

		// parameters.setComponentName("testunionAll"); //set the name of the
		// component

		UnionAllAssembly unionAllAssembly = new UnionAllAssembly(unionAllEntity, parameters); // create
																								// a
																								// dummy
																								// component
																								// to
																								// be
																								// tested

		Bucket bucket = plunger.newBucket(new Fields("col1", "col2", "col3"), unionAllAssembly); // create
																									// bucket
																									// for
																									// the
																									// unionAll
																									// sub
																									// assembly

		List<Tuple> actual = bucket.result().asTupleList(); // get results from
															// bucket

		// assert the actual results with expected results
		assertThat(actual.size(), is(4));
	}
}
