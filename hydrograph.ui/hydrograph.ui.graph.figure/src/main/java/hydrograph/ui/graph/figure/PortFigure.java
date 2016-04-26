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

import hydrograph.ui.graph.model.PortAlignmentEnum;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Display;

/**
 * The Class PortFigure.
 * 
 * @author Bitwise
 */
public class PortFigure extends Figure {

	private Color portColor;
	private String terminal;
	private FixedConnectionAnchor anchor;
	private TooltipFigure tooltipFigure;
	private String labelOfPort;
	private String portType;
	private static boolean displayPortLabels;
	private boolean isWatched;
	private PortAlignmentEnum portAlignment;
	/**
	 * Instantiates a new port figure.
	 * 
	 * @param portColor
	 *            the port color
	 * @param labelOfPort 
	 * @param terminal
	 *            the terminal
	 */
	public PortFigure(Color portColor, String portType, int portSeq,
			int totalPorts, String nameOfPort, String labelOfPort, PortAlignmentEnum alignment) {
		this.portColor = portColor;
		this.terminal = portType + portSeq;
		this.terminal = nameOfPort;
		this.anchor = new FixedConnectionAnchor(this, portType, totalPorts,
				portSeq);
		this.labelOfPort=labelOfPort;
		this.portType=portType;
		this.portAlignment = alignment;
		////to define the height and width of in, out and unused port 
		setPortDimension();

		tooltipFigure = new TooltipFigure();
		setToolTip(tooltipFigure);

		Font font = new Font(Display.getDefault(),ELTFigureConstants.labelFont, 8, SWT.NORMAL);
		setFont(font);
		setForegroundColor(ColorConstants.black);
		//NOTE : to Suppress the component tooltip when user hover the mouse on Port 
		addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseMoved(MouseEvent arg0) {
			}

			@Override
			public void mouseHover(MouseEvent arg0) {
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
			}

			@Override
			public void mouseDragged(MouseEvent arg0) {
			}
		});

	}
	  //to define the height and width of in, out and unused port 
		private void setPortDimension() {
			//if("in".equalsIgnoreCase(portType)){
			if(PortAlignmentEnum.LEFT.equals(portAlignment)){
				getBounds().setSize(new Dimension(27,10));
			}
			else if(PortAlignmentEnum.RIGHT.equals(portAlignment)){
				getBounds().setSize(new Dimension(27,10));
			}
			else if(PortAlignmentEnum.BOTTOM.equals(portAlignment)){
				getBounds().setSize(new Dimension(24,16));
			}		
		}
	public Color getPortColor() {
		return portColor;
	}
	public TooltipFigure getTooltipFigure() {
		return tooltipFigure;
	}
	public String getLabelOfPort() {
		return labelOfPort;
	}
	public void setLabelOfPort(String label) {
		this.labelOfPort=label;
	}
	public boolean isWatched() {
		return isWatched;
	}
	public void setWatched(boolean isWatched) {
		this.isWatched = isWatched;
	}
	public String getPortType() {
		return portType;
	}
	
	public boolean isDisplayPortlabels() {
		return displayPortLabels;
	}
	public void setDisplayPortlabels(boolean toggleValue) {
		this.displayPortLabels = toggleValue;
	}
	@Override
	protected void paintFigure(Graphics graphics) {
		super.paintFigure(graphics);
		if(isWatched)
			setBackgroundColor(ELTColorConstants.WATCH_COLOR);
		Rectangle r = getBounds().getCopy();
		//if("in".equalsIgnoreCase(portType))
		if(PortAlignmentEnum.LEFT.equals(portAlignment)){
			graphics.fillRectangle(getBounds().getLocation().x-20, getBounds()
					.getLocation().y-1, r.width, r.height-2);
		}else if(PortAlignmentEnum.RIGHT.equals(portAlignment)){
			graphics.fillRectangle(getBounds().getLocation().x+20, getBounds()
					.getLocation().y-1, r.width, r.height-2);
		}else if(PortAlignmentEnum.BOTTOM.equals(portAlignment)){
			graphics.fillRectangle(getBounds().getLocation().x-16, getBounds()
					.getLocation().y+10, r.width,r.height);
		}
			
			
		if(isDisplayPortlabels()){
			//if("in".equalsIgnoreCase(portType))
			if(PortAlignmentEnum.LEFT.equals(portAlignment)){
				graphics.drawText(labelOfPort,new Point(getBounds().getLocation().x+8,getBounds()
						.getLocation().y-0.2));
			}else if(PortAlignmentEnum.RIGHT.equals(portAlignment)){
				graphics.drawText(labelOfPort,new Point(getBounds().getLocation().x,getBounds()
						.getLocation().y-0.2));
			}else if(PortAlignmentEnum.BOTTOM.equals(portAlignment)){
				graphics.drawText(labelOfPort,new Point(getBounds().getLocation().x,getBounds()
						.getLocation().y-0.2));
			}	
		}
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof PortFigure) {
			PortFigure pf = (PortFigure) o;
			if (pf.getParent() == this.getParent()
					&& pf.getTerminal() == this.getTerminal()

					)
				return true;
		}
		return false;
	}

	@Override
	public int hashCode() {
		int result = 17;
		int var1 = terminal.length();
		int sequence = terminal.charAt(terminal.length() - 1);
		int var2 = portColor.hashCode();
		result = 31 * result + var1;
		result = 31 * result + sequence;
		result = 31 * result + var2;

		return result;

	}

	public void selectPort() {
		if(!isWatched)
		setBackgroundColor(ELTColorConstants.COMPONENT_BORDER_SELECTED);
			//setBackgroundColor(new Color(null, 255, 51, 0));
	}

	public void deSelectPort() {
		if(!isWatched)
		setBackgroundColor(ELTColorConstants.COMPONENT_BORDER);
	}

	public void changeWatchColor(){
		setBackgroundColor(ELTColorConstants.WATCH_COLOR);
	}
	
	public void removeWatchColor(){
		setBackgroundColor(ELTColorConstants.COMPONENT_BORDER);
	}
	@Override
	public void validate() {
		super.validate();

		if (isValid())
			return;

	}

	public Rectangle getHandleBounds() {
		return getBounds().getCopy();
	}

	public String getTerminal() {
		return terminal;
	}

	public FixedConnectionAnchor getAnchor() {
		return anchor;
	}

	@Override
	public String toString() {

		return "\n******************************************" + "\nTerminal: "
				+ this.terminal + "\nParent Figure: " + this.getParent()
				+ "\nHashcode: " + hashCode()
				+ "\n******************************************\n";
	}

	public void setTooltipText(String tooltipText) {
		tooltipFigure.setMessage(tooltipText);
	}

	public TooltipFigure getToolTipFigure() {
		return tooltipFigure;
	}
	


}
