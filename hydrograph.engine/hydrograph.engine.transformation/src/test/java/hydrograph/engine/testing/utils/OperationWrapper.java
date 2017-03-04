package hydrograph.engine.testing.utils;

import java.util.List;

import hydrograph.engine.transformation.userfunctions.base.TransformBase;

public class OperationWrapper {

	
	public static List<TestReusableRow>  callTransform(TransformBase transformBase,ReusableRowWrapper rowWrapper){
		for(int i=0;i<rowWrapper.getInputReusableRowList().size();i++){
			transformBase.transform(rowWrapper.getInputReusableRowList().get(i),rowWrapper.getOutputReusableRowList().get(i));
		}
		return rowWrapper.getOutputReusableRowList();
	}
}
