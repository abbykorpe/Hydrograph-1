package com.bitwise.app.tooltip.window;

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


public class PaletteToolTip extends Shell {
	private PaletteToolTip thisTestPaletteToolTip;
	private Composite toolTipComposite;
	private Label toolTipText;
	
	public Rectangle getFullToolTipBounds(){
		Point tooltipSize = toolTipComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		Rectangle bounds = new Rectangle(0, 0, tooltipSize.x, tooltipSize.y);
		return bounds;
	}
	
	public void setToolTipText(String text){
		toolTipText.setText(text.replace("\\n", "\n"));
		setSize(this.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}
	
	/**
	 * Create the shell.
	 * @param display
	 */
	public PaletteToolTip(Display display) {
		super(display, SWT.NONE);
		
		thisTestPaletteToolTip = this;
		
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.verticalSpacing = 0;
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		gridLayout.horizontalSpacing = 0;
		setLayout(gridLayout);
		
		toolTipComposite = new Composite(this, SWT.NONE);
		GridLayout gl_composite = new GridLayout(1, false);
		gl_composite.verticalSpacing = 0;
		gl_composite.marginWidth = 0;
		gl_composite.marginHeight = 0;
		gl_composite.horizontalSpacing = 0;
		toolTipComposite.setLayout(gl_composite);
		
		toolTipText = new Label(toolTipComposite, SWT.NONE);
		toolTipText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		toolTipText.setText("This is test description\n");
		toolTipComposite.setSize(toolTipComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		toolTipText.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
		
		toolTipText.addMouseTrackListener(new MouseTrackListener() {
			
			@Override
			public void mouseHover(MouseEvent e) {
				//Do Nothing
				
			}
			
			@Override
			public void mouseExit(MouseEvent e) {
				if(e.y < 0 || e.x < 0 || e.x > toolTipText.getBounds().width){
					thisTestPaletteToolTip.setVisible(false);
				}
			}
			
			@Override
			public void mouseEnter(MouseEvent e) {
				//Do Nothing
				
			}
		});
		
		Label label = new Label(toolTipComposite, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
		Link link = new Link(toolTipComposite, SWT.NONE);
		link.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		link.setText("<a>Help</a>");
		link.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
		link.addMouseTrackListener(new MouseTrackListener() {
			
			@Override
			public void mouseHover(MouseEvent e) {
				//Do Nothing
				
			}
			
			@Override
			public void mouseExit(MouseEvent e) {
				if(e.x < 0 || e.x > toolTipText.getBounds().width ||  e.y > toolTipText.getBounds().height){
					thisTestPaletteToolTip.setVisible(false);
				}
			}
			
			@Override
			public void mouseEnter(MouseEvent e) {
				//Do Nothing
				
			}
		});
		createContents();
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
