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
package hydrograph.engine.cascading.assembly;

import hydrograph.engine.assembly.entity.base.AssemblyEntityBase;
import hydrograph.engine.cascading.assembly.base.BaseComponent;
import hydrograph.engine.cascading.assembly.infra.ComponentParameters;

public class InputFileSequenceFormat extends BaseComponent {

	private static final long serialVersionUID = 1875266476440018910L;

	public InputFileSequenceFormat(AssemblyEntityBase assemblyEntityBase, ComponentParameters componentParameters) {
		super(assemblyEntityBase, componentParameters);
	}

	@Override
	public void castEntityFromBase(AssemblyEntityBase assemblyEntityBase) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void createAssembly() {
		// TODO Auto-generated method stub

	}

	/*
	 * @Override protected void prepareScheme() {
	 * 
	 * scheme = new SequenceFile(parameters.getInputFields());
	 * 
	 * }
	 * 
	 * @Override public void validateParameters(ComponentParameters
	 * componentParameters) { if (componentParameters.getPathUri() == null) {
	 * throw new ParametersValidationException(
	 * "Input file parameter cannot be null"); }
	 * 
	 * if (componentParameters.getInputFields() == null) { throw new
	 * ParametersValidationException( "Input fields parameter cannot be null");
	 * }
	 * 
	 * if (componentParameters.getComponentName() == null) { throw new
	 * ParametersValidationException( "Component Name parameter cannot be null"
	 * ); } }
	 */

}
