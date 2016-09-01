package hydrograph.ui.expression.editor.javasourceviewerconfiguration;

import hydrograph.ui.expression.editor.sourceviewer.SourceViewer;

import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.text.ContentAssistPreference;
import org.eclipse.jdt.internal.ui.text.java.ContentAssistProcessor;
import org.eclipse.jdt.ui.text.IColorManager;
import org.eclipse.jdt.ui.text.IJavaPartitions;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jdt.ui.text.JavaSourceViewerConfiguration;

public class HydrographJavaSourceViewerConfiguration extends JavaSourceViewerConfiguration {
	 
	private SourceViewer viewer;

	public HydrographJavaSourceViewerConfiguration(IColorManager colorManager, IPreferenceStore preferenceStore,
            SourceViewer viewer) {
        super(colorManager, preferenceStore,null, IJavaPartitions.JAVA_PARTITIONING);
        this.viewer = viewer;
    }

	
	@Override
	    public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {
		
		   ContentAssistant assistant = new ContentAssistant();
	        assistant.setDocumentPartitioning(getConfiguredDocumentPartitioning(sourceViewer));

	       assistant.setRestoreCompletionProposalSize(getSettings("completion_proposal_size")); //$NON-NLS-1$

	        IContentAssistProcessor javaProcessor = new HydrographJavaCompletionProcessor(assistant, IDocument.DEFAULT_CONTENT_TYPE);
	        assistant.setContentAssistProcessor(javaProcessor, IDocument.DEFAULT_CONTENT_TYPE);

	        ContentAssistProcessor singleLineProcessor = new HydrographJavaCompletionProcessor(assistant,
	                IJavaPartitions.JAVA_SINGLE_LINE_COMMENT);
	        assistant.setContentAssistProcessor(singleLineProcessor, IJavaPartitions.JAVA_SINGLE_LINE_COMMENT);

	        ContentAssistProcessor stringProcessor = new HydrographJavaCompletionProcessor(assistant, IJavaPartitions.JAVA_STRING);
	        assistant.setContentAssistProcessor(stringProcessor, IJavaPartitions.JAVA_STRING);

	        ContentAssistProcessor multiLineProcessor = new HydrographJavaCompletionProcessor(assistant,
	                IJavaPartitions.JAVA_MULTI_LINE_COMMENT);
	        assistant.setContentAssistProcessor(multiLineProcessor, IJavaPartitions.JAVA_MULTI_LINE_COMMENT);

	        ContentAssistProcessor javadocProcessor = new HydrographJavaCompletionProcessor(assistant,
	         IJavaPartitions.JAVA_DOC);
	         assistant.setContentAssistProcessor(javadocProcessor, IJavaPartitions.JAVA_DOC);

	     
	        
	        ContentAssistPreference.configure(assistant, fPreferenceStore);

	        assistant.setContextInformationPopupOrientation(IContentAssistant.CONTEXT_INFO_ABOVE);
	        assistant.setInformationControlCreator(getInformationControlCreator(sourceViewer));

	        return assistant;
	    }

	  
	private IDialogSettings getSettings(String sectionName) {
        IDialogSettings settings = JavaPlugin.getDefault().getDialogSettings().getSection(sectionName);
        if (settings == null) {
            settings = JavaPlugin.getDefault().getDialogSettings().addNewSection(sectionName);
        }

        return settings;
    }
	
}
