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

import hydrograph.engine.assembly.entity.base.AssemblyEntityBase;
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent;
import riffle.process.DependencyIncoming;
import riffle.process.DependencyOutgoing;
import riffle.process.Process;
import riffle.process.ProcessComplete;
import riffle.process.ProcessStop;

@Process
public abstract class CommandComponentGeneratorBase implements GeneratorBase {

	/**
	 * The constructor accepts a {@link TypeBaseComponent} object returned by
	 * jaxb and calls the methods to create and initialize the entity object
	 * using the {@link TypeBaseComponent} object
	 * 
	 * @param baseComponent
	 *            {@link TypeBaseComponent} object which holds all the
	 *            information for the component from xml
	 */
	public CommandComponentGeneratorBase(TypeBaseComponent typeCommandComponent) {
		castComponentFromBase(typeCommandComponent);
		createEntity();
		initializeEntity();
	}

	@ProcessComplete
	public void complete() throws Throwable {
		onComplete();
	}

	@ProcessStop
	public void stop() {
		onStop();
	}

	@DependencyIncoming
	public String in() {
		return inCommingDependency();
	}

	@DependencyOutgoing
	public String out() {
		return outGoingDependency();
	}

	
	/**
	 * @return the AssemblyEntityBase
	 */
	public abstract AssemblyEntityBase getEntity();

	/**
	 * @return the CommandComponentGeneratorBase
	 */
	public abstract CommandComponentGeneratorBase getCommandComponent();

	/**
	 * @throws Throwable
	 * 
	 * The method onComplete calls by Riffle
	 */
	public abstract void onComplete() throws Throwable ;

	/**
	 *  The method onStop calls by Riffle
	 */
	public abstract void onStop();

	/**
	 * @return the flow inCommingDependency
	 */
	public abstract String inCommingDependency();

	/**
	 * @return the flow outGoingDependency
	 */
	public abstract String outGoingDependency();
}
