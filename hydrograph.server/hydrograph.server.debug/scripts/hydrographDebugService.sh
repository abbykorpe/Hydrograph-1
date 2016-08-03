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


SERVICE_ID="NULL"
ts="`date +%Y%m%d%H%M%S`"
FILE_PATH="./log/"
FILE_NAME="debugService$ts.log";
PID_FILE="debugService.pid";
PID=-1
SERVICE_START_IND=0
function check_status {
SUPRESS=$1
SERVICE_START_IND=0
if [ -f $PID_FILE ]; then
        PID=`cat $PID_FILE`;
        if $(ps -ef | awk '{ print $2 }' | grep ${PID} > /dev/null)
        then
          if [ "$SUPRESS" == "" ]; then
          echo "Hydrograph debug service is running as PID=$PID"
          fi
          SERVICE_START_IND=1
          return 1
        else
          if [ "$SUPRESS" == "" ]; then
          echo "Hydrograph debug service not started"
          fi
        fi
fi
return 0
}

function print_help {
        echo "Hydrograph UI Debug Service"
        echo "Starts the Hydrograph Debug Service to enable debugging on development environments."
        echo "*Only one instance of the service should be present on the server"
        echo ""
        echo "Usage:"
        echo "hydrographDebugService.sh [status|start|stop|restart]"
        echo ""
        echo "To view the logs, please check out $FILE_PATH"
}

function start_service {
        SUPRESS=$1
        check_status $SUPRESS
        if [ $SERVICE_START_IND -eq 0 ]; then
                echo "Starting Debug Service"
                java -jar sts_elt_dbg_svc.jar > $FILE_PATH$FILE_NAME &
                SERVICE_ID=$!
                echo $SERVICE_ID > $PID_FILE
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
                        kill -9 $PID
                        if [ $? -eq 0 ]; then
                                echo "Service $PID stopped successfully!"
                                return 0
                        fi
                fi
        fi
        return 1
}



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
