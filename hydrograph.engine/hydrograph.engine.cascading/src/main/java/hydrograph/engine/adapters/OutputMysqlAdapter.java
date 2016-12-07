package hydrograph.engine.adapters;

import hydrograph.engine.adapters.base.OutputAdapterBase;
import hydrograph.engine.cascading.assembly.OutputMysqlAssembly;
import hydrograph.engine.cascading.assembly.base.BaseComponent;
import hydrograph.engine.cascading.assembly.infra.ComponentParameters;
import hydrograph.engine.core.component.generator.OutputMysqlEntityGenerator;
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent;

public class OutputMysqlAdapter extends OutputAdapterBase{

	OutputMysqlAssembly mysqlComponent;
	OutputMysqlEntityGenerator entityGenerator;
	
	public OutputMysqlAdapter(TypeBaseComponent component) {
		entityGenerator=new OutputMysqlEntityGenerator(component);
		
	}
	
	
	@Override
	public BaseComponent getAssembly() {
		return mysqlComponent;
	}

	@Override
	public void createAssembly(ComponentParameters componentParameters) {
		mysqlComponent=new OutputMysqlAssembly(entityGenerator.getEntity(), componentParameters);
	}

}
