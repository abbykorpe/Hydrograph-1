
@echo off

cd %~dp0
start hydrograph.exe -vm "%JAVA_HOME%\bin" -data
start java -cp config\service\hydrograph.server.execution.tracking.server.jar hydrograph.server.execution.tracking.server.websocket.StartServer
exit