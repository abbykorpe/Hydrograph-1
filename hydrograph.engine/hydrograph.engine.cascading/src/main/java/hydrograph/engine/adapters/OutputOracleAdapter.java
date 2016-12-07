package hydrograph.engine.adapters;

import hydrograph.engine.adapters.base.OutputAdapterBase;
import hydrograph.engine.cascading.assembly.OutputOracleAssembly;
import hydrograph.engine.cascading.assembly.base.BaseComponent;
import hydrograph.engine.cascading.assembly.infra.ComponentParameters;
import hydrograph.engine.core.component.generator.OutputOracleEntityGenerator;
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent;

public class OutputOracleAdapter extends OutputAdapterBase {

	OutputOracleAssembly oracleComponent;
	OutputOracleEntityGenerator entityGenerator;

	public OutputOracleAdapter(TypeBaseComponent baseComponent) {
		entityGenerator = new OutputOracleEntityGenerator(baseComponent);
	}

	@Override
	public BaseComponent getAssembly() {
		return oracleComponent;
	}

	@Override
	public void createAssembly(ComponentParameters componentParameters) {
		oracleComponent = new OutputOracleAssembly(entityGenerator.getEntity(), componentParameters);

	}

}
