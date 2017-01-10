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
package hydrograph.engine.core.commandlineparser;

import hydrograph.engine.core.utilities.CommandLineOptionsProcessor;
import hydrograph.engine.core.xmlparser.parametersubstitution.IParameterBank;
import hydrograph.engine.core.xmlparser.parametersubstitution.PropertyBank;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Properties;

public class CLIParser {

	public static final String OPTION_XML_PATH = "xmlpath";
	public static final String OPTION_DEBUG_XML_PATH = "debugxmlpath";
	public static final String OPTION_JOB_ID = "jobid";
	public static final String OPTION_BASE_PATH = "basepath";
	public static final String OPTION_PARAMETER_FILES = "paramfiles";
	public static final String OPTION_COMMANDLINE_PARAM = "param";
	public static final String OPTION_NO_EXECUTION = "noexecution";
	public static final String OPTION_DOT_PATH = "dotpath";
	public static final String OPTION_HELP = "help";
	public static final String OPTION_LIB_JARS="libjars";

	public static Options options = new Options();
	public static CommandLineParser cmdParser = new BasicParser();
	private static Logger LOG = LoggerFactory.getLogger(CLIParser.class);

	public static void initializeOptions() {
		options.addOption(OPTION_XML_PATH, true, "path of main xml");
		options.addOption(OPTION_DEBUG_XML_PATH, true, "path of debug xml");
		options.addOption(OPTION_JOB_ID, true, "unique Id of job");
		options.addOption(OPTION_BASE_PATH, true, "base path to store debug files");
		options.addOption(OPTION_PARAMETER_FILES, true, "parameter files");
		options.addOption(OPTION_COMMANDLINE_PARAM, true, "command line parameter");
		options.addOption(OPTION_NO_EXECUTION, false, "parameter to skip execution");
		options.addOption(OPTION_DOT_PATH, true, "path to write dot files");
		options.addOption(OPTION_HELP, false, "help");
		options.addOption(OPTION_LIB_JARS, false, "path of jar files to be included in classpath");

	}

	public static String getXmlPath(String[] args, Properties config) throws ParseException {
		initializeOptions();
		CommandLine cmd = cmdParser.parse(options, args);
		String path = null;
		if (cmd.hasOption(OPTION_XML_PATH)) {
			path = cmd.getOptionValue(OPTION_XML_PATH);
			return path;
		}

		else if (config.getProperty(OPTION_XML_PATH) != null) {
			// if path is not found from command line then check from config
			path = config.getProperty(OPTION_XML_PATH);
			return path;
		} else
			throw new CommandOptionException(
					"No XML file path, either through command line or XMLFilePath input property is provided");
	}

	public static String getJobId(String[] args) throws ParseException {
		initializeOptions();
		CommandLine cmd = cmdParser.parse(options, args);
		if (cmd.hasOption(OPTION_JOB_ID))
			return cmd.getOptionValue(OPTION_JOB_ID);
		else
			return null;
	}

	public static String getDebugXmlPath(String[] args, Properties config) throws ParseException {
		initializeOptions();
		CommandLine cmd = cmdParser.parse(options, args);
		if (cmd.hasOption(OPTION_DEBUG_XML_PATH))
			return cmd.getOptionValue(OPTION_DEBUG_XML_PATH);
		else
			// if path is not found from command line then check from config
			return config.getProperty(CommandLineOptionsProcessor.OPTION_XML_PATH);
	}

	public static String getBasePath(String[] args) throws ParseException {
		initializeOptions();
		CommandLine cmd = cmdParser.parse(options, args);
		if (cmd.hasOption(OPTION_BASE_PATH))
			return cmd.getOptionValue(OPTION_BASE_PATH);
		else
			return null;
	}

	public static String getParameterFiles(String[] args) throws ParseException {
		initializeOptions();
		CommandLine cmd = cmdParser.parse(options, args);
		if (cmd.hasOption(OPTION_PARAMETER_FILES))
			return cmd.getOptionValue(OPTION_PARAMETER_FILES);
		else
			return null;
	}

	public static IParameterBank getParameters(String[] args) throws ParseException {
		initializeOptions();
		Properties props = new Properties();
		PropertyBank pb = new PropertyBank();

		CommandLine cmd = cmdParser.parse(options, args);
		if (cmd.hasOption(OPTION_COMMANDLINE_PARAM)) {

			Option[] options = cmd.getOptions();

			for (int i = 0; i < options.length; i++) {
				if (options[i].getOpt().equalsIgnoreCase("param")) {
					String[] propNameAndValue = options[i].getValue().split("=");

					// if there is no equal to or multiple equal to|| param name
					// is
					// blank (cannot be space)||param value is blank
					if (propNameAndValue.length != 2 || propNameAndValue[0].trim().length() == 0
							|| propNameAndValue[1].length() == 0) {

						throw new CommandOptionException("Command line parameter " + Arrays.toString(propNameAndValue)
								+ " is not in correct syntax. It should be param=value");

					}
					props.put(propNameAndValue[0], propNameAndValue[1]);
				}
			}
			pb.addProperties(props);
			return pb;
		} else
			return pb;

	}

	public static boolean isArgumentOptionPresent(String[] args, String option) throws ParseException {

		initializeOptions();
		CommandLine cmd = cmdParser.parse(options, args);
		return cmd.hasOption(option);

	}

	public static String getDotFilePath(String[] args) throws ParseException {
		initializeOptions();
		CommandLine cmd = cmdParser.parse(options, args);
		if (cmd.hasOption(OPTION_DOT_PATH))
			return cmd.getOptionValue(OPTION_DOT_PATH);
		else
			return null;

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

	public static class CommandOptionException extends RuntimeException {

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
