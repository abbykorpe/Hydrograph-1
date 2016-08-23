/********************************************************************************
 * Copyright 2016 Capital One Services, LLC and Bitwise, Inc.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

 
package hydrograph.ui.graph.figure;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.graphics.Color;

/**
 * The Class ELTColorConstants.
 * Provides Colors for various parts of the component.
 * 
 * @author Bitwise
 */
public class ELTColorConstants {
	
	private ELTColorConstants()
	{}

	public static final Color LIGHT_RED = new Color(null,235, 176, 182);
	public static final Color DARK_RED = new Color(null,191, 52, 114);
	public static final Color DARK_GREY = new Color(null,115, 119, 120);
	public static final Color LIGHT_GREY = new Color(null,220, 221, 227);
	public static final Color LIGHT_BLUE = new Color(null, 22, 169, 199);
	public static final Color DARK_BLUE =  new Color(null, 17, 128, 151);
	public static final Color COMPONENT_SELECT_BLUE =  new Color(null, 83,126,137);
	public static final Color BLACK = ColorConstants.black;
	
	public static final Color BLUE_BRAND =  new Color(null,0,148,202);
	public static final Color BLUE_BRAND_BODER =  new Color(null,5,117,158);
	
	public static final Color BG_COMPONENT = LIGHT_GREY;
	public static final Color COMPONENT_BORDER = DARK_GREY;
	
	public static final Color BG_COMPONENT_SELECTED = new Color(null, 0,186,242);
	public static final Color COMPONENT_BORDER_SELECTED = new Color(null, 5,117,158);
	
	public static final Color WATCH_COLOR = new Color(null, 204, 0, 0);
}
