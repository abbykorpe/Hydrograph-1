package hydrograph.ui.expression.editor.styletext;

import hydrograph.ui.expression.editor.Constants;
import hydrograph.ui.expression.editor.Messages;
import hydrograph.ui.expression.editor.sourceviewer.SourceViewer;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IRegion;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;


public class ExpressionEditorStyledText extends StyledText{

	private SourceViewer viewer;
	public static final String RETURN_STATEMENT = "\t\treturn \n";
	
    public ExpressionEditorStyledText(Composite parent, int style, SourceViewer viewer) {
        super(parent, style);
        setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		setBackground(new Color(null , 255,255,255));
        this.viewer = viewer;
       
    }

    @Override
    public String getText() {
        IRegion region = viewer.getViewerRegion();
        String text=Constants.EMPTY_STRING;
        try {
            text = viewer.getDocument().get(region.getOffset(), region.getLength());
        } catch (BadLocationException e) {
            text = super.getText();
        }
        return getExpressionText(text);
    }

    private String getExpressionText(String expressionText) {
		StringBuffer buffer=new StringBuffer(expressionText);
		int startIndex=buffer.indexOf(RETURN_STATEMENT);
		if(startIndex>-1){
			buffer.delete(0, startIndex);
			buffer.delete(0, buffer.indexOf("\n"));
		}
		return StringUtils.trim(buffer.toString());
	}
    
    @Override
    public void setText(String text) {
    	super.setText(text);
        if (viewer.getUndoManager() != null) {
            viewer.getUndoManager().reset();
        }
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.swt.custom.StyledText#insert(java.lang.String)
     */
    @Override
	public void insert(String string) {
		if (!StringUtils.startsWith(StringUtils.trim(string),Messages.CANNOT_SEARCH_INPUT_STRING)) {
			super.insert(string);
			this.setFocus();
			this.setSelection(this.getText().length() + 100);
		}
	}

}
