package com.bitwise.app.propertywindow.widgets.joinproperty;

import java.util.ArrayList;
import java.util.List;

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

import com.bitwise.app.common.datastructure.property.LookupPropertyGrid;
import com.bitwise.app.common.util.Constants;
import com.bitwise.app.propertywindow.messages.Messages;
import com.bitwise.app.propertywindow.propertydialog.PropertyDialogButtonBar;
import com.bitwise.app.propertywindow.widgets.customwidgets.ELTJoinMapWidget;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.ELTSWTWidgets;
import com.bitwise.app.propertywindow.widgets.listeners.ELTVerifyTextListener;
import com.bitwise.app.propertywindow.widgets.listeners.ListenerHelper;
import com.bitwise.app.propertywindow.widgets.listeners.ListenerHelper.HelperType;
import com.bitwise.app.propertywindow.widgets.utility.WidgetUtility;

public class ELTJoinConfigGrid extends Dialog {

	private int inputPortValue = ELTJoinMapWidget.value;
	private Text keyText;
	private Label addButton, deleteButton;
	private Combo combo;
	private String[] ITEMS = new String[]{Constants.INNER, Constants.OUTER, Constants.PARAMETER};
	private List list = new ArrayList<>();
	private PropertyDialogButtonBar propertyDialogButtonBar;
	private ELTSWTWidgets eltswtWidgets = new ELTSWTWidgets();
	private LookupPropertyGrid lookupPropertyGrid;
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public ELTJoinConfigGrid(Shell parentShell, LookupPropertyGrid lookupPropertyGrid) {
		super(parentShell);
		this.lookupPropertyGrid = lookupPropertyGrid;
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(1, false));
		
		Composite composite_2 = new Composite(container, SWT.NONE);
		GridData gd_composite_2 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_composite_2.heightHint = 16;
		gd_composite_2.widthHint = 548;
		composite_2.setLayoutData(gd_composite_2);
		
		labelWidget(composite_2, SWT.None, new int[]{6, 0, 102, 15}, "Join configuration");
		labelWidget(composite_2, SWT.BORDER|SWT.CENTER, new int[]{496, 0, 20, 15}, "+");
		labelWidget(composite_2, SWT.BORDER|SWT.CENTER, new int[]{523, 0, 20, 15}, "*");
		
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
		
		for(int i=0,j=0;i<inputPortValue;i++,j++){
			eltswtWidgets.textBoxWidget(composite_1, SWT.BORDER, new int[]{0, 28+j, 142, 23}, "in"+i, false);
			
			if(lookupPropertyGrid!=null){
				if(lookupPropertyGrid.getJoinConfigText()!=null){
			keyText = eltswtWidgets.textBoxWidget(composite_1, SWT.BORDER|SWT.READ_ONLY, new int[]{144, 28+j, 190, 23}, (String)(list.get(i)), true);
			}
			}else{
				keyText = eltswtWidgets.textBoxWidget(composite_1, SWT.BORDER|SWT.READ_ONLY, new int[]{144, 28+j, 190, 23}, "", true);
			}
			ListenerHelper help = new ListenerHelper();
			help.put(HelperType.CONTROL_DECORATION, WidgetUtility.addDecorator(keyText, Messages.EMPTYFIELDMESSAGE));
			ELTVerifyTextListener listener = new ELTVerifyTextListener();
			keyText.addListener(SWT.Verify, listener.getListener(propertyDialogButtonBar, help, keyText));
			combo = eltswtWidgets.comboWidget(composite_1, SWT.BORDER, new int[]{337, 28+j, 205, 23}, ITEMS, 0);
			combo.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					String string = combo.getText();
					list.add(string);
					
				}
			}); 
		 
			j=j+26;
		}
		
		scrolledComposite.setMinSize(composite_1.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		return container;
	}

	
	public LookupPropertyGrid getLookupPropertyGrid(){
		LookupPropertyGrid lookupPropertyGrid = new LookupPropertyGrid();
		lookupPropertyGrid.setJoinConfigText(list);
		this.lookupPropertyGrid = lookupPropertyGrid;
		
		return lookupPropertyGrid;
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

	public Label labelWidget(Composite parent, int style, int[] bounds, String value){
		Label label = new Label(parent, style);
		label.setBounds(bounds[0], bounds[1], bounds[2], bounds[3]);
		label.setText(value);
		//label.setImage(image);
		
		return label;
	}
	
	public Text textBoxWidget(Composite parent, int style,int[] bounds, Object text, boolean value){
		Text textWidget = new Text(parent, style);
		textWidget.setBounds(bounds[0], bounds[1], bounds[2], bounds[3]);
		textWidget.setText((String)text);
		textWidget.setEditable(value);
		
		return textWidget;
	}
	public static void main(String[] args) {
		Display dis =new Display();
		Shell sh = new Shell(dis);
		ELTJoinConfigGrid configGrid = new ELTJoinConfigGrid(sh, null);
		configGrid.open();
	}
}
