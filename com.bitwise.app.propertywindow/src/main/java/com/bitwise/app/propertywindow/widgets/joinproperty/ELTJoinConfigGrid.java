package com.bitwise.app.propertywindow.widgets.joinproperty;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import com.bitwise.app.common.datastructure.property.JoinConfigProperty;
import com.bitwise.app.propertywindow.messages.Messages;

public class ELTJoinConfigGrid extends Dialog {
	private Shell shell;
	private TableViewer tableViewer;
	public static final String PORT_INDEX = "Port Index";
	public static final String JOIN_TYPE = "Join Type";
	public static final String JOIN_KEY = "Join key";
	private String[] columns = new String[]{PORT_INDEX, JOIN_TYPE, JOIN_KEY};
	private List<JoinConfigProperty> propertyList = new ArrayList<>();
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public ELTJoinConfigGrid(Shell parentShell) {
		super(parentShell);
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	
	private void addProperty(TableViewer viewer){
		JoinConfigProperty property = new JoinConfigProperty();
		
		if(propertyList.size() != 0){
			property.setPort_index("");
			property.setJoin_key(Integer.valueOf("0"));
			property.setJoin_type("");
			propertyList.add(property);
		
			viewer.refresh();
		}else {
			property.setPort_index("");
			property.setJoin_key(Integer.valueOf("0"));
			property.setJoin_type("");
			propertyList.add(property);
			viewer.refresh();
			
		}
	}
	
	 
	@Override
	public Control createDialogArea(final Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(1, false));
		parent.addListener(SWT.Close, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				int style = SWT.APPLICATION_MODAL | SWT.YES | SWT.NO;
				MessageBox messageBox = new MessageBox((Shell)parent, style);
				messageBox.setText("Information"); //$NON-NLS-1$
				messageBox.setMessage(Messages.MessageBeforeClosingWindow);
				event.doit = messageBox.open() == SWT.YES;
				
			}
		});
		
		Composite composite = new Composite(container, SWT.BORDER);
		GridData gd_composite = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_composite.heightHint = 360;
		gd_composite.widthHint = 643;
		composite.setLayoutData(gd_composite);
		
		tableViewer = ELTLookupMapWizard.createTableViewer(composite, columns, new int[]{10, 42, 623, 308}, 206, new JoinContentProvider(), new ELTJoinLabelProvider());
		CellEditor[] editor = new CellEditor[3];
		editor[0] = new TextCellEditor(tableViewer.getTable());
		editor[1] = new TextCellEditor(tableViewer.getTable());
		editor[2] = new ComboBoxCellEditor(tableViewer.getTable(), JoinKey.INSTANCES,SWT.BORDER|SWT.READ_ONLY);
		 
		tableViewer.setCellModifier(new JoinOutputCellModifier(tableViewer));
		tableViewer.setCellEditors(editor);
		tableViewer.setInput(propertyList);
		
		tableViewer.getTable().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				addProperty(tableViewer);				 
			}

			@Override
			public void mouseDown(MouseEvent e) {

			}
		}); 
		
		
		Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setBounds(10, 21, 182, 15);
		lblNewLabel.setText("Join Configuration");
		
		Composite composite_1 = new Composite(composite, SWT.None);
		composite_1.setBounds(510, 14, 100, 26);
		createIcons(composite_1);

		return container;
	}
	
	
	private void createIcons(Composite parent){
		//String addIcon = XMLConfigUtil.INSTANCE.CONFIG_FILES_PATH + "/icons/add.png";
		createLabel(parent, SWT.BORDER | SWT.CENTER, new int[]{0, 0, 20, 20}, "+" );
		
		//String deleteIcon = XMLConfigUtil.INSTANCE.CONFIG_FILES_PATH + "/icons/delete.png";
		createLabel(parent, SWT.BORDER | SWT.CENTER, new int[]{20, 0, 20, 20}, "*" );
		
		//String upIcon = XMLConfigUtil.INSTANCE.CONFIG_FILES_PATH + "/icons/up.png";
		createLabel(parent, SWT.BORDER | SWT.CENTER, new int[]{40, 0, 20, 20}, "^" );
		
		//String downIcon = XMLConfigUtil.INSTANCE.CONFIG_FILES_PATH + "/icons/down.png";
		createLabel(parent, SWT.BORDER | SWT.CENTER, new int[]{60, 0, 20, 20}, "|" );
	}
	
	
	private Label createLabel(Composite parent, int style, int[] bounds, String value){
		Label label = new Label(parent, style);
		label.setBounds(bounds[0], bounds[1], bounds[2], bounds[3]);
		label.setText(value);
		//label.setImage(image);
		
		return label;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(667, 456);
	}
	
	public static void main(String[] args) {
		Display dis = new Display();
		Shell sh = new Shell(dis);
		ELTJoinConfigGrid grid = new ELTJoinConfigGrid(sh.getShell());
		grid.open();
	}
}
