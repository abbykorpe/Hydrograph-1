package com.bitwise.app.propertywindow.handlers;

import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.ui.commands.IElementUpdater;
import org.eclipse.ui.menus.UIElement;

public class ShowHidePropertyHelpHandler extends AbstractHandler implements IHandler,IElementUpdater{
	private UIElement element;
	
	private static ShowHidePropertyHelpHandler INSTANCE;
	
	private boolean ShowHidePropertyHelpChecked;
	
	public ShowHidePropertyHelpHandler(){
		INSTANCE = this;
	}
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		if(ShowHidePropertyHelpChecked){
			element.setChecked(false);
			ShowHidePropertyHelpChecked=false;
		}else{
			element.setChecked(true);
			ShowHidePropertyHelpChecked=true;
		}
		
		return null;
	}
	
	@Override
	public void updateElement(UIElement element, Map parameters) {
		this.element = element;
	}
	
	
	public static ShowHidePropertyHelpHandler getInstance(){
		return INSTANCE;
	}
	
	public boolean isShowHidePropertyHelpChecked(){
		return ShowHidePropertyHelpChecked;
	}
}
