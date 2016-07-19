package hydrograph.ui.propertywindow.hivesparqueschema;

import hydrograph.ui.datastructure.property.BasicSchemaGridRow;
import hydrograph.ui.propertywindow.widgets.customwidgets.schema.ELTSchemaGridWidget;
import hydrograph.ui.propertywindow.widgets.customwidgets.schema.GeneralGridWidgetBuilder;
import hydrograph.ui.propertywindow.widgets.utility.DataType;
import hydrograph.ui.propertywindow.widgets.utility.SchemaRowValidation;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.TableItem;

/**
 * The Class HiveParqueSchemaCellModifier.
 * 
 * @author Bitwise
 *
 */
public class HiveParqueSchemaCellModifier implements ICellModifier{

	private Viewer viewer;
	private HiveParqueSchemaWidget hiveParqueSchemaWidget;
	   
	/**
	 * Instantiates a new schema grid cell modifier.
	 * 
	 * @param viewer
	 *            the viewer
	 */
	public HiveParqueSchemaCellModifier(HiveParqueSchemaWidget hiveParqueSchemaWidget,Viewer viewer) {
		this.viewer = viewer;
		this.hiveParqueSchemaWidget = hiveParqueSchemaWidget;
	}
	
	@Override
	public boolean canModify(Object element, String property) {
		// Allow editing of all values
		BasicSchemaGridRow basicSchemaGridRow = (BasicSchemaGridRow) element;
		if (ELTSchemaGridWidget.DATEFORMAT.equals(property))
		{
			if(DataType.DATE_CLASS.equals(basicSchemaGridRow.getDataTypeValue()))
				return true;
			else 
				return false; 	
		}
		if (ELTSchemaGridWidget.SCALE.equals(property))
		{
			if(DataType.BIGDECIMAL_CLASS.getValue().equals(basicSchemaGridRow.getDataTypeValue()))
				return true;
			else {
				return false; 	
			}
		}

		if (ELTSchemaGridWidget.SCALE_TYPE.equals(property))
		{
			if(DataType.BIGDECIMAL_CLASS.getValue().equals(basicSchemaGridRow.getDataTypeValue()))
				return true;
			else {
				return false; 	
			}
		}

		if (ELTSchemaGridWidget.PRECISION.equals(property))
		{
			if(DataType.BIGDECIMAL_CLASS.getValue().equals(basicSchemaGridRow.getDataTypeValue()))
				return true;
			else {
				return false; 	
			}
		}

		return true;
	}

	@Override
	public Object getValue(Object element, String property) {
		BasicSchemaGridRow basicSchemaGridRow = (BasicSchemaGridRow) element;
		if (ELTSchemaGridWidget.FIELDNAME.equals(property))
			return basicSchemaGridRow.getFieldName();
		else if (ELTSchemaGridWidget.DATATYPE.equals(property))
			return basicSchemaGridRow.getDataType();
		else if (ELTSchemaGridWidget.DATEFORMAT.equals(property))
			return String.valueOf(basicSchemaGridRow.getDateFormat());
		else if (ELTSchemaGridWidget.PRECISION.equals(property))
			return basicSchemaGridRow.getPrecision();
		else if (ELTSchemaGridWidget.SCALE.equals(property))
			return String.valueOf(basicSchemaGridRow.getScale());
		else if (ELTSchemaGridWidget.SCALE_TYPE.equals(property))
			return basicSchemaGridRow.getScaleType();
		else if (ELTSchemaGridWidget.FIELD_DESCRIPTION.equals(property))
			return basicSchemaGridRow.getDescription();

		else
			return null;
	}

