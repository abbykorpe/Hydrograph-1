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
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.draw2d.text.FlowPage;
import org.eclipse.draw2d.text.ParagraphTextLayout;
import org.eclipse.draw2d.text.TextFlow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Display;

/**
 * A Figure with a bent corner and an embedded TextFlow within a FlowPage that contains
 * text.
 * @author Bitwise
 * 
 */
public class CommentBoxFigure
	extends BentCornerFigure
{

	/** The inner TextFlow **/
	private TextFlow textFlow;
	private Font font;
	
	/**
	 *  Creates a new CommentBoxFigure with a default MarginBorder size of DEFAULT_CORNER_SIZE
	 *  - 3 and a FlowPage containing a TextFlow with the style WORD_WRAP_SOFT.
	 */
	public CommentBoxFigure(){
		this(BentCornerFigure.DEFAULT_CORNER_SIZE - 3);
		setInitialColor();
	}
	
	/** 
	 * Creates a new CommentBoxFigure with a MarginBorder that is the given size and a
	 * FlowPage containing a TextFlow with the style WORD_WRAP_SOFT.
	 * 
	 * @param borderSize the size of the MarginBorder
	 */
	public CommentBoxFigure(int borderSize){
		setBorder(new MarginBorder(5));
		FlowPage flowPage = new FlowPage();
	
		textFlow = new TextFlow();
	
		textFlow.setLayoutManager(new ParagraphTextLayout(textFlow,
						ParagraphTextLayout.WORD_WRAP_SOFT));
	
		flowPage.add(textFlow);
	
		setLayoutManager(new StackLayout());
		add(flowPage);
		font = new Font( Display.getDefault(), "Arial", 9,
				SWT.NORMAL );
		setFont(font);
		setForegroundColor(ColorConstants.black);
		setOpaque(false);
	}
	
	/**
	 * Returns the text inside the TextFlow.
	 * 
	 * @return the text flow inside the text.
	 */
	public String getText(){
		return textFlow.getText();
	}
	/**
	 * Sets the initial color for border of comment box
	 */
	private void setInitialColor(){
		new Color(null, ELTColorConstants.COMPONENT_BORDER_SELECTED_RGB[0], ELTColorConstants.COMPONENT_BORDER_SELECTED_RGB[1], ELTColorConstants.COMPONENT_BORDER_SELECTED_RGB[2]);
	}
	
	/**
	 * Sets the text of the TextFlow to the given value.
	 * 
	 * @param newText the new text value.
	 */
	public void setText(String newText){
		textFlow.setText(newText);
		}

	}