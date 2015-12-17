package com.bitwise.app.propertywindow.widgets.joinproperty;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Item;

import com.bitwise.app.common.datastructure.property.JoinConfigProperty;
import com.bitwise.app.propertywindow.widgets.customwidgets.runtimeproperty.RunTimePropertyWizard;
import com.bitwise.app.propertywindow.widgets.customwidgets.runtimeproperty.RuntimeProperties;

public class JoinOutputCellModifier implements ICellModifier{
	private Viewer viewer;
	
	public JoinOutputCellModifier(Viewer viewer){
		this.viewer = viewer;
	}
	@Override
	public boolean canModify(Object element, String property) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Object getValue(Object element, String property) {
		JoinConfigProperty join = (JoinConfigProperty) element;
		
		if(ELTJoinConfigGrid.PORT_INDEX.equals(property)){
			return join.getPort_index();
		}
		else if(ELTJoinConfigGrid.JOIN_TYPE.equals(property)){
			return join.getJoin_type();
			
		}else if(ELTJoinConfigGrid.JOIN_KEY.equals(property)){
			return join.getJoin_key();
		}
			return null;
 
		
	}

	@Override
	public void modify(Object element, String property, Object value) {
		if(element instanceof Item){
			element = ((Item)element).getData();
			JoinConfigProperty join = (JoinConfigProperty)element;
			
			if(ELTJoinConfigGrid.PORT_INDEX.equals(property)){
				join.setPort_index((String)value);
			}
			else if(ELTJoinConfigGrid.JOIN_TYPE.equals(property)){
				join.setJoin_type((String)value);
			}
			else if(ELTJoinConfigGrid.JOIN_KEY.equals(property)){
				join.setJoin_key((Integer)value);
			}
			viewer.refresh();
		}
		
	}

}
