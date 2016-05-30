package hydrograph.engine.test;

import java.util.ArrayList;
import java.util.Properties;

import hydrograph.engine.transformation.userfunctions.base.ReusableRow;
import hydrograph.engine.transformation.userfunctions.base.TransformBase;

public class TransformTest implements TransformBase {

	@Override
	public void prepare(Properties props, ArrayList<String> inputFields, ArrayList<String> outputFields) {
		// TODO Auto-generated method stub

	}

	@Override
	public void transform(ReusableRow inputRow, ReusableRow outputRow) {
		outputRow.setField("new_name", inputRow.getField("name"));
	}

	@Override
	public void cleanup() {
		// TODO Auto-generated method stub

	}

}
