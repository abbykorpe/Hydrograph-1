package hydrograph.engine.adapters;

import hydrograph.engine.adapters.base.InputAdapterBase;
import hydrograph.engine.cascading.assembly.InputRedshiftAssembly;
import hydrograph.engine.cascading.assembly.base.BaseComponent;
import hydrograph.engine.cascading.assembly.infra.ComponentParameters;
import hydrograph.engine.core.component.generator.InputRedshiftEntityGenerator;
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent;

public class InputRedShiftAdapter extends InputAdapterBase{

	InputRedshiftAssembly redShiftComponent;
	InputRedshiftEntityGenerator entityGenerator;
	
	public InputRedShiftAdapter(TypeBaseComponent baseComponent) {
		entityGenerator=new InputRedshiftEntityGenerator(baseComponent);
	}
	
	@Override
	public BaseComponent getAssembly() {
		return redShiftComponent;
	}

	@Override
	public void createAssembly(ComponentParameters componentParameters) {
		redShiftComponent=new InputRedshiftAssembly(entityGenerator.getEntity(), componentParameters);
		
	}

}
