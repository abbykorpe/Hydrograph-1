package hydrograph.engine.expression.antlr.custom.visitor;

import org.junit.Assert;
import org.junit.Test;

import hydrograph.engine.expression.antlr.DescriptiveErrorListener;

public class ValidationAPITest {

	@Test(expected = DescriptiveErrorListener.HydrographExpressionError.class)
	public void itShouldThrowException() {
		ValidationAPI.isExpressionValid("if(1==1) 12; lse 20;");
	}

	@Test
	public void itShouldNotThrowException() {
		ValidationAPI.isExpressionValid("if(1==1) 12; else 20;");
	}

	@Test
	public void itShouldImportDefauldPackage() {
		Assert.assertTrue(ValidationAPI.getValidExpression("if(1==1) 12; else 20;").contains("import"));
	}

}
