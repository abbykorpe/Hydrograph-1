package hydrograph.engine.expression.antlr.custom.visitor;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;

import org.junit.Assert;
import org.junit.Test;

public class ValidationAPITest {

	@Test(expected = DescriptiveErrorListener.HydrographExpressionError.class)
	public void itShouldThrowException() {
		ValidationAPI.isExpressionValid("(1==1)?12:20");
	}

	@Test
	public void itShouldImportDefauldPackage() {
		Assert.assertTrue(ValidationAPI.getValidExpression("(1==1)?12:20;").contains("import"));
	}

	@Test
	public void itShouldCompileAndMatchTheString() {
		Map<String, Class<?>> schemaFields = new HashMap<String, Class<?>>();
		schemaFields.put("f1", Date.class);
		DiagnosticCollector<JavaFileObject> dig = ValidationAPI.compile(
				"(1==1) ?12: 20;",
				schemaFields);

		Assert.assertTrue(dig.getDiagnostics().size() <= 0);
	}

}
