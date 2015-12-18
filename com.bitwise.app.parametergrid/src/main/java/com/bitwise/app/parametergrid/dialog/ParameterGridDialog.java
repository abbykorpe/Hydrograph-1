package com.bitwise.app.parametergrid.dialog;


import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.ColumnLayout;
import org.eclipse.ui.forms.widgets.ColumnLayoutData;

import com.bitwise.app.common.interfaces.parametergrid.DefaultGEFCanvas;
import com.bitwise.app.common.util.XMLConfigUtil;
import com.bitwise.app.parametergrid.textgridwidget.TextGrid;
import com.bitwise.app.parametergrid.textgridwidget.columns.TextGridColumnLayout;
import com.bitwise.app.parametergrid.textgridwidget.columns.TextGridRowLayout;
import com.bitwise.app.parametergrid.utils.ParameterFileManager;
import com.bitwise.app.propertywindow.messages.Messages;
import com.bitwise.app.propertywindow.widgets.utility.WidgetUtility;

public class ParameterGridDialog extends Dialog {

	private TextGrid textGrid;
	private boolean runGraph;
	private Button headerCompositeCheckBox;
	private Text paramterFileTextBox;
	private String parameterFile;
	private ControlDecoration txtDecorator;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public ParameterGridDialog(Shell parentShell) {
		super(parentShell);
		setShellStyle(SWT.CLOSE | SWT.RESIZE | SWT.TITLE | SWT.WRAP | SWT.APPLICATION_MODAL);
		runGraph=false;

	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		final Composite container = (Composite) super.createDialogArea(parent);
		ColumnLayout cl_container = new ColumnLayout();
		cl_container.verticalSpacing = 0;
		cl_container.maxNumColumns = 1;
		container.setLayout(cl_container);

		container.getShell().setText("Parameter Grid");

		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayout(new GridLayout(4, false));
		ColumnLayoutData cld_composite = new ColumnLayoutData();
		cld_composite.horizontalAlignment = ColumnLayoutData.RIGHT;
		cld_composite.heightHint = 30;
		composite.setLayoutData(cld_composite);

		addFileParameterFileBrowser(container);


		textGrid = new TextGrid(container);
		textGrid.clear();

		Label btnAdd = new Label(composite, SWT.NONE);
		GridData gd_btnAdd = getGridControlButtonLayout();
		btnAdd.setLayoutData(gd_btnAdd);
		btnAdd.addMouseListener(new MouseListener() {

			@Override
			public void mouseUp(MouseEvent e) {
				TextGridRowLayout textGridRowLayout = new TextGridRowLayout();
				textGridRowLayout.addColumn(new TextGridColumnLayout.Builder().columnWidth(90).editable(true).build());
				textGridRowLayout.addColumn(new TextGridColumnLayout.Builder().grabHorizantalAccessSpace(true).editable(true).build());
				Composite emptyRow = textGrid.addEmptyRow(textGridRowLayout);
				((Text)emptyRow.getChildren()[1]).setFocus();
				headerCompositeCheckBox.setSelection(false);
				((Button)emptyRow.getChildren()[0]).addSelectionListener(new SelectionAdapter() {

					@Override
					public void widgetSelected(SelectionEvent e) {
						// TODO Auto-generated method stub
						super.widgetSelected(e);
						changeHeaderCheckboxSelection();
					}

				});
				for(Composite row:textGrid.getGrid()){
					final Text text=((Text)row.getChildren()[1]);
					txtDecorator=WidgetUtility.addDecorator(text,Messages.CHARACTERSET);
					txtDecorator.hide();
					validateName(text);
					checkFocus(text); 
				}
				textGrid.refresh();
				textGrid.scrollToLastRow();
			}



			@Override
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});
		btnAdd.setText("");
		btnAdd.setImage(new Image(null, XMLConfigUtil.INSTANCE.CONFIG_FILES_PATH + "/icons/add.png"));

		Label btnRemove = new Label(composite, SWT.NONE);
		btnRemove.setLayoutData(getGridControlButtonLayout());
		btnRemove.addMouseListener(new MouseListener() {

			@Override
			public void mouseUp(MouseEvent e) {
				// TODO Auto-generated method stub
				textGrid.removeSelectedRows();
			}

			@Override
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});

		btnRemove.setText("");
		btnRemove.setImage(new Image(null, XMLConfigUtil.INSTANCE.CONFIG_FILES_PATH + "/icons/delete.png"));


		addGridHeader();

		if(getComponentCanvas().getCurrentParameterFilePath() ==null){
			if(getComponentCanvas().getParameterFile().startsWith("/")){
				parameterFile = getComponentCanvas().getParameterFile().replaceFirst("/", "");	
			}else{
				parameterFile = getComponentCanvas().getParameterFile();
			}

		}	
		else{
			parameterFile = getComponentCanvas().getCurrentParameterFilePath();
		}


		loadGridData();

		container.getParent().addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				textGrid.setHeight(container.getParent().getBounds().height - 150);
			}
		});





		return container;
	}

	private void checkFocus(final Text text) {
		text.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				txtDecorator.hide();
			}
			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
			}
		});
	}

	private void validateName(final Text text) {
		text.addVerifyListener(new VerifyListener() {
			@Override
			public void verifyText(VerifyEvent e) {
				txtDecorator.hide();
				String currentText = ((Text) e.widget).getText();
				String newName = (currentText.substring(0, e.start) + e.text + currentText.substring(e.end));
				Matcher matchName = Pattern.compile("[\\@]{1}[\\{]{1}[\\w]*[\\}]{1}||[\\w]*").matcher(newName);
				if(!matchName.matches()){
					text.setToolTipText(Messages.CHARACTERSET);
					txtDecorator=WidgetUtility.addDecorator(text,Messages.CHARACTERSET);
					txtDecorator.setDescriptionText(Messages.CHARACTERSET);
					txtDecorator.show();
					e.doit=false;
				}
				else
				{
					text.setToolTipText("");
					text.setMessage("");
					txtDecorator.hide();
				}
			}
		});
	}

	private void loadGridData() {
		ParameterFileManager parameterFileManager = new ParameterFileManager(parameterFile);
		
		Map<String, String> parameterMap=new LinkedHashMap<>();
		
		try {
			parameterMap = parameterFileManager.getParameterMap();
		} catch (IOException e) {
			//isValidParameterFile = false;
			e.printStackTrace();
		}

		if(parameterFile != null){
			if(parameterFile.contains(":")){
				if(parameterFile.startsWith("/"))
					paramterFileTextBox.setText(parameterFile.replaceFirst("/", ""));
				else
					paramterFileTextBox.setText(parameterFile);
			}
			else
			{
				paramterFileTextBox.setText(parameterFile);
			}	
		}


		//List of rows, where each row is list of columns
		List<List<String>> graphGridData =  new LinkedList<>();
		List<List<String>> externalGridData =  new LinkedList<>();
		List<String> canvasParameterList = getComponentCanvas().getLatestParameterList();
		for(String key: parameterMap.keySet()){
			List<String> rowData = new LinkedList<>();
			if(canvasParameterList.contains(key)){
				rowData.add(key);
				rowData.add(parameterMap.get(key));
				graphGridData.add(rowData);
			}else{
				rowData.add(key);
				rowData.add(parameterMap.get(key));
				externalGridData.add(rowData);
			}
		}



		for(List<String> row: graphGridData){
			TextGridRowLayout textGridRowLayout = new TextGridRowLayout();
			textGridRowLayout.addColumn(new TextGridColumnLayout.Builder().columnWidth(90).editable(false).build());
			textGridRowLayout.addColumn(new TextGridColumnLayout.Builder().grabHorizantalAccessSpace(true).editable(true).build());
			textGrid.addDisabledRow(textGridRowLayout, row);
		}

		for(List<String> row: externalGridData){
			TextGridRowLayout textGridRowLayout = new TextGridRowLayout();
			textGridRowLayout.addColumn(new TextGridColumnLayout.Builder().columnWidth(90).editable(true).build());
			textGridRowLayout.addColumn(new TextGridColumnLayout.Builder().grabHorizantalAccessSpace(true).editable(true).build());
			textGrid.addRow(textGridRowLayout, row);
		}


		textGrid.refresh();

		addGridRowSelectionListener();
	}


	private void loadParameterFile(){
		if(parameterFile == null){
			return;
		}
		textGrid.clear();

		//isValidParameterFile();

		loadGridData();
	}

	private List<String> getAllLines(String newParameterFile) throws Exception{
		Path filePath = Paths.get(newParameterFile, "");

		List<String> lines=null;
		Charset charset = Charset.forName(StandardCharsets.US_ASCII.name());

		lines = Files.readAllLines(filePath, charset);
		return lines;
	}
	private boolean isValidParameterFile(String newParameterFile) {
		boolean isValidParameterFile = true;
		try{
			//System.out.println(getAllLines().toString());
			List<String> fileContents = getAllLines(newParameterFile);
			for(String line: fileContents){
				if(!line.startsWith("#") && !line.isEmpty()){
					String[] keyvalue=line.split("=");
					if(keyvalue.length != 2){
						isValidParameterFile = false;
						MessageBox messageBox = new MessageBox(new Shell(), SWT.ICON_ERROR | SWT.OK );

						messageBox.setText("Error");
						messageBox.setMessage("Unable to load parameter file.\nPlease Check file contents.\nContent should be in key=value format" +
								"\nEach line should have single key=value pair");
						messageBox.open();
					}else{
						if(keyvalue[0].trim().contains(" ")){
							isValidParameterFile = false;
							MessageBox messageBox = new MessageBox(new Shell(), SWT.ICON_ERROR | SWT.OK );

							messageBox.setText("Error");
							messageBox.setMessage("Unable to load parameter file.\nPlease Check file contents.\nContent should be in key=value format" +
									"\nEach line should have single key=value pair" +
									"\nParameter key should not contain spaces");
							messageBox.open();
						}
					}
				}
			}

		}catch(Exception e){
			isValidParameterFile=false;
			MessageBox messageBox = new MessageBox(new Shell(), SWT.ICON_ERROR | SWT.OK );

			messageBox.setText("Error");
			messageBox.setMessage("Unable to load parameter file.\nPlease Check file format.\nExpected file format - US-ASCII");
			messageBox.open();
		}


		return isValidParameterFile;
	}

	private void addFileParameterFileBrowser(Composite container){
		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayout(new GridLayout(4, false));

		Label lblFile = new Label(composite, SWT.NONE);
		lblFile.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblFile.setText("Parameter File ");

		paramterFileTextBox = new Text(composite, SWT.BORDER);
		GridData gd_text = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_text.widthHint = 179;
		paramterFileTextBox.setLayoutData(gd_text);


		/*paramterFileTextBox.addKeyListener(new KeyListener() {

			@Override
			public void keyReleased(KeyEvent e) {
				if(e.keyCode == SWT.CR){
					parameterFile = paramterFileTextBox.getText();
					getComponentCanvas().setCurrentParameterFilePath(parameterFile);
					loadParameterFile();
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub

			}
		});*/

		final Button btnReloadParameterFile = new Button(composite, SWT.NONE);
		btnReloadParameterFile.setText("Reload File");
		btnReloadParameterFile.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
				parameterFile = paramterFileTextBox.getText();
				getComponentCanvas().setCurrentParameterFilePath(parameterFile);
				loadParameterFile();
			}

		});

		final Button btnNewButton = new Button(composite, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fd = new FileDialog(btnNewButton.getShell(), SWT.OPEN);
				fd.setText("Open");
				Path path = Paths.get(parameterFile.replaceFirst("/", ""));

				if(path.getParent() != null)
					fd.setFilterPath(path.getParent().toString());
				else{
					fd.setFilterPath(path.toAbsolutePath().getParent().toString());
				}


				String[] filterExt = { "*.properties" };
				fd.setFilterExtensions(filterExt);
				String selected = fd.open();

				if(selected!=null){
					if(isValidParameterFile(selected)){
						paramterFileTextBox.setText(selected);
					}
				}
				if(!parameterFile.equals(paramterFileTextBox.getText())){
					parameterFile=paramterFileTextBox.getText();
					getComponentCanvas().setCurrentParameterFilePath(parameterFile);
					loadParameterFile();	
				}
			}
		});
		btnNewButton.setText("...");
	}


	public String getParameterFile(){
		return parameterFile;
	}

	private void changeHeaderCheckboxSelection() {
		boolean allRowsSelected = true;
		for(Composite row:textGrid.getGrid()){
			if(((Button)((Composite)row).getChildren()[0]).isEnabled()){
				if(!((Button)row.getChildren()[0]).getSelection()){
					allRowsSelected = false;
					break;
				}
			}
		}

		if(allRowsSelected==true){
			headerCompositeCheckBox.setSelection(true);
		}else{
			headerCompositeCheckBox.setSelection(false);
		}
	}

	public void addGridRowSelectionListener(){
		for(Composite row: textGrid.getGrid()){

			//((Button)row.getChildren()[0]).

			((Button)row.getChildren()[0]).addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					// TODO Auto-generated method stub
					super.widgetSelected(e);
					changeHeaderCheckboxSelection();
				}
			});
		}
	}

	private void addGridHeader() {
		List<String> header= new LinkedList<>();
		header.add("Name");
		header.add("Value");
		TextGridRowLayout textGridRowLayout = new TextGridRowLayout();
		textGridRowLayout.addColumn(new TextGridColumnLayout.Builder().columnWidth(90).enabled(false).build());
		textGridRowLayout.addColumn(new TextGridColumnLayout.Builder().grabHorizantalAccessSpace(true).enabled(false).build());
		textGrid.addHeaderRow(textGridRowLayout, header);


		((Button)textGrid.getHeaderComposite().getChildren()[0]).addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				super.widgetSelected(e);

				if(((Button)textGrid.getHeaderComposite().getChildren()[0]).getSelection()){
					textGrid.selectAllRows();
				}else{
					textGrid.clearSelections();
				}
			}

		});

		headerCompositeCheckBox = ((Button)textGrid.getHeaderComposite().getChildren()[0]);
	}

	private GridData getGridControlButtonLayout() {
		GridData gridControlButtonLayout = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gridControlButtonLayout.widthHint = 21;
		gridControlButtonLayout.heightHint = 19;
		return gridControlButtonLayout;
	}
	
	private IPath getParameterFileIPath(){
		IFileEditorInput input = (IFileEditorInput)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor().getEditorInput() ;
	    IFile file = input.getFile();
	    IProject activeProject = file.getProject();
	    String activeProjectName = activeProject.getName();
	    
	    //java.nio.file.Path path= java.nio.file.Path(parameterFile)
	    Path filePath = Paths.get(parameterFile, "");
	    
	    IPath parameterFileIPath =new org.eclipse.core.runtime.Path("/"+activeProjectName+"/param/"+filePath.getFileName().toString().replace("job", "properties"));
	    
		return parameterFileIPath;
	}

	@Override
	protected void okPressed() {
		boolean error=false;


		//ParameterFileManager parameterFileManager = new ParameterFileManager(getComponentCanvas().getParameterFile());
		if(!paramterFileTextBox.getText().equals(parameterFile)){

			MessageBox messageBox = new MessageBox(new Shell(), SWT.ICON_ERROR | SWT.OK | SWT.CANCEL );	        
			messageBox.setText("Error");
			messageBox.setMessage("The file " + paramterFileTextBox.getText() + " is not loded in grid\n" +
					"Pressing OK will override the existing file if any \n"
					+ "Press Reload Button to load the file in grid");
			int buttonID = messageBox.open();
			if(buttonID == SWT.OK){
				parameterFile = paramterFileTextBox.getText();
				getComponentCanvas().setCurrentParameterFilePath(parameterFile);
			}else{
				return;
			}

		}

		textGrid.clearSelections();

		ParameterFileManager parameterFileManager = new ParameterFileManager(parameterFile);
		Map<String,String> dataMap = new LinkedHashMap<>();
		int rowId=0;
		for(List<String> row: textGrid.getData()){
			dataMap.put(row.get(0), row.get(1));
			if(row.get(0) == null || row.get(0).equals("")){
				textGrid.selectRow(rowId);
				error=true;
			}
			rowId++;
		}
		if(error == false){
			try {
				parameterFileManager.storeParameters(dataMap);
			} catch (IOException e) {
				MessageBox messageBox = new MessageBox(new Shell(), SWT.ICON_ERROR | SWT.OK );

				messageBox.setText("Error");
				messageBox.setMessage("Unable to store parameters to the file - \n" + e.getMessage());
				messageBox.open();
			}
			runGraph=true;
			super.okPressed();
		}else{
			MessageBox messageBox = new MessageBox(new Shell(), SWT.ICON_ERROR | SWT.OK | SWT.CANCEL );

			messageBox.setText("Error");
			messageBox.setMessage("Parameter name can not be blank..please correct selected rows");
			int buttonID = messageBox.open();
			switch(buttonID) {
			case SWT.OK:
				runGraph = true;
				break;
			case SWT.CANCEL:
				super.okPressed();
				break;
			}
		}		
		
		IFile file=ResourcesPlugin.getWorkspace().getRoot().getFile(getParameterFileIPath());
		try {
				file.refreshLocal(IResource.DEPTH_ZERO, null);
			} catch (CoreException e) {
				e.printStackTrace();
			}
	}



	@Override
	protected void cancelPressed() {
		// TODO Auto-generated method stub
		runGraph = false;
		super.cancelPressed();
	}

	public boolean canRunGraph(){
		return runGraph;
	}

	private DefaultGEFCanvas getComponentCanvas() {		
		if(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor() instanceof DefaultGEFCanvas)
			return (DefaultGEFCanvas) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		else
			return null;
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
		return new Point(450, 423);
	}

	public TextGrid getTextGrid() {
		return textGrid;
	}

}
