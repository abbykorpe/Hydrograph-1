#!/bin/bash

#*******************************************************************************
#  Copyright 2016 Capital One Services, LLC and Bitwise, Inc.
#  Licensed under the Apache License, Version 2.0 (the "License");
#  you may not use this file except in compliance with the License.
#  You may obtain a copy of the License at
#  http://www.apache.org/licenses/LICENSE-2.0
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS,
#  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  See the License for the specific language governing permissions and
#  limitations under the License.
#*******************************************************************************

#IMPORTANT: Please update the following paths for your environment for successful 
#           execution of the script.
SERVER_JAR_PATH=./bin
LIB_PATH=./lib
LOG_PATH=./log
LIBJARS=""
SERVER_JAR=hydrograph.server.execution.tracking.server.jar

SERVICE_ID="NULL"
ts="`date +%Y%m%d%H%M%S`"
LOG_FILE_NAME="executionTrackingService$ts.log";
PID_FILE="executionTrackingService.pid";
PID=-1
SERVICE_START_IND=0
PORT_NUMBER=8007

#============================== Functions ==============================================#

appendLibJars(){

  LIB_FOLDER=$1
  
  if [ -f ${LIB_FOLDER}/*.jar ];
  then

	  for JARFILE in `ls -1 ${LIB_FOLDER}/*.jar`
		do
					if [ -n "$LIBJARS" ]; then
							LIBJARS=${LIBJARS},$JARFILE
					else
							LIBJARS=${SERVER_JAR_PATH}/${SERVER_JAR},$JARFILE
					fi
			done
	fi
 }

function check_status {
SUPRESS=$1
SERVICE_START_IND=0
if [ -f $LOG_PATH/$PID_FILE ]; then
        PID=`cat $LOG_PATH/$PID_FILE`;
        if $(ps -ef | awk '{ print $2 }' | grep -w ${PID} > /dev/null)
        then
          if [ "$SUPRESS" == "" ]; then
          echo "Hydrograph execution tracking service is running as PID=$PID"
          fi
          SERVICE_START_IND=1
          return 1
        else
          if [ "$SUPRESS" == "" ]; then
          echo "Hydrograph execution tracking service not started"
          fi
        fi
fi
return 0
}

function print_help {
		echo "====================================="
        echo "Hydrograph Execution Tracking Service"
		echo "====================================="
        echo "Starts the Hydrograph Execution Tracking Service to enable execution tracking."
        echo "*Only one instance of the service should be present on the server"
        echo ""
        echo "Usage:"
        echo "$0 [status|start|stop|restart]"
        echo ""
        echo "To view the logs, please check out ${LOG_PATH}/"
}

function start_service {
        SUPRESS=$1
        check_status $SUPRESS
        if [ $SERVICE_START_IND -eq 0 ]; then
                echo "Starting Execution Tracking Service"
				appendLibJars
				
                cmd="java -cp ${SERVER_JAR_PATH}/${SERVER_JAR} hydrograph.server.execution.tracking.server.websocket.StartServer ${PORT_NUMBER} > ${LOG_PATH}/${LOG_FILE_NAME}"
				echo "Executing Command:  ${cmd}"
				$cmd &
                SERVICE_ID=$!
                echo $SERVICE_ID > $LOG_PATH/$PID_FILE
                echo "Service $SERVICE_ID started successfully!"
        else
                echo "Service $PID already running!"
        fi
        }

function stop_service {
        SUPRESS=$1
        if ! check_status $SUPRESS
        then
                echo "Trying to stop service..."
                if [ $PID -eq -1 ]; then
                        echo "No previously running service was found!"
                else
                        kill $PID
                        if [ $? -eq 0 ]; then
                                echo "Execution Tracking Service ($PID) stopped successfully!"
                                return 0
                        fi
                fi
        fi
        return 1
}

#====================================== Main ======================================================#

if [ $# -eq 0 ]; then
        print_help
        exit 1
else
        case "$1" in
        stop) stop_service
        ;;
        start) start_service
        ;;
        restart)
                if ! check_status "SUPRESS"
                then
                  stop_service
                fi
                start_service
        ;;
        status) check_status
        ;;
        *) print_help
        exit 1;;
        esac
fi
