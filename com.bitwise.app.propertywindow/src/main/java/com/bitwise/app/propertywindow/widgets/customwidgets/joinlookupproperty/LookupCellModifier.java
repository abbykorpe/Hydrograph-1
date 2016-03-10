package com.bitwise.app.propertywindow.widgets.customwidgets.joinlookupproperty;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Item;

import com.bitwise.app.common.datastructure.property.LookupMapProperty;
import com.bitwise.app.propertywindow.widgets.customwidgets.lookupproperty.ELTLookupMapWizard;

/**
 * Custom Table Cell modifier class for lookup component
 * 
 * @author Bitwise
 *
 */
public class LookupCellModifier  implements ICellModifier{
	private Viewer viewer;
	
	public LookupCellModifier(Viewer viewer){
		this.viewer = viewer;
	}
	
	@Override
	public boolean canModify(Object element, String property) {
		return true;
	}

	@Override
	public Object getValue(Object element, String property) {
		LookupMapProperty mapProperty =(LookupMapProperty)element;
		
		 if (ELTLookupMapWizard.PROPERTY_NAME.equals(property))
		        return mapProperty.getSource_Field();
		    else if (ELTLookupMapWizard.PROPERTY_VALUE.equals(property))
		        return mapProperty.getOutput_Field();
		    else
		        return null;
	}

	@Override
	public void modify(Object element, String property, Object value) {
		if (element instanceof Item)
		      element = ((Item) element).getData();
		 
		LookupMapProperty mapp = (LookupMapProperty) element;
		
		    if (ELTLookupMapWizard.PROPERTY_NAME.equals(property)){
		      mapp.setSource_Field((String)value);
		    }  if (ELTLookupMapWizard.PROPERTY_VALUE.equals(property))
		        mapp.setOutput_Field((String)value);
		    
		    viewer.refresh();
		
	}

}
