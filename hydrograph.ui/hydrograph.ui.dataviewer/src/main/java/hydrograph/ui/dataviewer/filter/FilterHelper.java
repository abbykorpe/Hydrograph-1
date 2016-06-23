package hydrograph.ui.dataviewer.filter;

import hydrograph.ui.dataviewer.filter.Condition;

import java.util.List;
import java.util.Map;
 
import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

public class FilterHelper {
	
	public static final FilterHelper INSTANCE = new FilterHelper();
	private FilterHelper() {
	}
	
	public  Listener getTextBoxListener(final List<Condition> conditionsList) {
		Listener listener = new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				Text text = (Text)event.widget;
				int index = (int) text.getData(FilterConditionsDialog.ROW_INDEX);
				Condition filterConditions = conditionsList.get(index);
				filterConditions.setValue(text.getText());
			}
		};
		return listener;
	}
	
	public SelectionListener getFieldNameSelectionListener(final TableViewer tableViewer, final List<Condition> conditionsList,
			final Map<String, String> fieldsAndTypes, final Map<String,String[]> typeBasedConditionalOperators) {
		SelectionListener listener = new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				Combo source = (Combo) e.getSource();
				int index = (int) source.getData(FilterConditionsDialog.ROW_INDEX);
				Condition filterConditions = conditionsList.get(index);
				String fieldName = source.getText();
				filterConditions.setFieldName(fieldName);
				
				if(StringUtils.isNotBlank(fieldName)){
					String fieldType = fieldsAndTypes.get(fieldName);
					TableItem item = tableViewer.getTable().getItem(index);
					Combo conditionalCombo = (Combo) item.getData(FilterConditionsDialog.CONDITIONAL_OPERATORS);
					conditionalCombo.setItems(typeBasedConditionalOperators.get(fieldType));
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		};
		return listener;
	}
	
	public SelectionListener getConditionalOperatorSelectionListener(final List<Condition> conditionsList) {
		SelectionListener listener = new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				Combo source = (Combo) e.getSource();
				int index = (int) source.getData(FilterConditionsDialog.ROW_INDEX);
				Condition filterConditions = conditionsList.get(index);
				filterConditions.setConditionalOperator(source.getText());
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		};
		return listener;
	}
	
	public SelectionListener getRelationalOpSelectionListener(final List<Condition> conditionsList) {
		SelectionListener listener = new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				Combo source = (Combo) e.getSource();
				int index = (int) source.getData(FilterConditionsDialog.ROW_INDEX);
				Condition filterConditions = conditionsList.get(index);
				filterConditions.setRelationalOperator(source.getText());
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		};
		return listener;
	}
	
	public SelectionListener addButtonListener(final TableViewer tableViewer, final List<Condition> conditionsList) {
		SelectionListener listener = new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				Button button = (Button) e.getSource();
				int index = (int) button.getData(FilterConditionsDialog.ROW_INDEX);
				conditionsList.add(index, new Condition());
				tableViewer.refresh();
			}
			
		/*	private void updateIndexes(TableItem tabItem, int index) {
				Button addButton = (Button) tabItem.getData(ADD);
				addButton.setData(ROW_INDEX, index);
				Button removeButton = (Button) tabItem.getData(REMOVE);
				removeButton.setData(ROW_INDEX, index);
				
				Combo conditionalCombo = (Combo) tabItem.getData(CONDITIONAL_OPERATORS);
				conditionalCombo.setData(ROW_INDEX, index);
				Combo fieldNamesCombo = (Combo) tabItem.getData(FIELD_NAMES);
				fieldNamesCombo.setData(ROW_INDEX, index);
				Combo relationalOperatorsCombo = (Combo) tabItem.getData(RELATIONAL_OPERATORS);
				relationalOperatorsCombo.setData(ROW_INDEX, index);
				
				Text text = (Text)tabItem.getData(VALUE_TEXT_BOX);
				text.setData(ROW_INDEX, index);
			}*/
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		};
		return listener;
	}

	public SelectionListener removeButtonListener(final TableViewer tableViewer, final List<Condition> conditionsList) {
		SelectionListener listener = new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(conditionsList.size() > 1){
					Button button = (Button) e.getSource();
					int removeIndex = (int) button.getData(FilterConditionsDialog.ROW_INDEX);
					
					conditionsList.remove(removeIndex);
				}
				tableViewer.refresh();
			}
			
			/*private void updateIndexes(TableItem tabItem, int index) {
				Button addButton = (Button) tabItem.getData(ADD);
				addButton.setData(ROW_INDEX, index);
				Button removeButton = (Button) tabItem.getData(REMOVE);
				removeButton.setData(ROW_INDEX, index);
				
				Combo conditionalCombo = (Combo) tabItem.getData(CONDITIONAL_OPERATORS);
				conditionalCombo.setData(ROW_INDEX, index);
				Combo fieldNamesCombo = (Combo) tabItem.getData(FIELD_NAMES);
				fieldNamesCombo.setData(ROW_INDEX, index);
				Combo relationalOperatorsCombo = (Combo) tabItem.getData(RELATIONAL_OPERATORS);
				relationalOperatorsCombo.setData(ROW_INDEX, index);
				
				Text text = (Text)tabItem.getData(VALUE_TEXT_BOX);
				text.setData(ROW_INDEX, index);
			}*/
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		};
		return listener;
	}
	
	public SelectionListener getOkButtonListener(final List<Condition> conditionsList) {
		SelectionListener listener = new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				StringBuffer buffer = new StringBuffer();
				for(int index = 0; index < conditionsList.size(); index++){
					Condition condition = conditionsList.get(index);
					if(index !=0){
						buffer.append(" ").append(condition.getRelationalOperator()).append(" ");
					}
					buffer.append(condition.getFieldName()).append(" ")
					.append(condition.getConditionalOperator()).append(" ")
					.append(condition.getValue());
				}
				
				System.out.println(buffer);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		};
		return listener;
	}
	

}
