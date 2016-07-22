package hydrograph.ui.expression.editor.styletext;

import hydrograph.ui.expression.editor.color.manager.JavaLineStyler;
import hydrograph.ui.expression.editor.launcher.LaunchExpressionEditor;
import hydrograph.ui.expression.editor.sourceviewer.SourceViewer;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IRegion;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;


public class ExpressionEditorStyledText extends StyledText{

	private SourceViewer viewer;
    private  JavaLineStyler lineStyler;
    
    public ExpressionEditorStyledText(Composite parent, int style, SourceViewer viewer) {
        super(parent, style);
        lineStyler=new JavaLineStyler(LaunchExpressionEditor.tempData());
        setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		addLineStyleListener(lineStyler);
		setBackground(new Color(null , 255,255,255));
        this.viewer = viewer;
       
    }

    @Override
    public String getText() {
        IRegion region = viewer.getViewerRegion();
        try {
            return viewer.getDocument().get(region.getOffset(), region.getLength());
        } catch (BadLocationException e) {
            return super.getText();
        }
    }

    @Override
    public void setText(String text) {
        super.setText(text);
        if (viewer.getUndoManager() != null) {
            viewer.getUndoManager().reset();
        }
    }
	
}
