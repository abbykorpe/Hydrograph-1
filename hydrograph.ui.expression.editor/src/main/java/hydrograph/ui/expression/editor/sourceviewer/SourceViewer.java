package hydrograph.ui.expression.editor.sourceviewer;

import hydrograph.ui.expression.editor.jar.util.BuildExpressionEditorDataSturcture;
import hydrograph.ui.expression.editor.javasourceviewerconfiguration.HotKeyUtil;
import hydrograph.ui.expression.editor.javasourceviewerconfiguration.HydrographJavaSourceViewerConfiguration;
import hydrograph.ui.expression.editor.styletext.ExpressionEditorStyledText;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.text.java.hover.SourceViewerInformationControl;
import org.eclipse.jdt.ui.text.IColorManager;
import org.eclipse.jdt.ui.text.IJavaPartitions;
import org.eclipse.jdt.ui.text.JavaTextTools;
import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.FindReplaceDocumentAdapter;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.IPositionUpdater;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.AnnotationModel;
import org.eclipse.jface.text.source.CompositeRuler;
import org.eclipse.jface.text.source.IAnnotationAccess;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.jface.text.source.IOverviewRuler;
import org.eclipse.jface.text.source.ISharedTextColors;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.text.source.OverviewRuler;
import org.eclipse.jface.text.source.projection.ProjectionAnnotation;
import org.eclipse.jface.text.source.projection.ProjectionSupport;
import org.eclipse.jface.text.source.projection.ProjectionViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.VerifyKeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.editors.text.EditorsUI;
import org.eclipse.ui.internal.editors.text.EditorsPlugin;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.AbstractDecoratedTextEditorPreferenceConstants;
import org.eclipse.ui.texteditor.AnnotationPreference;
import org.eclipse.ui.texteditor.DefaultMarkerAnnotationAccess;
import org.eclipse.ui.texteditor.DefaultRangeIndicator;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.MarkerAnnotationPreferences;
import org.eclipse.ui.texteditor.SourceViewerDecorationSupport;

public class SourceViewer extends ProjectionViewer {
	   
	    private IRegion viewerStartRegion, viewerEndRegion;
	    private static int currentId = 0;
	    private String filename;
	    private SourceViewerDecorationSupport fSourceViewerDecorationSupport;
	    private static String className;
	    private ISharedTextColors sharedColors;
        private ICompilationUnit compilatioUnit;
	    private IOverviewRuler fOverviewRuler;
	    private MarkerAnnotationPreferences fAnnotationPreferences;
	    private IFile file = null; 
	    private IAnnotationAccess annotationAccess;
	    private int oldDocLength;
	    private boolean checkCode;
	    private static final String CURRENT_LINE = AbstractDecoratedTextEditorPreferenceConstants.EDITOR_CURRENT_LINE;
	    private static final String CURRENT_LINE_COLOR = AbstractDecoratedTextEditorPreferenceConstants.EDITOR_CURRENT_LINE_COLOR;
	    private static final String PRINT_MARGIN = AbstractDecoratedTextEditorPreferenceConstants.EDITOR_PRINT_MARGIN;
	    private static final String PRINT_MARGIN_COLOR = AbstractDecoratedTextEditorPreferenceConstants.EDITOR_PRINT_MARGIN_COLOR;
	    private static final String PRINT_MARGIN_COLUMN = AbstractDecoratedTextEditorPreferenceConstants.EDITOR_PRINT_MARGIN_COLUMN;
	    private  Map<ProjectionAnnotation, Position> oldAnnotations; 

