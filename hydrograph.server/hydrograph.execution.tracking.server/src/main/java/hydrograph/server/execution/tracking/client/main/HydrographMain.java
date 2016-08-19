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

package hydrograph.server.execution.tracking.client.main;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

import javax.websocket.CloseReason;
import javax.websocket.Session;

import org.apache.log4j.Logger;
import org.glassfish.tyrus.client.ClientManager;

import com.google.gson.Gson;

import hydrograph.engine.commandline.utilities.HydrographService;
import hydrograph.engine.execution.tracking.ComponentInfo;
import hydrograph.server.execution.tracking.client.HydrographEngineCommunicatorSocket;
import hydrograph.server.execution.tracking.client.logger.ExecutionTrackingFileLogger;
import hydrograph.server.execution.tracking.server.status.datastructures.ComponentStatus;
import hydrograph.server.execution.tracking.server.status.datastructures.Constants;
import hydrograph.server.execution.tracking.server.status.datastructures.ExecutionStatus;
import hydrograph.server.execution.tracking.utils.ExecutionTrackingUtils;

/**
 * The Class HydrographMain use to execute job also post tracking status if
 * execution tracking flag is enable.
 * 
 * @author Bitwise
 */
public class HydrographMain {

	/** The Constant logger. */
	private final static Logger logger = Logger.getLogger(HydrographMain.class);

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 * @throws Exception
	 *             the exception
	 */
	public static void main(String[] args) throws Exception {
		final CountDownLatch latch = new CountDownLatch(1);
		Session session = null;
		String[] argsList = args;
		boolean isExecutionTracking = false;

		List<String> argumentList = Arrays.asList(args);
		final String jobId = getJobId(argumentList);

		if (argumentList.contains(Constants.IS_TRACKING_ENABLE)) {
			int index = argumentList.indexOf(Constants.IS_TRACKING_ENABLE);
			isExecutionTracking = Boolean.valueOf(argsList[index + 1]);
			argumentList = argumentList.subList(0, index);
			argsList = argumentList.toArray(new String[argumentList.size()]);
		}
		final String[] argsFinalList = argsList;

		logger.debug("Execution tracking enabled - " + isExecutionTracking);

		final Timer timer = new Timer();

		/**
		 * Start new thread to run job
		 */
		final HydrographService execution = new HydrographService();
		executeGraph(latch, jobId, argsFinalList, timer, execution);
		
		/**
		 * If tracking enable, start to post execution tracking status.
		 */
		sendExecutionTrackingStatus(latch, session, isExecutionTracking, jobId, timer, execution);
	}

	
	private static void sendExecutionTrackingStatus(final CountDownLatch latch, Session session,
			boolean isExecutionTracking, final String jobId, final Timer timer, final HydrographService execution)
			throws IOException {
		if (isExecutionTracking) {
			try {
				ClientManager client = ClientManager.createClient();
				final HydrographEngineCommunicatorSocket socket = new HydrographEngineCommunicatorSocket(execution);
				session = client.connectToServer(socket,
						new URI(ExecutionTrackingUtils.INSTANCE.getTrackingUrl() + jobId));

				TimerTask task = new TimerTask() {
					@Override
					public void run() {
						List<ComponentInfo> componentInfos = execution.getStatus();
						List<ComponentStatus> componentStatusList = new ArrayList<ComponentStatus>();
						for (ComponentInfo componentInfo : componentInfos) {
							ComponentStatus componentStatus = new ComponentStatus(componentInfo.getComponentId(),
									componentInfo.getCurrentStatus(), componentInfo.getProcessedRecords());
							componentStatusList.add(componentStatus);
						}
						ExecutionStatus executionStatus = new ExecutionStatus(componentStatusList);
						executionStatus.setJobId(jobId);
						executionStatus.setType(Constants.POST);
						Gson gson = new Gson();
						try {
							socket.sendMessage(gson.toJson(executionStatus));
							ExecutionTrackingFileLogger.INSTANCE.log(jobId, executionStatus);
						} catch (IOException e) {
							logger.error("Fail to send status for job - " + jobId, e);
							timer.cancel();
						}
					}
				};
				timer.schedule(task, 0l, Constants.STATUS_FREQ);
				latch.await();
			} catch (Throwable t) {
				logger.error("Failure in job - " + jobId, t);
				timer.cancel();
			} finally {
				if (session != null && session.isOpen()) {
					logger.debug("Closing Websocket engine client");
					CloseReason closeReason = new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "Session Closed");
					session.close(closeReason);
				}
			}
		}
	}

	private static void executeGraph(final CountDownLatch latch, final String jobId, final String[] argsFinalList,
			final Timer timer, final HydrographService execution) {
		new Thread(new Runnable() {
			public void run() {
				try {
					execution.executeGraph(argsFinalList);
					Thread.sleep(Constants.DELAY_TIME);
					timer.cancel();
					ExecutionTrackingFileLogger.INSTANCE.disposeLogger(jobId);
					latch.countDown();
				} catch (Exception e) {
					logger.error(e);
					logger.info("JOB FAILED");
					try {
						Thread.sleep(Constants.DELAY_TIME);
					} catch (InterruptedException e1) {
					}
					timer.cancel();
					latch.countDown();
				}
			}
		}).start();
	}

	/**
	 * Gets the job id.
	 *
	 * @param argumentList
	 *            the argument list
	 * @return the job id
	 */
	private static String getJobId(List<String> argumentList) {
		if (argumentList.contains(Constants.JOBID_KEY)) {
			return argumentList.get(argumentList.indexOf(Constants.JOBID_KEY) + 1);
		}
		return null;
	}
}
