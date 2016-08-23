#!/bin/bash
cd Contents/MacOS
./hydrograph
cd ../../config/service
chmod 755 *
java -cp hydrograph.server.execution.tracking.server.jar hydrograph.server.execution.tracking.server.websocket.StartServer
exit 0