	    public static final String VIEWER_CLASS_NAME = "CustomCompilationUnit"; 

	
	    public SourceViewer(Composite parent, IVerticalRuler verticalRuler, IOverviewRuler overviewRuler,
	            boolean showAnnotationsOverview, int styles, IAnnotationAccess annotationAccess, ISharedTextColors sharedColors,
	            boolean checkCode, IDocument document) 
	  {
	        super(parent, verticalRuler, overviewRuler, showAnnotationsOverview, SWT.BOLD);
	        int id = currentId++;
	        filename = VIEWER_CLASS_NAME + id++ + ".java";
	        this.sharedColors=sharedColors;
	        this.annotationAccess=annotationAccess;
	        this.fOverviewRuler=overviewRuler;
	        this.checkCode=checkCode; 
	        oldAnnotations= new HashMap<ProjectionAnnotation, Position>();
	        
	        try 
	        {
             IPackageFragmentRoot[] ipackageFragmentRootList=JavaCore.create(BuildExpressionEditorDataSturcture.INSTANCE.getCurrentProject()).getPackageFragmentRoots();
             IPackageFragmentRoot ipackageFragmentRoot=null;
             for(IPackageFragmentRoot tempIpackageFragmentRoot:ipackageFragmentRootList)
             {
        	   if(tempIpackageFragmentRoot.getKind()==IPackageFragmentRoot.K_SOURCE)
        	   {
        		   ipackageFragmentRoot=tempIpackageFragmentRoot;
        		   break;
        	   }   
             } 
              
             IPackageFragment compilationUnitPackage=   ipackageFragmentRoot.createPackageFragment("hydrograph.compilationunit", true, new NullProgressMonitor());
             compilatioUnit=   compilationUnitPackage.createCompilationUnit(filename,document.get(),true, new NullProgressMonitor());
            } 
	        catch (Exception e) {
	        	e.printStackTrace();
	        }
	        initializeViewer(document);
	        updateContents();
	  }

	 private void handleVerifyKeyPressed(VerifyEvent event) {
	        if (!event.doit) {
	            return;
	        }
	        try {
	            KeyStroke triggerKeyStroke = HotKeyUtil.getHotKey(HotKeyUtil.contentAssist);
	            if (triggerKeyStroke != null) {
	                
	                if ((triggerKeyStroke.getModifierKeys() == KeyStroke.NO_KEY && triggerKeyStroke.getNaturalKey() == event.character)
	                        ||
	                        (((triggerKeyStroke.getNaturalKey() == event.keyCode)
	                                || (Character.toLowerCase(triggerKeyStroke.getNaturalKey()) == event.keyCode) || (Character
	                                .toUpperCase(triggerKeyStroke.getNaturalKey()) == event.keyCode)) && ((triggerKeyStroke
	                                .getModifierKeys() & event.stateMask) == triggerKeyStroke.getModifierKeys()))) {
	                    doOperation(ISourceViewer.CONTENTASSIST_PROPOSALS);
	                    event.doit = false;
	                    return;
	                }
	            }
	        } 
	        catch (Exception e) {
	        }

	        if (event.stateMask != SWT.CTRL) {
	            return;
	        }

	        switch (event.character) {
	        case ' ':
	            doOperation(ISourceViewer.CONTENTASSIST_PROPOSALS);
	            event.doit = false;
	            break;

	        case '.':
	            doOperation(ISourceViewer.CONTENTASSIST_PROPOSALS);
	            event.doit = false;
	            break;
	        case 'y' - 'a' + 1:
	            doOperation(ITextOperationTarget.REDO);
	            event.doit = false;
	            break;
	        case 'z' - 'a' + 1:
	            doOperation(ITextOperationTarget.UNDO);
	            event.doit = false;
	            break;
	        default:
	        }
	    }
	
	
	
	 private SourceViewerDecorationSupport getSourceViewerDecorationSupport() {
	        if (fSourceViewerDecorationSupport == null) {
	            fSourceViewerDecorationSupport = new SourceViewerDecorationSupport(this, fOverviewRuler, annotationAccess,
	                    sharedColors);
	            configureSourceViewerDecorationSupport(fSourceViewerDecorationSupport);
	        }
	        return fSourceViewerDecorationSupport;
	    }
	
