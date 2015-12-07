package com.bitwise.app.tooltip.tooltips;

import java.nio.CharBuffer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;
import org.slf4j.Logger;

import com.bitwise.app.common.util.LogFactory;
import com.bitwise.app.tooltip.utils.ToolTipUtils;

/**
 * 
 * This is custom tooltip, to be shown in Component Palette
 * 
 * @author Bitwise
 *
 */

public class PaletteToolTip extends Shell {
	
	private static final Logger logger = LogFactory.INSTANCE.getLogger(PaletteToolTip.class);
	
	private PaletteToolTip thisTestPaletteToolTip;
	private Composite toolTipComposite;
	private Label toolTipText;
	private String blankCharacters;
	private Link helpLink;		
	
	/**
	 * 
	 * Computes height and width of tooltip and saves it in Point,
	 * where Point.x is width and Point.y is height
	 * 
	 * @return org.eclipse.swt.graphics.Point
	 */
	private Point getToolTipWidthHeight() {
		Point tooltipSize = toolTipComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		logger.debug("PaletteToolTip.getToolTipWidthHeight: Computed Width=" + tooltipSize.x + "Height=" + tooltipSize.y);
		return tooltipSize;
	}
	
	/**
	 * 
	 * Returns tooltip bounds
	 * 
	 * @return org.eclipse.swt.graphics.Rectangle
	 */
	public Rectangle getToolTipBounds(){
		Point tooltipSize = getToolTipWidthHeight();
		Rectangle bounds = new Rectangle(0, 0, tooltipSize.x, tooltipSize.y);
		logger.debug("PaletteToolTip.getToolTipBounds: tooltip bounds=" + bounds);
		return bounds;
	}

	/**
	 *  Add spaces before "Help" link
	 * 
	 * @param numberOfSpaces - to add
	 */
	private void addSpacesBeforeHelpLink(int numberOfSpaces){		
		blankCharacters=CharBuffer.allocate( numberOfSpaces + 5).toString().replace( '\0', ' ' );
		helpLink.setText(blankCharacters + "<a>help</a> ");
		logger.debug("PaletteToolTip.addSpacesBeforeHelpLink: added " + numberOfSpaces + " before \"Help\" link");
	}

	
	/**
	 * set tooltip text
	 * 
	 * @param text - text message to set
	 */
	public void setToolTipText(String text){
		String[] lines = ToolTipUtils.getLines(text);
		int maxLength = ToolTipUtils.getMaxLength(lines);		
		addSpacesBeforeHelpLink(maxLength);		
		toolTipText.setText(text.replace("\\n", "\n"));
		setSize(this.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		logger.debug("PaletteToolTip.setToolTipText: set tooltip text - " + text);
	}
	
	
	/**
	 * Create the shell.
	 * @param display
	 */
	public PaletteToolTip(Display display) {
		super(display, SWT.NONE);		
		thisTestPaletteToolTip = this;
				
		setLayoutToOuterMostContainer();
		addToolTipComposite();
		logger.debug("PaletteToolTip.PaletteToolTip: created tooltip box");
		
		addToolTipTextArea();
		logger.debug("PaletteToolTip.PaletteToolTip: added tooltip textarea");
		addSeparator();
		logger.debug("PaletteToolTip.PaletteToolTip: added separator");
		addHelpLink();
		logger.debug("PaletteToolTip.PaletteToolTip: added help link");
		
		addListenersToHideToolTip();
		logger.debug("PaletteToolTip.PaletteToolTip: tooltip hide listener");
		
		createContents();
	}

	/**
	 * Create link for "Help"
	 */
	private void addHelpLink() {
		helpLink = new Link(toolTipComposite, SWT.NONE);
		helpLink.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		helpLink.setText("<a>Help</a>");
		helpLink.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
	}

	/**
	 * Create a separator between tooltip text area and add "Help" link
	 */
	private void addSeparator() {
		Label label = new Label(toolTipComposite, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
	}

	/**
	 * Add tooltip text area - toolTipText(Label com.bitwise.app.tooltip.tooltips.PaletteToolTip.toolTipText)
	 */
	private void addToolTipTextArea() {
		toolTipText = new Label(toolTipComposite, SWT.NONE);
		toolTipText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		toolTipText.setText("This is test description\n");
		toolTipComposite.setSize(toolTipComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		toolTipText.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_INFO_BACKGROUND));	
	}

	private void addListenersToHideToolTip() {
		addToolTipTextAreaMouseTrackListener();
		addToolTipHelpLinkMouseTrackListener();
	}

	/**
	 * help link Mouse exit event to hide tooltip 
	 */
	private void addToolTipHelpLinkMouseTrackListener() {
		helpLink.addMouseTrackListener(new MouseTrackListener() {
			
			@Override
			public void mouseHover(MouseEvent e) {
				//Do Nothing
				
			}
			
			@Override
			public void mouseExit(MouseEvent e) {
				logger.debug("PaletteToolTip.addToolTipHelpLinkMouseTrackListener(mouseExit) :" +
						" (e.x , toolTipText.getBounds().width , e.y , toolTipText.getBounds().height)= " + 
						e.x + " , " + toolTipText.getBounds().width + e.x + " , " +  e.y + e.x + " , "
						+  toolTipText.getBounds().height );
				
				if(e.x < 0 || e.x > toolTipText.getBounds().width ||  e.y > toolTipText.getBounds().height){
					logger.debug("PaletteToolTip.addToolTipHelpLinkMouseTrackListener: hiding tooltip");
					thisTestPaletteToolTip.setVisible(false);
				}
			}
			
			@Override
			public void mouseEnter(MouseEvent e) {
				//Do Nothing
				
			}
		});
	}

	/**
	 * toolTipText area Mouse exit event to hide tooltip 
	 */
	private void addToolTipTextAreaMouseTrackListener() {
		toolTipText.addMouseTrackListener(new MouseTrackListener() {
			
			@Override
			public void mouseHover(MouseEvent e) {
				//Do Nothing
				
			}
			
			@Override
			public void mouseExit(MouseEvent e) {
				logger.debug("PaletteToolTip.addToolTipTextAreaMouseTrackListener(mouseExit) :" +
						" (e.x , toolTipText.getBounds().width , e.y , toolTipText.getBounds().height)= " + 
						e.x + " , " + toolTipText.getBounds().width + e.x + " , " +  e.y + e.x + " , "
						+  toolTipText.getBounds().height );
				
				if(e.y < 0 || e.x < 0 || e.x > toolTipText.getBounds().width){
					logger.debug("PaletteToolTip.addToolTipHelpLinkMouseTrackListener: hiding tooltip");
					thisTestPaletteToolTip.setVisible(false);
				}
			}
			
			@Override
			public void mouseEnter(MouseEvent e) {
				//Do Nothing
				
			}
		});
	}

	/**
	 * Add main tooltip composite
	 */
	private void addToolTipComposite() {
		toolTipComposite = new Composite(this, SWT.NONE);
		GridLayout gl_composite = new GridLayout(1, false);
		gl_composite.verticalSpacing = 0;
		gl_composite.marginWidth = 0;
		gl_composite.marginHeight = 0;
		gl_composite.horizontalSpacing = 0;
		toolTipComposite.setLayout(gl_composite);
	}

	/**
	 * Set Layout To Outer Most Container
	 */
	private void setLayoutToOuterMostContainer() {
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.verticalSpacing = 0;
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		gridLayout.horizontalSpacing = 0;
		setLayout(gridLayout);
	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText("SWT Application");
		setSize(this.computeSize(SWT.DEFAULT, SWT.DEFAULT));

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
