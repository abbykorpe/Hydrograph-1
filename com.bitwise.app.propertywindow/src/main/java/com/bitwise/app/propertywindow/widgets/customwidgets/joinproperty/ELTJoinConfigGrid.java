package com.bitwise.app.propertywindow.widgets.customwidgets.joinproperty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.bitwise.app.common.datastructure.property.JoinConfigProperty;
import com.bitwise.app.common.util.Constants;
import com.bitwise.app.common.util.ImagePathConstant;
import com.bitwise.app.common.util.XMLConfigUtil;
import com.bitwise.app.propertywindow.propertydialog.PropertyDialogButtonBar;
import com.bitwise.app.propertywindow.widgets.customwidgets.ELTJoinWidget;
import com.bitwise.app.propertywindow.widgets.dialogs.FieldDialog;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.ELTSWTWidgets;

public class ELTJoinConfigGrid extends Dialog {

	private int inputPortValue = ELTJoinWidget.value;
	private List<String> ITEMS = Arrays.asList(Constants.TRUE, Constants.FALSE);
	private List<JoinConfigProperty> tempraryConfigPropertyList;
	private List<JoinConfigProperty> joinConfigPropertyList;
	private PropertyDialogButtonBar propertyDialogButtonBar;
	private ELTSWTWidgets eltswtWidgets = new ELTSWTWidgets();
	private Label editLableAsButton;
	private Map<String, List<String>> propagatedFiledNames;
	private String editImageIconPath = XMLConfigUtil.CONFIG_FILES_PATH + ImagePathConstant.EDIT_BUTTON;

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 * @param propertyDialogButtonBar
	 * @param validationStatus
	 */
	public ELTJoinConfigGrid(Shell parentShell, PropertyDialogButtonBar propertyDialogButtonBar,
			List<JoinConfigProperty> configProperty) {
		super(parentShell);

		this.joinConfigPropertyList = configProperty;
		this.propertyDialogButtonBar = propertyDialogButtonBar;
		copyAll(configProperty);
	}

	private List<JoinConfigProperty> copyAll(List<JoinConfigProperty> configProperty) {
		tempraryConfigPropertyList = new ArrayList<>();
		for (JoinConfigProperty joinConfigProperty : configProperty) {
			tempraryConfigPropertyList.add(new JoinConfigProperty(joinConfigProperty.getPortIndex(), joinConfigProperty
					.getJoinKey(), joinConfigProperty.getRecordRequired()));
		}
		return tempraryConfigPropertyList;
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.getShell().setText("Join Configuration");
		container.setLayout(new GridLayout(1, false));

		Composite composite_2 = new Composite(container, SWT.NONE);
		GridData gd_composite_2 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_composite_2.heightHint = 16;
		gd_composite_2.widthHint = 400;
		composite_2.setLayoutData(gd_composite_2);


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

		eltswtWidgets.textBoxWidget(composite_1, SWT.BORDER, new int[] { 0, 2, 142, 23 }, "PortIndex", false);
		eltswtWidgets.textBoxWidget(composite_1, SWT.BORDER, new int[] { 144, 2, 190, 23 }, "Join Key(s)", false);
		eltswtWidgets.textBoxWidget(composite_1, SWT.BORDER, new int[] { 337, 2, 205, 23 }, "Record Required", false);

		scrolledComposite.setContent(composite_1);

		if (tempraryConfigPropertyList != null && tempraryConfigPropertyList.isEmpty()) {
			for (int i = 0; i < inputPortValue; i++) {
				tempraryConfigPropertyList.add(new JoinConfigProperty());
			}
		}

		if (inputPortValue > tempraryConfigPropertyList.size()) {
			for (int i = tempraryConfigPropertyList.size(); i <= inputPortValue; i++) {
				tempraryConfigPropertyList.add(new JoinConfigProperty());
			}
		}

		for (int i = 0, j = 0; i < inputPortValue; i++, j++) {
			final JoinConfigProperty joinConfigProperty = tempraryConfigPropertyList.get(i);

			Text portIndex = eltswtWidgets.textBoxWidget(composite_1, SWT.BORDER, new int[] { 0, 28 + j, 142, 23 },
					"in" + i, false);
			joinConfigProperty.setPortIndex("in" + i);

			final Text keyText = eltswtWidgets.textBoxWidget(composite_1, SWT.BORDER | SWT.READ_ONLY, new int[] { 144,
					28 + j, 170, 23 }, "", false);
			keyText.setBackground(new Color(null, 255, 255, 255));


			Combo joinTypeCombo = eltswtWidgets.comboWidget(composite_1, SWT.BORDER,
					new int[] { 337, 28 + j, 205, 23 }, (String[]) ITEMS.toArray(), 0);
			joinTypeCombo.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					String comboText = ((Combo) e.widget).getText();
					joinConfigProperty.setRecordRequired(ITEMS.indexOf(comboText));
				}
			});
			j = j + 26;
			if (tempraryConfigPropertyList != null && !tempraryConfigPropertyList.isEmpty()) {
				populate(i, portIndex, keyText, joinTypeCombo);
			}

			editLableAsButton = new Label(composite_1, SWT.None);
			editLableAsButton.setBounds(317, 5 + j, 20, 20);
			editLableAsButton.setImage(new Image(null, editImageIconPath));
			editLableAsButton.addMouseListener(new MouseAdapter() {
				
				@Override
				public void mouseUp(MouseEvent e) {
					keyText.setText(launchDialogToSelectFields(keyText.getText(), joinConfigProperty.getPortIndex()));
					keyText.setToolTipText(keyText.getText());
					joinConfigProperty.setJoinKey(keyText.getText());
				}

			});
			keyText.setToolTipText(keyText.getText());
		}

		scrolledComposite.setMinSize(composite_1.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		return container;
	}

	public void populate(int i, Text portIndex, Text keyText, Combo joinTypeCombo) {
		portIndex.setText(tempraryConfigPropertyList.get(i).getPortIndex());
		keyText.setText(tempraryConfigPropertyList.get(i).getJoinKey());
		joinTypeCombo.select(tempraryConfigPropertyList.get(i).getRecordRequired());
	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(565, 320);
	}
	
	private String launchDialogToSelectFields(String availableValues, String socketId) {
		FieldDialog fieldDialog = new FieldDialog(new Shell(), propertyDialogButtonBar);
		fieldDialog.setPropertyFromCommaSepratedString(availableValues);
		fieldDialog.setSourceFieldsFromPropagatedSchema(propagatedFiledNames.get(socketId));
		fieldDialog.setComponentName(Constants.JOIN_KEYS_WINDOW_TITLE);
		fieldDialog.open();
		return fieldDialog.getResultAsCommaSeprated();
	}

	public void setPropagatedFieldProperty(Map<String, List<String>> propagatedFiledNames) {
		this.propagatedFiledNames = propagatedFiledNames;
	}

	@Override
	protected void okPressed() {
		joinConfigPropertyList.clear();
		joinConfigPropertyList.addAll(tempraryConfigPropertyList);
		super.okPressed();
	}

}
