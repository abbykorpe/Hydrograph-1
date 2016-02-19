package com.bitwise.app.propertywindow.widgets.filterproperty;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Item;

import com.bitwise.app.common.datastructure.property.FilterProperties;
import com.bitwise.app.common.util.Constants;
import com.bitwise.app.propertywindow.widgets.customwidgets.lookupproperty.ELTLookupMapWizard;


/**
 * The Class ELTCellModifier.
 * 
 * @author Bitwise
 */
public class ELTCellModifier implements ICellModifier{
	
	private Viewer viewer;
		
	/**
	 * Instantiates a new ELT cell modifier.
	 * 
	 * @param viewer
	 *            the viewer
	 */
	public ELTCellModifier(Viewer viewer) {
		this.viewer = viewer;
	}

	@Override
	public boolean canModify(Object element, String property) {
		return true;
	}

	@Override
	public Object getValue(Object element, String property) {
		FilterProperties filter = (FilterProperties) element;
		if(Constants.COMPONENT_NAME.equals(property))
		return filter.getPropertyname();
		else if(ELTLookupMapWizard.OPERATIONAL_INPUT_FIELD.equals(property)){
			return filter.getPropertyname();
		}
		return null;
	}

	@Override
	public void modify(Object element, String property, Object value) {
		if (element instanceof Item)
			element = ((Item) element).getData();

		FilterProperties p = (FilterProperties) element;
		
			if(Constants.COMPONENT_NAME.equals(property))
					p.setPropertyname((String)value);
			else if(ELTLookupMapWizard.OPERATIONAL_INPUT_FIELD.equals(property)){
				  p.setPropertyname((String)value);
			}
		// Force the viewer to refresh
		viewer.refresh();
		
	}

}
