package com.bitwise.app.graph.figure;

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
	private static boolean toggleValue;
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
			int totalPorts, String nameOfPort, String labelOfPort) {
		this.portColor = portColor;
		this.terminal = portType + portSeq;
		this.anchor = new FixedConnectionAnchor(this, portType, totalPorts,
				portSeq);
		this.labelOfPort=labelOfPort;
		this.portType=portType;
		getBounds().setSize(new Dimension(27,10));

		tooltipFigure = new TooltipFigure();
		setToolTip(tooltipFigure);

		Font font = new Font(Display.getDefault(),ELTFigureConstants.labelFont, 8, SWT.NORMAL);
		setFont(font);
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
	public Color getPortColor() {
		return portColor;
	}
	public TooltipFigure getTooltipFigure() {
		return tooltipFigure;
	}
	public String getLabelOfPort() {
		return labelOfPort;
	}
	public String getPortType() {
		return portType;
	}
	
	public boolean getToggleValue() {
		return toggleValue;
	}
	public void setToggleValue(boolean toggleValue) {
		this.toggleValue = toggleValue;
	}
	@Override
	protected void paintFigure(Graphics graphics) {
		super.paintFigure(graphics);
		Rectangle r = getBounds().getCopy();
		if("in".equalsIgnoreCase(portType))
		{
			graphics.fillRectangle(getBounds().getLocation().x-20, getBounds()
					.getLocation().y-1, r.width, r.height-2);
		}
		else
		{
			graphics.fillRectangle(getBounds().getLocation().x+20, getBounds()
					.getLocation().y-1, r.width, r.height-2);
		}
		if(getToggleValue())
		{
			if("in".equalsIgnoreCase(portType))
			{
				graphics.drawText(labelOfPort,new Point(getBounds().getLocation().x+8,getBounds()
						.getLocation().y-3));
			}
			else
			{
				graphics.drawText(labelOfPort,new Point(getBounds().getLocation().x,getBounds()
						.getLocation().y-3));
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
		setBackgroundColor(ELTColorConstants.blueBrandBoder);
	}

	public void deSelectPort() {
		setBackgroundColor(ELTColorConstants.componentBorder);
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
