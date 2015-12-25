package com.bitwise.app.propertywindow.widgets.joinproperty;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.bitwise.app.common.util.Constants;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.ELTSWTWidgets;

public class ELTJoinConfigGrid extends Dialog {

	private Text text;
	private Text text_1;
	private String[] ITEMS = new String[]{Constants.INNER, Constants.OUTER, Constants.PARAMETER};
	ELTSWTWidgets eltswtWidgets = new ELTSWTWidgets();
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
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(2, false));
		//new Label(container, SWT.NONE);
		
		Label label =new Label(container, SWT.None);
		label.setBounds(0, 0, 100, 15);
		label.setText("Join Configuration");
		
		new Label(container, SWT.NONE);
		
		Composite composite = new Composite(container, SWT.BORDER);
		GridData gd_composite = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_composite.heightHint = 212;
		gd_composite.widthHint = 546;
		composite.setLayoutData(gd_composite);
		
		ScrolledComposite scrolledComposite = new ScrolledComposite(composite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setBounds(0, 0, 546, 212);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
		Composite composite_1 = new Composite(scrolledComposite, SWT.NONE);
		
		eltswtWidgets.textBoxWidget(composite_1, SWT.BORDER, new int[]{0, 2, 142, 23}, "PortIndex", false);
		eltswtWidgets.textBoxWidget(composite_1, SWT.BORDER, new int[]{144, 2, 190, 23}, "Join key(s)", false);
		eltswtWidgets.textBoxWidget(composite_1, SWT.BORDER, new int[]{337, 2, 205, 23}, "Join Type", false);
		scrolledComposite.setContent(composite_1);
		
		for(int i=0,j=0;i<10;i++,j++){
			createWidgets(composite_1, j);
			j=j+26;
		}
		
		scrolledComposite.setMinSize(composite_1.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		new Label(container, SWT.NONE);
		return container;
	}

	private Control createWidgets(Composite parent, int y){
		eltswtWidgets.textBoxWidget(parent, SWT.BORDER|SWT.READ_ONLY, new int[]{0, 28+y, 138, 23}, "Port Index", false);
		eltswtWidgets.textBoxWidget(parent, SWT.BORDER|SWT.READ_ONLY, new int[]{144, 28+y, 187, 23}, "Join key", true);
		Combo combo = eltswtWidgets.comboWidget(parent, SWT.READ_ONLY, new int[]{337, 28+y, 205, 23}, ITEMS, 0);
		combo.setEnabled(true);
		combo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				//for(int i=0;i<ITEMS.length;i++)
				System.out.println(event.widget);
				
			}
		});
		 
		return parent;
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
		return new Point(566, 351);
	}

	public static void main(String[] args) {
		Display dis =new Display();
		Shell sh = new Shell(dis);
		ELTJoinConfigGrid configGrid = new ELTJoinConfigGrid(sh);
		configGrid.open();
	}
}
