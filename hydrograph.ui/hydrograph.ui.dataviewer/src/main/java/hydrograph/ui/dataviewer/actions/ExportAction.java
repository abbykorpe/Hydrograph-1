package hydrograph.ui.dataviewer.actions;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import hydrograph.ui.dataviewer.DebugDataViewer;
import hydrograph.ui.dataviewer.datastructures.ColumnData;
import hydrograph.ui.dataviewer.datastructures.RowData;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.opencsv.CSVWriter;

public class ExportAction extends Action {
	private DebugDataViewer debugDataViewer;
	
	
	public ExportAction(String menuItem) {
		super(menuItem);
		this.debugDataViewer = debugDataViewer;
	}
	
	public ExportAction(String menuItem, DebugDataViewer debugDataViewer) {
		super(menuItem);
		this.debugDataViewer = debugDataViewer;
	}
	@Override
	public void run() {
		Boolean header=true;
		TableViewer tableViewer=debugDataViewer.getTableViewer();
		TableItem[] items = tableViewer.getTable().getItems();
		int i=1;
		List<RowData> rowDatas=new ArrayList<RowData>();
		
		for(int index=0;index<items.length;index++){
			List<ColumnData> columnData=new ArrayList<ColumnData>();
			for(int j=0;j<tableViewer.getTable().getColumnCount();j++){
				columnData.add(new ColumnData(items[index].getText(j),null));
			}
			RowData rowData=new RowData( columnData,i);
			rowDatas.add(rowData);
			i++;
		
		}
		List<String[]> database = new ArrayList<String[]>();
		TableColumn[] temp=tableViewer.getTable().getColumns();
		if(header)
		{
			String[] tablecolumns = new String[tableViewer
					.getTable().getColumnCount()];
			for (int k = 0; k < temp.length; k++) {
				tablecolumns[k] = temp[k].getText();
			}

			database.add(tablecolumns);
		}
		for(RowData rowData:rowDatas)
		{
			List<ColumnData> columnDatas=rowData.getColumns();
			String[] eachrowdata=new String[tableViewer.getTable().getColumnCount()];

			for(int j=0;j<columnDatas.size();j++)
			{
				eachrowdata[j]=columnDatas.get(j).getValue();
			}
			database.add(eachrowdata);
			
		}

	    FileDialog fd=new FileDialog(Display.getDefault().getActiveShell(),SWT.SAVE);
	    System.out.println(fd);
	    fd.setText("Export File");
	    fd.setFileName("export_data.csv");
	    fd.setOverwrite(true);
        String path = fd.open();
        System.out.println(path);
		String filePath = path;
			CSVWriter writer;
			try {
				writer = new CSVWriter(new FileWriter(filePath));
				writer.writeAll(database,false); 
				writer.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
	}
	
}
