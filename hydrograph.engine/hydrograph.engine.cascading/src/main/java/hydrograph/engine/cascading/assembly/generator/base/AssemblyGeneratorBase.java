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
package hydrograph.engine.cascading.assembly.generator.base;

import hydrograph.engine.cascading.assembly.base.BaseComponent;
import hydrograph.engine.cascading.assembly.infra.ComponentParameters;
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent;

public abstract class AssemblyGeneratorBase implements GeneratorBase  {

	/**
	 * The constructor accepts a {@link TypeBaseComponent} object returned by
	 * jaxb and calls the methods to create and initialize the entity object
	 * using the {@link TypeBaseComponent} object
	 * 
	 * @param baseComponent
	 *            {@link TypeBaseComponent} object which holds all the
	 *            information for the component from xml
	 */
	public AssemblyGeneratorBase(TypeBaseComponent baseComponent) {
		castComponentFromBase(baseComponent);
		createEntity();
		initializeEntity();
	}


	/**
	 * Fetches the entity object
	 * 
	 * @return the entity object
	 */
	// public abstract AssemblyEntityBase getEntity();

	/**
	 * Creates the Hydrograph assembly
	 * 
	 * @param componentParameters
	 */
	public abstract void createAssembly(ComponentParameters componentParameters);

	/**
	 * Returns the Hydrograph assembly object
	 * 
	 * @return the Hydrograph assembly object
	 */
	public abstract BaseComponent getAssembly();
}