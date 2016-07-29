/*******************************************************************************
 *  Copyright 2016 Capital One Services, LLC and Bitwise, Inc.
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *  http://www.apache.org/licenses/LICENSE2.0
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *******************************************************************************/
package hydrograph.server.sqlquery.lingualquery.generator;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import hydrograph.server.debug.lingual.json.GridRow;
import hydrograph.server.debug.lingual.json.JobDetails;
import hydrograph.server.debug.lingual.json.RemoteFilterJson;
import hydrograph.server.sqlquery.parser.antlr.QueryParserLexer;
import hydrograph.server.sqlquery.parser.antlr.QueryParserParser;

/**
 * Created by bhaveshs on 6/22/2016.
 */
public class LingualQueryCreatorTest {

	RemoteFilterJson remoteFilterJson;

	@Before
	public void init() {
		remoteFilterJson = new RemoteFilterJson();

		List<GridRow> gridList = new ArrayList<GridRow>();
		GridRow gridRow = new GridRow();
		gridRow.setDataType(Integer.valueOf(1));
		gridRow.setDataTypeValue("String");
		gridRow.setDateFormat("dateFormat");
		gridRow.setDescription("description");
		gridRow.setFieldName("f1");
		gridRow.setPrecision("precision");
		gridRow.setScale("scale");
		gridRow.setScaleType(Integer.valueOf(1));
		gridRow.setScaleTypeValue("scaleTypeValue");
		gridList.add(gridRow);

		GridRow gridRow1 = new GridRow();
		gridRow1.setDataType(Integer.valueOf(1));
		gridRow1.setDataTypeValue("Date");
		gridRow1.setDateFormat("dateFormat");
		gridRow1.setDescription("description");
		gridRow1.setFieldName("f2");
		gridRow1.setPrecision("precision");
		gridRow1.setScale("scale");
		gridRow1.setScaleType(Integer.valueOf(1));
		gridRow1.setScaleTypeValue("scaleTypeValue");
		gridList.add(gridRow1);

		GridRow gridRow2 = new GridRow();
		gridRow2.setDataType(Integer.valueOf(1));
		gridRow2.setDataTypeValue("float");
		gridRow2.setDateFormat("dateFormat");
		gridRow2.setDescription("description");
		gridRow2.setFieldName("f3");
		gridRow2.setPrecision("precision");
		gridRow2.setScale("scale");
		gridRow2.setScaleType(Integer.valueOf(1));
		gridRow2.setScaleTypeValue("scaleTypeValue");
		gridList.add(gridRow2);

		GridRow gridRow3 = new GridRow();
		gridRow3.setDataType(Integer.valueOf(1));
		gridRow3.setDataTypeValue("Double");
		gridRow3.setDateFormat("dateFormat");
		gridRow3.setDescription("description");
		gridRow3.setFieldName("f4");
		gridRow3.setPrecision("precision");
		gridRow3.setScale("scale");
		gridRow3.setScaleType(Integer.valueOf(1));
		gridRow3.setScaleTypeValue("scaleTypeValue");
		gridList.add(gridRow3);

		GridRow gridRow4 = new GridRow();
		gridRow4.setDataType(Integer.valueOf(1));
		gridRow4.setDataTypeValue("Boolean");
		gridRow4.setDateFormat("dateFormat");
		gridRow4.setDescription("description");
		gridRow4.setFieldName("f5");
		gridRow4.setPrecision("precision");
		gridRow4.setScale("scale");
		gridRow4.setScaleType(Integer.valueOf(1));
		gridRow4.setScaleTypeValue("scaleTypeValue");
		gridList.add(gridRow4);
		remoteFilterJson.setSchema(gridList);

		remoteFilterJson.setFileSize(10);
		remoteFilterJson.setJobDetails(new JobDetails("host", "port", "username", "password", "basepath", "uniqueJobID",
				"componentID", "componentSocketID", true));
	}

	@Test
	public void itShouldGenerateLingualQuery() {

		remoteFilterJson.setCondition("(f1  not in ('1') and f2 = '2012-01-10') or (f3=23.23 and f4=1234.123)");

		ANTLRInputStream stream = new ANTLRInputStream(remoteFilterJson.getCondition());
		QueryParserLexer lexer = new QueryParserLexer(stream);
		CommonTokenStream tokenStream = new CommonTokenStream(lexer);
		QueryParserParser parser = new QueryParserParser(tokenStream);
		parser.removeErrorListeners();
		LingualQueryCreator customVisitor = new LingualQueryCreator(remoteFilterJson.getSchema());
		String lingualExpression = customVisitor.visit(parser.eval());
		Assert.assertEquals(
				"((\"f1\" is not null and \"f1\" not in('1'))  and  \"f2\" = date '2012-01-10')  or  (\"f3\" = cast(23.23 as float)  and  \"f4\" = cast(1234.123 as double))",
				lingualExpression);
	}

