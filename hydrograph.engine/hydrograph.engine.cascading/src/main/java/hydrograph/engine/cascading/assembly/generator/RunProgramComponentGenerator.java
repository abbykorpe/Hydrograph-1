/*******************************************************************************
 * Copyright 2016 Capital One Services, LLC and Bitwise, Inc.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package hydrograph.engine.cascading.assembly.generator;

import hydrograph.engine.assembly.entity.RunProgramEntity;
import hydrograph.engine.assembly.entity.base.AssemblyEntityBase;
import hydrograph.engine.cascading.assembly.generator.base.CommandComponentGeneratorBase;
import hydrograph.engine.jaxb.commandtypes.RunProgram;
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent;

public class RunProgramComponentGenerator extends CommandComponentGeneratorBase {

	private RunProgram runProgram;
	private RunProgramEntity runProgramEntity;

	public RunProgramComponentGenerator(TypeBaseComponent typeCommandComponent) {
		super(typeCommandComponent);
	}

	@Override
	public void createEntity() {
		runProgramEntity = new RunProgramEntity();
	}

	@Override
	public void initializeEntity() {
		runProgramEntity.setComponentId(runProgram.getId());
		runProgramEntity.setPhase(runProgram.getPhase());
		runProgramEntity.setCommand(runProgram.getCommand().getValue());
	}

	@Override
	public CommandComponentGeneratorBase getCommandComponent() {
		return this;
	}

	@Override
	public AssemblyEntityBase getEntity() {
		return runProgramEntity;
	}

	@Override
	public void castComponentFromBase(TypeBaseComponent baseComponent) {
		this.runProgram = (RunProgram) baseComponent;
	}

	@Override
	public void onComplete() throws Throwable {
		new hydrograph.engine.commandtype.component.RunProgram(runProgramEntity);
	}

	@Override
	public void onStop() {
	}

	@Override
	public String inCommingDependency() {
		return runProgramEntity.getComponentId() + "_in";
	}

	@Override
	public String outGoingDependency() {
		return runProgramEntity.getComponentId() + "_out";
	}
}
