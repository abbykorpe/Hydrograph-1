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

package hydrograph.ui.propertywindow.widgets.dialogs;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TableItem;

/**
 * Field Compare Utility Class.
 * 
 * The class is used to compare available fields and selected partition keys for hive input
 *   and output components.
 * 
 * @author Bitwise
 * 
 */

public class FieldCompareUtility {
	
	public final static FieldCompareUtility INSTANCE = new FieldCompareUtility();
	
	private FieldCompareUtility(){
		
	}
	
	/**
	 * 
	 * Compares available fields and selected partition key fields for 
	 *   hive input and output components. 
	 * 
	 */
	public boolean compare_fields(TableItem[] items,List<String> sourceFieldsList)
	{
		ListIterator<String> t_itr,s_itr;
		boolean is_equal=true;
		
		List<String> target_fields = new ArrayList<String>();
		if(items.length > 0){
			for (TableItem tableItem : items){
				target_fields.add((String) tableItem.getText());
			}
		
		
		List<String> source_field = new ArrayList<String>(sourceFieldsList);
		
		t_itr=target_fields.listIterator(target_fields.size());
		s_itr = source_field.listIterator(source_field.size());
		
		
		while(t_itr.hasPrevious() & s_itr.hasPrevious()){
			if(StringUtils.equals(s_itr.previous(),t_itr.previous())){
				is_equal=true;
			}
			else{
				is_equal=false;
				break;
			}
		}
		}
		return is_equal;
		
	}
	
	/**
	 * 
	 * Message dialog to be displayed if compare_fields() method returns false. 
	 * 
	 */
	public int Message_Dialog()
	{
		String message="The partition fields should appear at the end of the schema in the same order. Please rearrange fields either in schema or in partition fields";
		
			MessageDialog dialog = new MessageDialog(Display.getCurrent().getActiveShell(), "Rearrange Fields", null,
			    message, MessageDialog.ERROR, new String[] { "Rearrange Schema",
			  "Rearrange Partition Fields" }, 0);
			int result = dialog.open();
			return result;
	}
	
	/**
	 * 
	 * Change color of selected partition key fields to red if compare_fields() method returns false.
	 * 
	 */
	public boolean compareAndChangeColor(TableItem[] items,List<String> sourceFieldsList){
		boolean check_field=compare_fields(items,sourceFieldsList);
		if(!check_field){
			Color color = Display.getCurrent().getSystemColor(SWT.COLOR_RED);
			for (TableItem tableItem : items){
				tableItem.setForeground(color);
			}
		}
		return check_field;
	}

}