	@Override
	public void modify(Object element, String property, Object value) {
		Object object=null;
		if (element instanceof Item)
		{
			object = ((Item) element).getData();
			
		}
		BasicSchemaGridRow basicSchemaGridRow = (BasicSchemaGridRow) object;
		if (ELTSchemaGridWidget.FIELDNAME.equals(property))
			basicSchemaGridRow.setFieldName(((String) value).trim());
		else if (ELTSchemaGridWidget.DATATYPE.equals(property)){
			if(StringUtils.equals(DataType.BIGDECIMAL_CLASS.getValue(), GeneralGridWidgetBuilder.getDataTypeValue()[(Integer)value]))
			{
				basicSchemaGridRow.setScaleType(2); 
				basicSchemaGridRow.setScaleTypeValue(GeneralGridWidgetBuilder.getScaleTypeValue()[2]);
				basicSchemaGridRow.setScale(String.valueOf(1));
			}
			basicSchemaGridRow.setDataType((Integer)value);
			basicSchemaGridRow.setDataTypeValue(GeneralGridWidgetBuilder.getDataTypeValue()[(Integer)value]);
		}
		else if (ELTSchemaGridWidget.DATEFORMAT.equals(property))
		{
			basicSchemaGridRow.setDateFormat( ((String) value).trim()); 
		}	
		else if (ELTSchemaGridWidget.PRECISION.equals(property))
		{	
			basicSchemaGridRow.setPrecision(((String) value).trim()); 
		}
		else if (ELTSchemaGridWidget.SCALE.equals(property))
			{
			 basicSchemaGridRow.setScale(((String) value).trim());
			}
			
		else if (ELTSchemaGridWidget.SCALE_TYPE.equals(property)){
			basicSchemaGridRow.setScaleType((Integer)value); 
			basicSchemaGridRow.setScaleTypeValue(GeneralGridWidgetBuilder.getScaleTypeValue()[(Integer)value]);
			
		}
		else if (ELTSchemaGridWidget.FIELD_DESCRIPTION.equals(property))
			basicSchemaGridRow.setDescription(((String) value).trim());


		if (isResetNeeded(basicSchemaGridRow, property)){
			basicSchemaGridRow.setScale("");
			basicSchemaGridRow.setScaleTypeValue(GeneralGridWidgetBuilder.getScaleTypeValue()[0]);
			basicSchemaGridRow.setScaleType(0);
			basicSchemaGridRow.setPrecision("");
		}
		resetDateFormat(basicSchemaGridRow, property);
		viewer.refresh();
		SchemaRowValidation.INSTANCE.highlightInvalidRowWithRedColor(basicSchemaGridRow, (TableItem)element, hiveParqueSchemaWidget.getTable(), hiveParqueSchemaWidget.getComponentType());
		hiveParqueSchemaWidget.showHideErrorSymbol(hiveParqueSchemaWidget.isWidgetValid());
	}
	
	private void resetDateFormat(BasicSchemaGridRow row, String property){
		if(ELTSchemaGridWidget.DATATYPE.equals(property) && StringUtils.isNotBlank(row.getDataTypeValue())){
			if(!(DataType.DATE_CLASS.equals(row.getDataTypeValue()))){
				row.setDateFormat("");
			}

		}
	}


	private boolean isResetNeeded(BasicSchemaGridRow basicSchemaGridRow, String property) {
		if(ELTSchemaGridWidget.DATATYPE.equals(property) && StringUtils.isNotBlank(basicSchemaGridRow.getDataTypeValue())){
			if(DataType.INTEGER_CLASS.equals(basicSchemaGridRow.getDataTypeValue()) 
					||DataType.LONG_CLASS.equals(basicSchemaGridRow.getDataTypeValue())
					||DataType.STRING_CLASS.equals(basicSchemaGridRow.getDataTypeValue())
					||DataType.SHORT_CLASS.equals(basicSchemaGridRow.getDataTypeValue())
					||DataType.BOOLEAN_CLASS.equals(basicSchemaGridRow.getDataTypeValue())
					||DataType.FLOAT_CLASS.equals(basicSchemaGridRow.getDataTypeValue())
					||DataType.DOUBLE_CLASS.equals(basicSchemaGridRow.getDataTypeValue())
					||DataType.DATE_CLASS.equals(basicSchemaGridRow.getDataTypeValue())){
				return true;
			}	
		}
		return false;
	}


}
