package hydrograph.engine.adapters;

import hydrograph.engine.adapters.base.CommandAdapterBase;
import hydrograph.engine.commandtype.component.BaseCommandComponent;
import hydrograph.engine.commandtype.component.RunProgram;
import hydrograph.engine.core.component.generator.RunProgramEntityGenerator;
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent;

public class RunProgramAdapter extends CommandAdapterBase {

	
	private static final long serialVersionUID = 4972095665271276000L;
	BaseCommandComponent runProgram;
	RunProgramEntityGenerator entityGenerator;

	public RunProgramAdapter(TypeBaseComponent typeCommandComponent) {

		entityGenerator = new RunProgramEntityGenerator(typeCommandComponent);

	}

	@Override
	public BaseCommandComponent getComponent() {

		try {
			runProgram = new RunProgram(entityGenerator.getEntity());
		} catch (Throwable e) {
			throw new RuntimeException("Assembly Creation Error for RunProgram Component : " + e.getMessage());

		}
		return runProgram;
	}

}
