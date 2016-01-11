package com.bitwise.app.common.interfaces.parametergrid;

import java.util.List;

public interface DefaultGEFCanvas {
	public String getParameterFile();
	public String getXMLString();
	public List<String> getLatestParameterList();
	public String getCurrentParameterFilePath();
	public void setCurrentParameterFilePath(String currentParameterFilePath);
	public String getActiveProject();
	public String getJobName();
	public void disableRunningJobResource();
	public void enableRunningJobResource();
}
