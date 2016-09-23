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

package hydrograph.server.execution.tracking.server;

import java.io.IOException;
import java.util.Set;

import javax.websocket.CloseReason;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hydrograph.server.execution.tracking.json.JsonDecoder;
import hydrograph.server.execution.tracking.json.JsonEncoder;
import hydrograph.server.execution.tracking.server.status.datastructures.Constants;
import hydrograph.server.execution.tracking.server.status.datastructures.ExecutionStatus;

/**
 * The Class TrackingSocketServer.
 */
@ServerEndpoint(value = "/executionTracking/{client-id}", encoders = { JsonEncoder.class }, decoders = { JsonDecoder.class })
public class TrackingSocketServer {

	/** The all sessions. */
	private static Set<Session> allSessions;
	
	/** The Constant logger. */
	final static Logger logger = LoggerFactory.getLogger(TrackingSocketServer.class);
	
	
	/**
	 * On open.
	 *
	 * @param session the session
	 */
	@OnOpen
	public void onOpen(Session session) {
		logger.debug("Starting a session" + session.toString());
	}

	/**
	 * Tracking communicator.
	 *
	 * @param executionStatus the execution status
	 * @param session the session
	 * @param clientId the client id
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@OnMessage
	public void trackingCommunicator(ExecutionStatus executionStatus, Session session, @PathParam("client-id") String clientId)
			throws IOException {
		logger.debug("Execution Tracking Request - " + executionStatus);

		session.getUserProperties().put(Constants.CLIENTID, clientId);
		
		/**
		 * Ui Client send connection request and save jobid in open session.
		 */
		getTrackingStatus(executionStatus, session);

		/**
		 * 
		 * Status posted by engine client will delivered to requested UI client based on Job Id.
		 */
		postTrackingStatus(executionStatus, session);
		
		/**
		 * UI will send kill request along with job id, server intern call engine client to kill the job.
		 */
		killRequestedJob(executionStatus, session);
	}

	
	private void killRequestedJob(ExecutionStatus executionStatus, Session session) {
		if (Constants.KILL.equalsIgnoreCase(executionStatus.getType())) {
			logger.info("Kill request received for job - " + executionStatus.getJobId() );
			final String jobId = executionStatus.getJobId().trim();
			allSessions = session.getOpenSessions();
			for (Session openSession : allSessions) {
				try {
					if (openSession.getUserProperties().get(Constants.CLIENTID) != null) {
						if (((String) openSession.getUserProperties().get(Constants.CLIENTID))
								.equalsIgnoreCase(Constants.ENGINE_CLIENT + executionStatus.getJobId())) {
							logger.debug("Before sending kill" + jobId);
							openSession.getBasicRemote().sendText("");
							logger.debug("After sending kill" + jobId);
						}
					}
				} catch (IOException e) {
					logger.error("Failed to send kill request for - " + jobId, e);
				}
			}
		}
	}

	
	private void postTrackingStatus(ExecutionStatus executionStatus, Session session) {
		if (Constants.POST.equalsIgnoreCase(executionStatus.getType())) {
			allSessions = session.getOpenSessions();
			for (Session openSession : allSessions) {
				try {
					if (openSession.getUserProperties().get(Constants.JOBID) != null) {
						if (((String) openSession.getUserProperties().get(Constants.JOBID))
								.equalsIgnoreCase(executionStatus.getJobId())) {
							try {
								openSession.getBasicRemote().sendObject(executionStatus);
							} catch (EncodeException e) {
								e.printStackTrace();
							}
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	
	private void getTrackingStatus(ExecutionStatus executionStatus, Session session) {
		if (Constants.GET.equalsIgnoreCase(executionStatus.getType())) {
			session.getUserProperties().put(Constants.JOBID, executionStatus.getJobId());
		}
	}

	/**
	 * On close.
	 *
	 * @param reason the reason
	 * @param session the session
	 */
	@OnClose
	public void onClose(CloseReason reason, Session session) {
		logger.debug("Closing a WebSocket due to " + reason.getReasonPhrase());
	}
}