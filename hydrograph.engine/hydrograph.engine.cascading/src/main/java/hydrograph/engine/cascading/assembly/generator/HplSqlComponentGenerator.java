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

import hydrograph.engine.assembly.entity.HplSqlEntity;
import hydrograph.engine.assembly.entity.base.AssemblyEntityBase;
import hydrograph.engine.cascading.assembly.generator.base.CommandComponentGeneratorBase;
import hydrograph.engine.jaxb.commandtypes.Hplsql;
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent;

public class HplSqlComponentGenerator extends CommandComponentGeneratorBase {

	private Hplsql hplsql;
	private HplSqlEntity hplSqlEntity;

	public HplSqlComponentGenerator(TypeBaseComponent typeCommandComponent) {
		super(typeCommandComponent);
	}

	@Override
	public void createEntity() {
		hplSqlEntity = new HplSqlEntity();
	}

	@Override
	public void initializeEntity() {
		hplSqlEntity.setComponentId(hplsql.getId());
		hplSqlEntity.setBatch(hplsql.getBatch());
		hplSqlEntity.setComponentName(hplsql.getName());
		hplSqlEntity.setCommand(hplsql.getCommand().getCmd());
		if (hplsql.getExecute().getUri() != null)
			hplSqlEntity.setUri(hplsql.getExecute().getUri().getValue());
		if (hplsql.getExecute().getQuery() != null)
			hplSqlEntity.setQuery(hplsql.getExecute().getQuery().getValue());
	}

	@Override
	public CommandComponentGeneratorBase getCommandComponent() {
		return this;
	}

	@Override
	public AssemblyEntityBase getEntity() {
		return hplSqlEntity;
	}

	@Override
	public void castComponentFromBase(TypeBaseComponent baseComponent) {
		this.hplsql = (Hplsql) baseComponent;
	}

	@Override
	public void onComplete() throws Throwable {
		new hydrograph.engine.commandtype.component.HplSqlComponent(
				hplSqlEntity);
	}

	@Override
	public void onStop() {
	}

	@Override
	public String inCommingDependency() {
		return hplSqlEntity.getComponentId() + "_in";
	}

	@Override
	public String outGoingDependency() {
		return hplSqlEntity.getComponentId() + "_out";
	}
}