	 private void configureSourceViewerDecorationSupport(SourceViewerDecorationSupport support) {

	        Iterator e = fAnnotationPreferences.getAnnotationPreferences().iterator();
	        while (e.hasNext()) {
	            support.setAnnotationPreference((AnnotationPreference) e.next());
	        }

	        support.setCursorLinePainterPreferenceKeys(CURRENT_LINE, CURRENT_LINE_COLOR);
	        support.setMarginPainterPreferenceKeys(PRINT_MARGIN, PRINT_MARGIN_COLOR, PRINT_MARGIN_COLUMN);
	        support.setSymbolicFontName(JFaceResources.TEXT_FONT);
	    }

	
	private void initializeViewer(IDocument document) {
	        fAnnotationPreferences = EditorsPlugin.getDefault().getMarkerAnnotationPreferences();
            setDocument(document);
            installViewerConfiguration();
	        setEditable(true);
            Font font = JFaceResources.getFontRegistry().get(JFaceResources.TEXT_FONT);
	        getTextWidget().setFont(font);
	        getTextWidget().setData("document",document);
            Control control = getControl();
	        GridData data = new GridData(GridData.FILL_BOTH);
	        control.setLayoutData(data);
	                
//	        attachListnerToTextWidget(getTextWidget());
	        
	       prependVerifyKeyListener(new VerifyKeyListener() {

	            @Override
	            public void verifyKey(VerifyEvent event) {
	                handleVerifyKeyPressed(event);
	            }
	        });
	       addDocumentListener(document);
	    }

	
	
