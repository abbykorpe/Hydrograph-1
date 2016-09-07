package hydrograph.engine.expression.api;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;

import org.junit.Assert;
import org.junit.Test;

import hydrograph.engine.expression.api.DescriptiveErrorListener;
import hydrograph.engine.expression.api.ValidationAPI;

public class ValidationAPITest {

	@Test(expected = DescriptiveErrorListener.HydrographExpressionError.class)
	public void itShouldThrowException() {
		ValidationAPI validationAPI = new ValidationAPI("(1==1)?12:20", "");
		validationAPI.isExpressionValid();
	}

	@Test
	public void itShouldValidateTheExpression() {
		ValidationAPI validationAPI = new ValidationAPI("StringFunctions.stringMatch(\"AAA\",\"a\")?1:2;", "");
		Assert.assertTrue(validationAPI.isExpressionValid());
	}

	@Test
	public void itShouldImportDefauldPackage() {
		ValidationAPI validationAPI = new ValidationAPI("(1==1)?12:20;", "");
		Assert.assertTrue(validationAPI.getValidExpression().contains("import"));
	}

	@Test
	public void itShouldCompileFilterExpression() {
		ValidationAPI validationAPI = new ValidationAPI(
				"StringFunctions.stringMatch(\"HELLO WORLD\",DateFunctions.getStringDateFromDateObject(f1, \"\"))?true:false;",
				"");
		Map<String, Class<?>> schemaFields = new HashMap<String, Class<?>>();
		schemaFields.put("f1", Date.class);
		DiagnosticCollector<JavaFileObject> dig = validationAPI.filterCompiler(schemaFields);

		Assert.assertTrue(dig.getDiagnostics().size() <= 0);
	}
	
	@Test
	public void itShouldCompileTransformExpression() {
		ValidationAPI validationAPI = new ValidationAPI(
				"StringFunctions.stringMatch(\"HELLO WORLD\",DateFunctions.getStringDateFromDateObject(f1, \"\"))?f1:\"HELLO WORLD\";",
				"");
		Map<String, Class<?>> schemaFields = new HashMap<String, Class<?>>();
		schemaFields.put("f1", Date.class);
		DiagnosticCollector<JavaFileObject> dig = validationAPI.transformCompiler(schemaFields);

		Assert.assertTrue(dig.getDiagnostics().size() <= 0);
	}

	@Test
	public void itShouldMatchFieldName() {
		ValidationAPI validationAPI = new ValidationAPI(
				"StringFunctions.stringMatch(f2,DateFunctions.getStringDateFromDateObject(f1, \"\"))?1:0;", "");
		Map<String, Class<?>> schemaFields = new HashMap<String, Class<?>>();
		schemaFields.put("f1", Date.class);
		schemaFields.put("f2", String.class);
		schemaFields.put("f3", Date.class);

		List<String> fieldList = validationAPI.getFieldNameList(schemaFields);
		Assert.assertEquals("f1", fieldList.get(0));
	}

	@Test
	public void itShouldExcuteTheExpression() {
		ValidationAPI validationAPI = new ValidationAPI("StringFunctions.stringMatch(\"test\",\"test\")?1:0;", "");
		Assert.assertEquals(1, validationAPI.execute());
	}
}