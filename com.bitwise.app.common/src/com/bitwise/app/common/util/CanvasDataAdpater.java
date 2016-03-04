package com.bitwise.app.common.util;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.IAdaptable;

public class CanvasDataAdpater {
	private List<String> parameterList;
	private String canvasData;
	
	public CanvasDataAdpater(String canvasData){
		this.canvasData = canvasData;
		//parameterList = new ArrayList<>();
		parameterList = new LinkedList<>();
	}
	
	public void fetchData(){
		Pattern parameterPattren = Pattern.compile("@\\{(.*?)\\}");
		Matcher matcher = parameterPattren.matcher(canvasData);
		while (matcher.find()) {
		    String parameter = matcher.group(1);
		    parameterList.add(parameter);
		}
	}
	
	public List<String> getParameterList(){
		fetchData();
		/*IProject project = (IProject)((IAdaptable)firstElement).getAdapter(IProject.class);
        IPath path = project.getFullPath();*/
		return parameterList;		
	}
	
}
