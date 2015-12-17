
package com.bitwise.app.graph.figure;

import org.eclipse.draw2d.AbstractConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ScalableFigure;
import org.eclipse.draw2d.geometry.Point;


/**
 * The Class FixedConnectionAnchor.
 * 
 * @author Bitwise
 */
public class FixedConnectionAnchor extends AbstractConnectionAnchor {

	private boolean allowMultipleLinks, linkMandatory;
	private String type;
	private int totalPortsOfThisType;
	private int sequence;

	

	/**
	 * Instantiates a new fixed connection anchor.
	 * 
	 * @param owner
	 *            the owner
	 * @param type
	 *            the type
	 * @param totalPortsOfThisType
	 *            the total ports of this type
	 * @param sequence
	 *            the sequence
	 */
	public FixedConnectionAnchor(IFigure owner, String type, int totalPortsOfThisType, int sequence) {
		super(owner);
		this.type=type;
		this.totalPortsOfThisType=totalPortsOfThisType;
		this.sequence=sequence;
	}

	/**
	 * @see org.eclipse.draw2d.AbstractConnectionAnchor#ancestorMoved(IFigure)
	 */
	@Override
	public void ancestorMoved(IFigure figure) {
		if (figure instanceof ScalableFigure)
			return;
		super.ancestorMoved(figure);
	}
	

	@Override
	public Point getLocation(Point arg0) {
		int xLocation =0, yLocation = 0;
		
			
		if(("in").equalsIgnoreCase(this.type)){
			 xLocation=getOwner().getBounds().getTopLeft().x-1;
			 yLocation=getOwner().getBounds().getTopLeft().y+4;
		}else if(("out").equalsIgnoreCase(this.type)){
			 xLocation=getOwner().getBounds().getTopRight().x-1;
			 yLocation=getOwner().getBounds().getTopRight().y+4;
		}else if(("unused").equalsIgnoreCase(this.type)){
			 xLocation=getOwner().getBounds().getBottomRight().x-4;
			 yLocation=getOwner().getBounds().getBottomRight().y-2;
		}
		
		Point point= new Point(xLocation, yLocation);
		getOwner().getParent().translateToAbsolute(point);
		return point;
	}
		
	public String getType() {
		return type;
	}

	public int getSequence() {
		return sequence;
	}

	public void setAllowMultipleLinks(boolean allowMultipleLinks) {
		this.allowMultipleLinks = allowMultipleLinks;
	}

	public void setLinkMandatory(boolean linkMandatory) {
		this.linkMandatory = linkMandatory;
	}

	public int getTotalPortsOfThisType() {
		return totalPortsOfThisType;
	}

	
	@Override
	public String toString() {
				
		 return "\n******************************************"+
				"\nOwner: "+getOwner()+
				"\nallowMultipleLinks: "+this.allowMultipleLinks+
				"\nlinkMandatory: "+this.linkMandatory+
				"\ntype: "+this.type+
				"\nsequence: "+this.sequence+
				"\ntotalPortsOfThisType: "+this.totalPortsOfThisType+
				"\n******************************************\n";
		 
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof FixedConnectionAnchor) {
			FixedConnectionAnchor fa = (FixedConnectionAnchor) o;
			
			if ( fa.getOwner() == this.getOwner() &&
					fa.getType().equals(this.getType()) &&
					fa.getTotalPortsOfThisType()==this.getTotalPortsOfThisType() &&
					fa.getSequence() == this.getSequence() &&
					fa.allowMultipleLinks == this.allowMultipleLinks &&
					fa.linkMandatory == this.linkMandatory
				)
				return true;
			
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int result = 17;
		int var1 = allowMultipleLinks?1:0;
		int var2 = linkMandatory?1:0;
		result = 31 * result + var1;
		result = 31 * result + var2;
		
		return result;
		
		
	}
	
}
