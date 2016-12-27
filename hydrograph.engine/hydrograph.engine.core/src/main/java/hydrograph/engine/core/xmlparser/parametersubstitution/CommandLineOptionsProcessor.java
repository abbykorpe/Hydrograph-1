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
package hydrograph.engine.core.xmlparser.parametersubstitution;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hydrograph.engine.core.utilities.GeneralUtilities;

public class CommandLineOptionsProcessor {

	public static final String OPTION_XML_PATH = "xmlpath";
	public static final String OPTION_JOB_ID = "jobid";
	public static final String OPTION_BASE_PATH = "basepath";
	public static final String OPTION_PARAMETER_FILES = "paramfiles";
	public static final String OPTION_COMMANDLINE_PARAM = "param";
	public static final String OPTION_HELP = "help";
	public static final String OPTION_USER_FUNCTION = "udfpath";

	private static final Logger LOG = LoggerFactory.getLogger(CommandLineOptionsProcessor.class);

	public String getXMLPath(String[] args) {
		String[] paths = null;

		paths = GeneralUtilities.getArgsOption(args, OPTION_XML_PATH);

		if (paths != null) {
			// only the first path
			return paths[0];
		} else {
			return null;
		}
	}

	public String getUDFPath(String[] args) {
		String[] paths = null;

		paths = GeneralUtilities.getArgsOption(args, OPTION_USER_FUNCTION);

		if (paths != null) {
			// only the first path
			return paths[0];
		} else {
			return null;
		}
	}

	public String getJobId(String[] args) {
		String[] jobId = null;

		jobId = GeneralUtilities.getArgsOption(args, OPTION_JOB_ID);

		if (jobId != null) {
			// only the first path
			return jobId[0];
		} else {
			return null;
		}
	}

	public String getBasePath(String[] args) {
		String[] basePath = null;

		basePath = GeneralUtilities.getArgsOption(args, OPTION_BASE_PATH);

		if (basePath != null) {
			// only the first path
			return basePath[0];
		} else {
			return null;
		}
	}

	public String getParamFiles(String[] args) {
		String[] commaSeperatedFilePaths = GeneralUtilities.getArgsOption(args, OPTION_PARAMETER_FILES);
		String allPaths = null;
		if (commaSeperatedFilePaths == null) {
			return null;
		} else {

			// combine all param files into one commaseperated string.
			// Internally they can be comma seperated as well
			allPaths = commaSeperatedFilePaths[0];
			for (int i = 1; i < commaSeperatedFilePaths.length; i++) {
				allPaths = allPaths + "," + commaSeperatedFilePaths[i];
			}
		}
		return allPaths;
	}

	public IParameterBank getParams(String[] args) {

		Properties props = new Properties();
		PropertyBank pb = new PropertyBank();

		String[] paramNameValueArray = GeneralUtilities.getArgsOption(args, OPTION_COMMANDLINE_PARAM);

		if (paramNameValueArray == null) {
			return pb;
		}

		for (String paramNameValue : paramNameValueArray) {
			String[] split = paramNameValue.split("=");

			// if there is no equal to or multiple equal to|| param name is
			// blank (cannot be space)||param value is blank
			if (split.length != 2 || split[0].trim().length() == 0 || split[1].length() == 0) {

				throw new CommandOptionException("Command line parameter " + paramNameValue
						+ " is not in correct syntax. It should be param=value");

			}

			props.put(split[0].trim(), split[1]);
			LOG.info("Parsed commandline parameter {} with value-> {}", split[0].trim(), split[1]);
		}

		pb.addProperties(props);
		return pb;
	}

	public static void printUsage() {

		LOG.info("This utility can have following options:");
		LOG.info("  -xmlpath \t\t Required, single, to specify main/parent xml file to execute");
		LOG.info("  -libjars \t\t Optional, single, comma seperated paths of jar files to be included in classpath");
		LOG.info("  -debugxmlpath \t Optional, single, location of the debug file");
		LOG.info("  -jobid \t\t Optional (Required when -debugxmlpath option is specified), single, the job id");
		LOG.info(
				"  -basepath \t\t Optional (Required when -debugxmlpath option is specified), single, the base path where the debug files will be written");
		LOG.info("  -param \t\t Optional, multiple, command line parameters to be used for substitution.");
		LOG.info(
				"  -param <name>=<value>  for each parameter. Use double quotes if value has spaces. Please see example below.");
		LOG.info(
				"  -paramfiles \t\t Optional, multiple, comma seperated paths of parameter files having name vale pair for parameters to be substituted");
		LOG.info("  -help \t\t to print this usage and exit");
		LOG.info("\n");
		LOG.info("Example invocations could be:");
		LOG.info("\t $0 -xmlpath /user/jobs/myjob.xml -param param1=value1 -param param2=\"value2 valueafterspace\" ");
		LOG.info("or");
		LOG.info(
				"\t $0 -xmlpath /user/jobs/myjob.xml -libjars /mypath/my1.jar,/mypath/my2.jar \n\t\t -paramfiles /mypath/paramfile1,/mypath/paramfile2");
		LOG.info("or");
		LOG.info(
				"\t $0 -xmlpath /user/jobs/myjob.xml -debugxmlpath /user/jobs/debug/mydebug.xml -jobid myjob \n \t\t -basepath /tmp/debug/ -libjars /mypath/my1.jar,/mypath/my2.jar -paramfiles /mypath/paramfile1,/mypath/paramfile2");
	}

	public class CommandOptionException extends RuntimeException {

		/**
		 * 
		 */
		private static final long serialVersionUID = 2015773561652559751L;

		public CommandOptionException(String msg) {
			super(msg);
		}

		public CommandOptionException(Throwable e) {
			super(e);
		}
	}
}
