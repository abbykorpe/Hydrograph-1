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
/**
 * 
 */
package hydrograph.engine.commandline.utilities;


/**
 * @author Alpeshk
 *
 */
public class GeneralCommandLineUtilities {


	public Object loadAndInitClass(String className) {

		@SuppressWarnings("rawtypes")
		Class loadedClass;

		try {
			loadedClass = Class.forName(className);
		} catch (Exception e) {

			throw new LoadClassException("Given class " + className
					+ " could not be loaded.", e);
		}

		try {
			return loadedClass.newInstance();
		} catch (Exception e) {

			throw new LoadClassException("Given class " + className
					+ " could not be instantiated.", e);

		}

	}

	private class LoadClassException extends RuntimeException {
		private static final long serialVersionUID = 4982305737525826906L;

		public LoadClassException(String msg, Throwable e) {
			super(msg, e);
		}
	}
}