	 private void attachListnerToTextWidget(StyledText styledText) {

		 styledText.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				Document document=(Document) ((StyledText)e.widget).getData("document");
				System.out.println(document);
			}
		});
	}

	private void addDocumentListener(final IDocument document) {
	        final ExecutionLimiter documentReconcilerLimiter = new ExecutionLimiter(500, true) {

	            @Override
	            protected void execute(boolean isFinalExecution, Object data) {
	                if (isFinalExecution) {
	                    if (getControl() != null && !getControl().isDisposed()) {
	                        getControl().getDisplay().asyncExec(new Runnable() {

	                            @Override
	                            public void run() {
	                                updateContents();
	                                if (document.get().length() != 0) {
	                                   calculatePositions();
	                                }
	                                updateVisibleRegion();
	                            }
	                        });
	                    }
	                }
	            }
	        };

	        document.addDocumentListener(new IDocumentListener() {

	            @Override
	            public void documentAboutToBeChanged(DocumentEvent event) {
	                
	            }

	            @Override
	            public void documentChanged(DocumentEvent event) {
	                documentReconcilerLimiter.resetTimer();
	                documentReconcilerLimiter.startIfExecutable(true, null);
	            }
	        });
	        
	        document.addPositionUpdater(new IPositionUpdater() {
				
				@Override
				public void update(DocumentEvent event) {
					
				}
			});
	    }
	
	 
	    private void calculatePositions() {
	        if (hasSnippetsModifications()) {
	            final Map<ProjectionAnnotation, Position> annotations = getAllSnippetsAnnotations();

	            Display.getDefault().asyncExec(new Runnable() {

	                @Override
	                public void run() {
	                    if (!annotations.isEmpty() && getProjectionAnnotationModel() == null) {
	                        enableProjection();
	                    }
	                    if (getProjectionAnnotationModel() != null) {
	                        Annotation[] oldAnno = oldAnnotations.keySet().toArray(new Annotation[0]);
	                        getProjectionAnnotationModel().modifyAnnotations(oldAnno, annotations, null);
	                        oldAnnotations.clear();
	                        oldAnnotations.putAll(annotations);
	                        if (annotations.isEmpty()) {
	                            disableProjection();
	                        }
	                    }
	                }

	            });
	        }
	    }
	    
	    private boolean hasSnippetsModifications() {
	        IDocument document = getDocument();
	        if (document == null) {
	            return false;
	        }
	        int curNbAnnotations = oldAnnotations.size();
	        int actualNbAnnotations = 0;
	        int curOffset = 0;
	        FindReplaceDocumentAdapter frda = new FindReplaceDocumentAdapter(document);
	        try {
	            IRegion startRegion = frda.find(curOffset, "SNIPPET_START", true, false, false, false); //$NON-NLS-1$
	            while (startRegion != null && startRegion.getOffset() >= curOffset) {
	                int startLine = document.getLineOfOffset(startRegion.getOffset());
	                int startOffset = document.getLineOffset(startLine);
	                curOffset = startOffset + document.getLineLength(startLine);
	                IRegion endRegion = frda.find(startRegion.getOffset(), "SNIPPET_END", true, false, false, false); //$NON-NLS-1$
	                if (endRegion != null) {
	                    actualNbAnnotations++;
	                    int endLine = document.getLineOfOffset(endRegion.getOffset());
	                    int endOffset = document.getLineOffset(endLine);
	                    endOffset += document.getLineLength(endLine);
	                    curOffset = endOffset;
	                    boolean contains = false;
	                    String text = document.get(startOffset, endOffset - startOffset);
	                    for (ProjectionAnnotation annotation : oldAnnotations.keySet()) {
	                        Position pos = oldAnnotations.get(annotation);
	                        if (annotation.getText().equals(text) && (startOffset == pos.getOffset())) {
	                            contains = true;
	                        }
	                    }
	                    if (!contains) {
	                        return true;
	                    }
	                }
	                if (curOffset < document.getLength()) {
	                    startRegion = frda.find(curOffset, "SNIPPET_START", true, false, false, false); //$NON-NLS-1$
	                }
	            }

	        } catch (BadLocationException e) {
	            
	        }
	        if (curNbAnnotations != actualNbAnnotations) {
	            return true;
	        }
	        return false;
	    }
	    private Map<ProjectionAnnotation, Position> getAllSnippetsAnnotations() {
	        Map<ProjectionAnnotation, Position> annotations = new HashMap<ProjectionAnnotation, Position>();
	        IDocument document = getDocument();
	        int curOffset = 0;
	        FindReplaceDocumentAdapter frda = new FindReplaceDocumentAdapter(document);
	        try {
	            IRegion startRegion = frda.find(curOffset, "SNIPPET_START", true, false, false, false); //$NON-NLS-1$
	            while (startRegion != null && startRegion.getOffset() >= curOffset) {
	                int startLine = document.getLineOfOffset(startRegion.getOffset());
	                int startOffset = document.getLineOffset(startLine);
	                curOffset = startOffset + document.getLineLength(startLine);
	                IRegion endRegion = frda.find(startRegion.getOffset(), "SNIPPET_END", true, false, false, false); //$NON-NLS-1$
	                if (endRegion != null) {
	                    int endLine = document.getLineOfOffset(endRegion.getOffset());
	                    int endOffset = document.getLineOffset(endLine);
	                    endOffset += document.getLineLength(endLine);
	                    curOffset = endOffset;
	                    String text = document.get(startOffset, endOffset - startOffset);
	                    ProjectionAnnotation annotation = new ProjectionAnnotation(true);
	                    annotation.setText(text);
	                    annotation.setRangeIndication(true);
	                    annotations.put(annotation, new Position(startOffset, endOffset - startOffset));
	                }
	                if (curOffset < document.getLength()) {
	                    startRegion = frda.find(curOffset, "SNIPPET_START", true, false, false, false); //$NON-NLS-1$
	                }
	            }

	        } catch (BadLocationException e) {
	           
	        }
	        return annotations;
	    }
	   
	    private void initializeModel() {
	        getSourceViewerDecorationSupport().install(JavaPlugin.getDefault().getCombinedPreferenceStore());
	        this.setRangeIndicator(new DefaultRangeIndicator());

	        IAnnotationModel model;
	        IDocument document;
	        if (checkCode) {
	            IDocumentProvider provider = JavaPlugin.getDefault().getCompilationUnitDocumentProvider();
	            IEditorInput ei = new FileEditorInput(file);
	            try {
	                provider.connect(ei);
	            } catch (CoreException e) {
	                
	            }
	            document = provider.getDocument(ei);
	            model = provider.getAnnotationModel(ei);
	        } else {
	            model = new AnnotationModel();
	            document = getDocument();
	            model.connect(document);
	        }

	        if (document != null) {
	            setDocument(document, model);
	            showAnnotations(model != null );
	        }
	        
	        
	        ProjectionSupport projectionSupport = new ProjectionSupport(this, annotationAccess, sharedColors);
	        projectionSupport.addSummarizableAnnotationType("org.eclipse.ui.workbench.texteditor.error"); //$NON-NLS-1$
	        projectionSupport.addSummarizableAnnotationType("org.eclipse.ui.workbench.texteditor.warning"); //$NON-NLS-1$
	        projectionSupport.setHoverControlCreator(new IInformationControlCreator() {

	            @Override
	            public IInformationControl createInformationControl(Shell shell) {
	                return new SourceViewerInformationControl(shell, false, SWT.LEFT_TO_RIGHT, EditorsUI.getTooltipAffordanceString());
	            }
	        });
	        projectionSupport.setInformationPresenterControlCreator(new IInformationControlCreator() {

	            @Override
	            public IInformationControl createInformationControl(Shell shell) {
	                int shellStyle = SWT.RESIZE | SWT.TOOL | SWT.LEFT_TO_RIGHT;
	                int style = SWT.V_SCROLL | SWT.H_SCROLL;
	                return new SourceViewerInformationControl(shell, true, SWT.LEFT_TO_RIGHT, null);
	            }
	        });
	        projectionSupport.install();
	       
	    }
	    protected void updateVisibleRegion() {
	        if (this.getDocument() == null || this.viewerEndRegion == null || this.getVisibleDocument() == null) {
	            return;
	        }
	        final String docText = this.getDocument().get();
	        final int newDocLength = docText.length();
	        if (this.oldDocLength != newDocLength) {
	            final String visibleText = this.getVisibleDocument().get(); // get
	            final int newLength = visibleText.length();
	            final int newStart = newDocLength - newLength - this.viewerEndRegion.getLength();
	            setVisibleRegion(newStart, newLength);
	            this.oldDocLength = newDocLength;
	        }
	    }
		private void installViewerConfiguration() {
			JavaTextTools tools = JavaPlugin.getDefault().getJavaTextTools();
	        tools.setupJavaDocumentPartitioner(getDocument(), IJavaPartitions.JAVA_PARTITIONING);
	        IPreferenceStore store = JavaPlugin.getDefault().getCombinedPreferenceStore();
	        configure(new HydrographJavaSourceViewerConfiguration((IColorManager) sharedColors, store, this));
	    }

	 
	    protected StyledText createTextWidget(Composite parent, int styles) {
		 return new ExpressionEditorStyledText(parent, SWT.BORDER| SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL, this);
	    }
	 
	 @Override
	    public void setVisibleRegion(int start, int length) {
	        viewerStartRegion = new Region(0, start);
	       
	        if (getDocument() == null) {
	            return;
	        }
	        if (getDocument().getLength() > start) {
	            viewerEndRegion = new Region(start + 1 + length, getDocument().getLength() - start - length);
	        } else {
	            viewerEndRegion = new Region(start, 0);
	        }
	        super.setVisibleRegion(start, length);
	    }

	 
	 public IRegion getViewerRegion() {
	        if (viewerStartRegion == null) {
	            return new Region(0, getDocument().getLength());
	        }
	        return new Region(viewerStartRegion.getLength(), getDocument().getLength() - viewerStartRegion.getLength()
	                - viewerEndRegion.getLength());
	    }
	 
	private static SourceViewer initializeViewer(Composite composite, int styles, boolean checkCode, IDocument document,
	            int visibleOffset) 
	  {
		    IAnnotationAccess annotationAccess = new DefaultMarkerAnnotationAccess();
	        @SuppressWarnings("restriction")
			ISharedTextColors sharedColors = JavaPlugin.getDefault().getJavaTextTools().getColorManager();
	        IOverviewRuler overviewRuler = null;
	        IVerticalRuler verticalRuler = null;
	        if (checkCode) {
	            
	            @SuppressWarnings("restriction")
				Iterator<?> e = EditorsPlugin.getDefault().getMarkerAnnotationPreferences().getAnnotationPreferences().iterator();
	            while (e.hasNext()) {
	            	 overviewRuler = new OverviewRuler(annotationAccess, 12, sharedColors);
	                AnnotationPreference preference = (AnnotationPreference) e.next();
	                if (preference.contributesToHeader()) {
	                    overviewRuler.addHeaderAnnotationType(preference.getAnnotationType());
	                }
	            }
	        }
	        verticalRuler = new CompositeRuler(12);
	       SourceViewer viewer = new SourceViewer(composite, verticalRuler, overviewRuler, checkCode, styles,
	    		   annotationAccess, sharedColors, checkCode, document);

	        if (visibleOffset != -1) {
	            viewer.setVisibleRegion(visibleOffset, 0);
	        }
	        viewer.getControl().setParent(composite);
            return viewer;
	    }
	 private static String getImports() {
	        String imports = ""; 
	        imports += "\n";
	        imports += "import hydrograph.engine.transformation.standardfunctions.DateFunctions;\n"; 
	    	imports += "import java.text.ParseException;\n"; 
	        imports += "import java.text.SimpleDateFormat;\n"; 
	        imports += "import java.util.Date;\n"; 
	        imports += "import java.util.List;\n"; 
	        imports += "import java.math.BigDecimal;\n"; 
	        imports += "\n";
	        return imports;
	    }
	 
	 public static SourceViewer createViewerWithVariables(Composite composite, int iz) {
		   IDocument document = new Document();
		   StringBuffer buff = new StringBuffer();
	       buff.append("\n package hydrograph.engine.transformation.compilationunit;\n\n");
	       buff.append(getImports());
	       buff.append("public class " + VIEWER_CLASS_NAME + currentId + " {\n\n\n");
	       buff.append("\tpublic " + "String" +"myFunction(){\n");
		   buff.append("\t\treturn \n"); 
	       int length = buff.toString().length();
	       String defaultValue = " "; 
	       buff.append(defaultValue + "\n\n\n;\t\n}\n}");  
	       document.set(buff.toString());
	       return initializeViewer(composite,iz,true,document,length);
		}
	 
	 public void updateContents() {
	        if (getDocument() == null) {
	            return ;
	        }
	        InputStream codeStream = new ByteArrayInputStream(getDocument().get().getBytes());
	        try {
	           
	                file = BuildExpressionEditorDataSturcture.INSTANCE.getCurrentProject().getFile( "src/hydrograph/compilationunit"+ '/' + filename);
	                file.setContents(codeStream, true, false, null);
	                initializeModel();
	           }
	         catch(Exception e){
	        	 
	         } 
		}
	 
	 public static String getClassName() {
	        return className;
	    }
	 
	 public ICompilationUnit getCompilatioUnit() {
		 return compilatioUnit;
		}
	 
	 public void setCompilatioUnit(ICompilationUnit compilatioUnit) {
			this.compilatioUnit = compilatioUnit;
		}
		
	 
}

//IDocument document = new Document();
//StringBuffer buff = new StringBuffer();
//buff.append("\n package hydrograph.compilationunit;\n\n");
//buff.append(getImports());
//buff.append("public class " + VIEWER_CLASS_NAME + currentId + " {\n\n\n");
//buff.append("\tpublic " + "void" +"myFunction(){\n");
//buff.append("\n try { Object obj= \n ");
//int length = buff.toString().length();
//String defaultValue = " "; 
//buff.append("\n\n;} catch(Exception e){}");
//buff.append(defaultValue + "\n\n\n;\t\n}\n}");  
//document.set(buff.toString());
//return initializeViewer(composite,iz,true,document,length);
