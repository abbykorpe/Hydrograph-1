package hydrograph.engine.adapters;

import hydrograph.engine.adapters.base.InputAdapterBase;
import hydrograph.engine.cascading.assembly.InputMysqlAssembly;
import hydrograph.engine.cascading.assembly.base.BaseComponent;
import hydrograph.engine.cascading.assembly.infra.ComponentParameters;
import hydrograph.engine.core.component.generator.InputMysqlEntityGenerator;
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent;

public class InputMysqlAdapter extends InputAdapterBase{

	InputMysqlAssembly mysqlComponent;
	InputMysqlEntityGenerator entityGenerator;
	
	public InputMysqlAdapter(TypeBaseComponent component) {
		entityGenerator=new InputMysqlEntityGenerator(component);
		
	}
	
	
	@Override
	public BaseComponent getAssembly() {
		return mysqlComponent;
	}

	@Override
	public void createAssembly(ComponentParameters componentParameters) {
		mysqlComponent=new InputMysqlAssembly(entityGenerator.getEntity(), componentParameters);
	}

}
