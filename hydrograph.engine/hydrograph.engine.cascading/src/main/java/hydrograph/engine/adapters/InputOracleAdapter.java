package hydrograph.engine.adapters;

import hydrograph.engine.adapters.base.InputAdapterBase;
import hydrograph.engine.cascading.assembly.InputOracleAssembly;
import hydrograph.engine.cascading.assembly.base.BaseComponent;
import hydrograph.engine.cascading.assembly.infra.ComponentParameters;
import hydrograph.engine.core.component.generator.InputOracleEntityGenerator;
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent;

public class InputOracleAdapter extends InputAdapterBase {

	InputOracleAssembly oracleComponent;
	InputOracleEntityGenerator entityGenerator;

	public InputOracleAdapter(TypeBaseComponent baseComponent) {
		entityGenerator = new InputOracleEntityGenerator(baseComponent);
	}

	@Override
	public BaseComponent getAssembly() {
		return oracleComponent;
	}

	@Override
	public void createAssembly(ComponentParameters componentParameters) {
		oracleComponent = new InputOracleAssembly(entityGenerator.getEntity(), componentParameters);

	}

}
