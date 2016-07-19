REM 0 = hide window, 1 = show window (useful for debugging)
Set WshShell = CreateObject("WScript.Shell")

WshShell.Run "java -classpath config;elt-debug-0.1.1.jar hydrograph.server.debug.service.DebugService", 0, False
Set WshShell = Nothing