package hydrograph.ui.propertywindow.widgets.listeners.grid;

import hydrograph.ui.datastructure.property.FixedWidthGridRow;
import hydrograph.ui.datastructure.property.GridRow;
import hydrograph.ui.datastructure.property.MixedSchemeGridRow;
import hydrograph.ui.propertywindow.factory.ListenerFactory;
import hydrograph.ui.propertywindow.messages.Messages;
import hydrograph.ui.propertywindow.propertydialog.PropertyDialogButtonBar;
import hydrograph.ui.propertywindow.widgets.listeners.ListenerHelper;
import hydrograph.ui.propertywindow.widgets.listeners.MouseActionListener;
import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Widget;

public class MouseHoverOnSchemaGridListener extends MouseActionListener{
	
	Table table=null;
	private Shell tip=null;
	private Label label=null;
	
	@Override
	public int getListenerType() {
		return SWT.MouseHover;
	}
	
	@Override
	public void mouseAction(
			PropertyDialogButtonBar propertyDialogButtonBar,
			ListenerHelper helpers,Event event,Widget... widgets) {
		
		  table=(Table)widgets[0];
		 
	      TableItem item = table.getItem(new Point(event.x, event.y));
         if (item != null && item.getForeground().getRed()==255) {
           if (tip != null && !tip.isDisposed())
           tip.dispose();
           tip = new Shell(table.getShell(), SWT.ON_TOP | SWT.TOOL);
           tip.setLayout(new FormLayout());
           label = new Label(tip, SWT.NONE);
           label.setForeground(table.getParent().getShell().getDisplay()
               .getSystemColor(SWT.COLOR_INFO_FOREGROUND));
           label.setBackground(table.getParent().getShell().getDisplay()
               .getSystemColor(SWT.COLOR_INFO_BACKGROUND));
           label.setData("_TABLEITEM", item);
           label.setText(setAppropriateToolTipMessage(item));
           label.addListener(SWT.MouseExit,ListenerFactory.Listners.DISPOSE_LISTENER
					.getListener().getListener(propertyDialogButtonBar, helpers, widgets) );
           label.addListener(SWT.MouseDown,ListenerFactory.Listners.DISPOSE_LISTENER
					.getListener().getListener(propertyDialogButtonBar, helpers, widgets));
           Point size = tip.computeSize(SWT.DEFAULT, SWT.DEFAULT);
           Point pt = table.toDisplay(event.x, event.y);
           tip.setBounds(pt.x, pt.y-20, size.x, size.y);
           tip.setVisible(true);
           table.setData("tip",tip);
           table.setData("label", label);
	}
}
	private String setAppropriateToolTipMessage(TableItem item)
	{
		GridRow basicSchemaGridRow=(GridRow)item.getData();
		if(StringUtils.equalsIgnoreCase(basicSchemaGridRow.getDataTypeValue(),"java.util.Date") && (StringUtils.isBlank(basicSchemaGridRow.getDateFormat())))
			return Messages.DATE_FORMAT_MUST_NOT_BE_BLANK;
		if((StringUtils.equalsIgnoreCase(basicSchemaGridRow.getDataTypeValue(), "java.math.BigDecimal")))
		{			 
			if((StringUtils.isBlank(basicSchemaGridRow.getPrecision())))
			{
		    return Messages.PRECISION_MUST_NOT_BE_BLANK;
		    }	
			else if(!(basicSchemaGridRow.getPrecision().matches("\\d+")))
			{
				return Messages.PRECISION_MUST_CONTAINS_NUMBER_ONLY_0_9;
			}	
			else if((StringUtils.isBlank(basicSchemaGridRow.getScale())))
			{
				 return Messages.SCALE_MUST_NOT_BE_BLANK;
			}
			else if(!(basicSchemaGridRow.getScale().matches("\\d+")))
			{
				return Messages.SCALE_MUST_CONTAINS_NUMBER_ONLY_0_9;
			}	
			else if(StringUtils.equalsIgnoreCase(basicSchemaGridRow.getScaleTypeValue(),"none"))
			{
				return Messages.SCALETYPE_MUST_NOT_BE_NONE;
			}	
		}
	    if(basicSchemaGridRow instanceof FixedWidthGridRow)
		{
			FixedWidthGridRow fixedWidthGridRow=(FixedWidthGridRow)basicSchemaGridRow;
			
			if(fixedWidthGridRow instanceof MixedSchemeGridRow)
			{
				if(StringUtils.isBlank(fixedWidthGridRow.getDelimiter()) && StringUtils.isBlank(fixedWidthGridRow.getLength()))
				{
					return Messages.LENGTH_OR_DELIMITER_MUST_NOT_BE_BLANK;
				}	
				else if(!(fixedWidthGridRow.getLength().matches("\\d+")))
				{
					return Messages.LENGTH_MUST_BE_AN_INTEGER_VALUE;
				}
				
				else if(StringUtils.isNotBlank(fixedWidthGridRow.getDelimiter()) && StringUtils.isNotBlank(fixedWidthGridRow.getLength()))
				{
					return Messages.ONLY_SPECIFY_LENGTH_OR_DELIMITER;
				}
				
				
			}
			else
			{
			if(StringUtils.isBlank(fixedWidthGridRow.getLength()))
			{
				return Messages.LENGTH_MUST_NOT_BE_BLANK;
			}	
			else if(!(fixedWidthGridRow.getLength().matches("\\d+")))
			{
				return Messages.LENGTH_MUST_BE_AN_INTEGER_VALUE;
			}
			
			}
		}	
			return "";
	}
}
