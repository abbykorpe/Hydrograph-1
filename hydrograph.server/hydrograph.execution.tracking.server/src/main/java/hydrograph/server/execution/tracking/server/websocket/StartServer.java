/********************************************************************************
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
 ******************************************************************************/

package hydrograph.server.execution.tracking.server.websocket;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;
import org.glassfish.tyrus.server.Server;

import hydrograph.server.execution.tracking.server.TrackingSocketServer;
import hydrograph.server.execution.tracking.utils.ExecutionTrackingUtils;

/**
 * The Class StartServer.
 */
public class StartServer {
	
	/** The Constant logger. */
	private final static Logger logger = Logger.getLogger(StartServer.class);

	/**
	 * Start the server
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		
		logger.info("*************Starting Server **************");
		StartServer startServer = new StartServer();
		ExecutionTrackingUtils.INSTANCE.getFilePath();
		startServer.runServer(args.length != 0 ? args[0] : ExecutionTrackingUtils.INSTANCE.getPortNo());
		logger.info("*************Server Stopped**************");
		
	} 

	/**
	 * Run server.
	 *
	 * @param portNo the port no
	 */
	private void runServer(String portNo) {
		Server server = new Server("localhost", Integer.parseInt(portNo), "/", TrackingSocketServer.class);
		try {
			server.start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			logger.info("Please press a key to stop the server.");
	        reader.readLine();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
