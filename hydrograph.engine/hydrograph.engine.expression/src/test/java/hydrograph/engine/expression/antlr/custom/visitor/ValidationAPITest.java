package hydrograph.engine.expression.antlr.custom.visitor;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class ValidationAPITest {

	@Test(expected = DescriptiveErrorListener.HydrographExpressionError.class)
	public void itShouldThrowException() {
		ValidationAPI.isExpressionValid("if(1==1) 12; lse 20;");
	}

	@Test
	public void itShouldImportDefauldPackage() {
		Assert.assertTrue(ValidationAPI.getValidExpression("if(1==1) 12; else 20;").contains("import"));
	}

	@Test
	public void itShouldCompileAndMatchTheString() {
		Map<String, Class<?>> schemaFields = new HashMap<String, Class<?>>();
		schemaFields.put("f1", String.class);
		Assert.assertEquals(ValidationAPI.compile(
				"StringFunctions.match(\"HELLO WORLD\",StringFunctions.stringUpper(f1))?1:0;", schemaFields), 1);
	}

//	@Test
//	public void itShouldCompileAndMatchTheDate() {
//		Map<String, Class<?>> schemaFields = new HashMap<String, Class<?>>();
//		schemaFields.put("f1", Date.class);
//		Assert.assertEquals(ValidationAPI.compile(
//				"StringFunctions.match(\"HELLO WORLD\",DateFunctions.getStringDateFromDateObject(f1, \"MM-dd-yyyy\"))1:0;",
//				schemaFields), 1);
//
//	}

}
