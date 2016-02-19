package com.bitwise.app.common.constants.imagepath;

/**
 * 
 * Enum to store Image path constants
 * 
 * @author Bitwise
 *
 */
public enum ImagePathConstant {
	ADD_BUTTON("/icons/add.png"),
	DELETE_BUTTON("/icons/delete.png"),
	MOVEUP_BUTTON("/icons/up.png"),
	MOVEDOWN_BUTTON("/icons/down.png"),
	EDIT_BUTTON("/icons/editImage.png");
	private String path;
	private ImagePathConstant(String path){
		this.path = path;
	}
	
	public String getImagePath(){
		return path;
	}
	
}
