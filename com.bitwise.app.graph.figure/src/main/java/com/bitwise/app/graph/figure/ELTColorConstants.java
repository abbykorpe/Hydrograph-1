package com.bitwise.app.graph.figure;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.graphics.Color;

/**
 * The Class ELTColorConstants.
 * 
 * @author Bitwise
 */
public class ELTColorConstants {
	
	private ELTColorConstants()
	{}

	public static final Color lightRed = new Color(null,235, 176, 182);
	public static final Color darkRed = new Color(null,191, 52, 114);
	public static final Color darkGrey = new Color(null,115, 119, 120);
	public static final Color lightGrey = new Color(null,220, 221, 227);
	public static final Color lightBlue = new Color(null, 22, 169, 199);
	public static final Color darkBlue =  new Color(null, 17, 128, 151);
	public static final Color componentSelectBlue =  new Color(null, 83,126,137);
	public static final Color black = ColorConstants.black;
	
	public static final Color blueBrand =  new Color(null,0,148,202);
	public static final Color blueBrandBoder =  new Color(null,5,117,158);
	
	public static final Color bgComponent = lightGrey;
	public static final Color bgComponentSelected = blueBrand;
	public static final Color componentBorder = darkGrey;
	public static final Color componentSelectedBorder = darkBlue;
	
	public static final Color watchColor = new Color(null, 204, 0, 0);
}
