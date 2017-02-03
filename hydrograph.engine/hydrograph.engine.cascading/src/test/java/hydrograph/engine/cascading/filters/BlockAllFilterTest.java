/*******************************************************************************
 * Copyright 2017 Capital One Services, LLC and Bitwise, Inc.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 *******************************************************************************/
package hydrograph.engine.cascading.filters;

import cascading.operation.Filter;
import cascading.pipe.Each;
import cascading.pipe.Pipe;
import cascading.tuple.Fields;
import cascading.tuple.Tuple;
import com.hotels.plunger.Bucket;
import com.hotels.plunger.Data;
import com.hotels.plunger.DataBuilder;
import com.hotels.plunger.Plunger;
import hydrograph.engine.cascading.filters.BlockAllFilter;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class BlockAllFilterTest {

	@Test
	public void itShouldRemoveEveryRecord() {
		Filter filter = new BlockAllFilter();
		Plunger plunger = new Plunger();
		Data data = new DataBuilder(new Fields("id", "name", "surname", "city", "phone"))
				.addTuple(1, "abc", "xyz", "kyn", "98").addTuple(1, "pqr", "stu", "mum", "82").build();
		Pipe words = plunger.newNamedPipe("records", data);
		Pipe pipesToTest = new Each(words, filter);
		Bucket bucket = plunger.newBucket(new Fields("id", "name", "surname", "city", "phone"), pipesToTest);
		List<Tuple> actual = bucket.result().asTupleList();
		assertTrue(actual.isEmpty());
	}

}
