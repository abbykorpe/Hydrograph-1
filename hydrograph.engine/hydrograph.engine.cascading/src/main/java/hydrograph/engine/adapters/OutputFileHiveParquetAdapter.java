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
package hydrograph.engine.adapters;

import hydrograph.engine.adapters.base.OutputAdapterBase;
import hydrograph.engine.cascading.assembly.OutputFileHiveParquetAssembly;
import hydrograph.engine.cascading.assembly.base.BaseComponent;
import hydrograph.engine.cascading.assembly.infra.ComponentParameters;
import hydrograph.engine.core.component.entity.base.HiveEntityBase;
import hydrograph.engine.core.component.generator.OutputFileHiveParquetEntityGenerator;
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent;

public class OutputFileHiveParquetAdapter extends OutputAdapterBase{
	
	
	private static final long serialVersionUID = -1606863131036936000L;
	OutputFileHiveParquetAssembly outputFileHiveParquetAssembly;
	OutputFileHiveParquetEntityGenerator entityGenerator;
	
	public OutputFileHiveParquetAdapter(TypeBaseComponent component){
		
		entityGenerator=new OutputFileHiveParquetEntityGenerator(component);
		
	}
	
	
	@Override
	public void createAssembly(ComponentParameters componentParameters) {
		outputFileHiveParquetAssembly = new OutputFileHiveParquetAssembly(entityGenerator.getEntity(), componentParameters);
	}

	@Override
	public BaseComponent<HiveEntityBase> getAssembly() {
		return outputFileHiveParquetAssembly;
	}


	
}