	@Test
	public void itShouldGenerateLingualQueryWithNotLikeClause() {

		remoteFilterJson.setCondition("(f1 not like 'condition' and f2 = '2012-01-10') or (f3=23.23 and f4=1234.123)");

		ANTLRInputStream stream = new ANTLRInputStream(remoteFilterJson.getCondition());
		QueryParserLexer lexer = new QueryParserLexer(stream);
		CommonTokenStream tokenStream = new CommonTokenStream(lexer);
		QueryParserParser parser = new QueryParserParser(tokenStream);
		parser.removeErrorListeners();
		LingualQueryCreator customVisitor = new LingualQueryCreator(remoteFilterJson.getSchema());
		String lingualExpression = customVisitor.visit(parser.eval());
		Assert.assertEquals(
				"((\"f1\" is not null and \"f1\" not like 'condition')  and  \"f2\" = date '2012-01-10')  or  (\"f3\" = cast(23.23 as float)  and  \"f4\" = cast(1234.123 as double))",
				lingualExpression);
	}

	@Test
	public void itShouldGenerateLingualQueryWithLikeClause() {

		remoteFilterJson.setCondition("f1 like '%island@.com%' AND f1 LIKE '%'");

		ANTLRInputStream stream = new ANTLRInputStream(remoteFilterJson.getCondition());
		QueryParserLexer lexer = new QueryParserLexer(stream);
		CommonTokenStream tokenStream = new CommonTokenStream(lexer);
		QueryParserParser parser = new QueryParserParser(tokenStream);
		parser.removeErrorListeners();
		LingualQueryCreator customVisitor = new LingualQueryCreator(remoteFilterJson.getSchema());
		String lingualExpression = customVisitor.visit(parser.eval());
		Assert.assertEquals("\"f1\" like '%island@.com%'  AND  \"f1\" LIKE '%'", lingualExpression);
	}

	@Test
	public void itShouldGenerateLingualQueryWithLikeClauseAndBooleanCondition() {

		remoteFilterJson.setCondition("f1 like '%abc dfsf%' AND f1 like '%' AND f5 = 'true'");

		ANTLRInputStream stream = new ANTLRInputStream(remoteFilterJson.getCondition());
		QueryParserLexer lexer = new QueryParserLexer(stream);
		CommonTokenStream tokenStream = new CommonTokenStream(lexer);
		QueryParserParser parser = new QueryParserParser(tokenStream);
		parser.removeErrorListeners();
		LingualQueryCreator customVisitor = new LingualQueryCreator(remoteFilterJson.getSchema());
		String lingualExpression = customVisitor.visit(parser.eval());
		Assert.assertEquals("\"f1\" like '%abc dfsf%'  AND  \"f1\" like '%'  AND  \"f5\" = true", lingualExpression);
	}

	@Test
	public void itShouldGenerateLingualQueryWithBetweenClause() {

		remoteFilterJson.setCondition("f3 between 10 and 20 and f3 BETWEEN 10 AND 20");

		ANTLRInputStream stream = new ANTLRInputStream(remoteFilterJson.getCondition());
		QueryParserLexer lexer = new QueryParserLexer(stream);
		CommonTokenStream tokenStream = new CommonTokenStream(lexer);
		QueryParserParser parser = new QueryParserParser(tokenStream);
		parser.removeErrorListeners();
		LingualQueryCreator customVisitor = new LingualQueryCreator(remoteFilterJson.getSchema());
		String lingualExpression = customVisitor.visit(parser.eval());
		Assert.assertEquals(
				"\"f3\" between  cast(10 as float)  and  cast(20 as float)  and  \"f3\" BETWEEN  cast(10 as float)  AND  cast(20 as float)",
				lingualExpression);
	}

	@Test
	public void itShouldGenerateLingualQueryWithMultipleCondition() {

		remoteFilterJson.setCondition("f1 = 'or maybe' AND f1 <> 'or mat' and f3 between 10 and 20");

		ANTLRInputStream stream = new ANTLRInputStream(remoteFilterJson.getCondition());
		QueryParserLexer lexer = new QueryParserLexer(stream);
		CommonTokenStream tokenStream = new CommonTokenStream(lexer);
		QueryParserParser parser = new QueryParserParser(tokenStream);
		parser.removeErrorListeners();
		LingualQueryCreator customVisitor = new LingualQueryCreator(remoteFilterJson.getSchema());
		String lingualExpression = customVisitor.visit(parser.eval());
		Assert.assertEquals(
				"\"f1\" = 'or maybe'  AND  \"f1\" <> 'or mat'  and  \"f3\" between  cast(10 as float)  and  cast(20 as float)",
				lingualExpression);
	}

	@Test
	public void itShouldGenerateLingualQueryWithFieldComparator() {

		remoteFilterJson.setCondition("f1 = f2 AND f1 <> f2 and f3 > f4");

		ANTLRInputStream stream = new ANTLRInputStream(remoteFilterJson.getCondition());
		QueryParserLexer lexer = new QueryParserLexer(stream);
		CommonTokenStream tokenStream = new CommonTokenStream(lexer);
		QueryParserParser parser = new QueryParserParser(tokenStream);
		parser.removeErrorListeners();
		LingualQueryCreator customVisitor = new LingualQueryCreator(remoteFilterJson.getSchema());
		String lingualExpression = customVisitor.visit(parser.eval());
		Assert.assertEquals("\"f1\" = \"f2\"  AND  \"f1\" <> \"f2\"  and  \"f3\" > \"f4\"", lingualExpression);
	}

}
