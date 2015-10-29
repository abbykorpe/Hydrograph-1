package com.bitwise.app.graph.editor;


import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.ScrollPaneSolver;
import org.eclipse.draw2d.UpdateListener;
import org.eclipse.draw2d.UpdateManager;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.slf4j.Logger;

import com.bitwise.app.common.util.LogFactory;


public class CustomFigureCanvas extends FigureCanvas{
	private int containerHeight = 0;
    protected Control containerForSearchTextBox;
    private static Logger logger = LogFactory.INSTANCE.getLogger(CustomFigureCanvas.class);

    static final int APPLY_STYLES =  SWT.V_SCROLL | SWT.H_SCROLL|SWT.NO_REDRAW_RESIZE | SWT.NO_BACKGROUND;

 
    //call package private method using reflection method
    private static Method reflectionMethod;

  
    //access package private field using reflection field
    private static Field reflectionField;

  
   /* reflection is using because of some limitations of figurecanvas class 
    access method in static form so that it can be avoided to do it every time these are called so that performance can be improved*/
    
    static {
        try {
            reflectionField = UpdateManager.class.getDeclaredField("listeners"); //$NON-NLS-1$
            reflectionField.setAccessible(true);
            reflectionMethod = LightweightSystem.class.getDeclaredMethod("setIgnoreResize", boolean.class); //$NON-NLS-1$
            reflectionMethod.setAccessible(true);
        } catch (SecurityException e) {
        	logger.error(e.getMessage());
            throwExceptionWhenReflectionIsFailed(e);
        } catch (NoSuchMethodException e) {
        	logger.error(e.getMessage());
            throwExceptionWhenReflectionIsFailed(e);
        } catch (NoSuchFieldException e) {
        	logger.error(e.getMessage());
            throwExceptionWhenReflectionIsFailed(e);
        }
    }

    public CustomFigureCanvas(Composite parent, LightweightSystem lws, CustomPaletteViewer toolViewer, PaletteRoot paletteRoot,ETLGraphicalEditor editor) {
        this(SWT.DOUBLE_BUFFERED, parent, lws, toolViewer, paletteRoot,editor);
    }

    public CustomFigureCanvas(int style, Composite parent, LightweightSystem lws, CustomPaletteViewer toolViewer, PaletteRoot paletteRoot,ETLGraphicalEditor editor) {
        super(style | APPLY_STYLES, parent, lws);
            containerForSearchTextBox = toolViewer.creatSearchTextBox(this, paletteRoot,editor);
        if (containerForSearchTextBox != null && toolViewer != null) {
            org.eclipse.swt.graphics.Point bounds = containerForSearchTextBox.computeSize(SWT.DEFAULT, SWT.DEFAULT);
            if (containerHeight < bounds.y) {
                containerHeight = bounds.y;
            }
        }
        
        customLayoutViewPort();
    }

  
     // cannot override  Layout Viewport in the FigureCanvas so make custom layout viewport
     
    private void customLayoutViewPort() {
        try {
            Object nullArray = Array.newInstance(UpdateListener.class, 0);
            reflectionField.set(getLightweightSystem().getUpdateManager(), nullArray);
             getLightweightSystem().getUpdateManager().addUpdateListener(new UpdateListener() {

                @Override
                public void notifyPainting(Rectangle damage, java.util.Map dirtyRegions) {
                    if (containerForSearchTextBox != null) {
                        containerForSearchTextBox.setBounds(0, 0, containerForSearchTextBox.getBounds().width, containerHeight);
                    }
                }

                @Override
                public void notifyValidating() {
                    if (!isDisposed()) {
                        layoutViewport();
                    }
                }
            });
        } catch (IllegalAccessException e) {
        	logger.error(e.getMessage());
            throwExceptionWhenReflectionIsFailed(e);
        }
    }

  
    private static void throwExceptionWhenReflectionIsFailed(Exception e) {
             throw new RuntimeException(e);

    }

    protected void layoutViewport() {
        ScrollPaneSolver.Result result;
        int viewPortY = 0;
        if (containerForSearchTextBox != null) {
            viewPortY = containerHeight;
        }
        result = ScrollPaneSolver.solve(new Rectangle(getBounds()).setLocation(0, viewPortY), getViewport(),
                getHorizontalScrollBarVisibility(), getVerticalScrollBarVisibility(), computeTrim(0, 0, 0, 0).width,
                computeTrim(0, 0, 0, 0).height);
        try {
           
            reflectionMethod.invoke(getLightweightSystem(), true);
        } catch (IllegalArgumentException e) {
        	logger.error(e.getMessage());
            throwExceptionWhenReflectionIsFailed(e);
        } catch (IllegalAccessException e) {
        	logger.error(e.getMessage());
            throwExceptionWhenReflectionIsFailed(e);
        } catch (InvocationTargetException e) {
        	logger.error(e.getMessage());
            throwExceptionWhenReflectionIsFailed(e);
        }
        try {
            if (getHorizontalBar().getVisible() != result.showH) {
                getHorizontalBar().setVisible(result.showH);
            }
            if (getVerticalBar().getVisible() != result.showV) {
                getVerticalBar().setVisible(result.showV);
            }
            Rectangle r = new Rectangle(getClientArea());
            if (containerForSearchTextBox != null) {
                containerForSearchTextBox.setBounds(0, 0, r.width, containerHeight);
                r.height -= containerHeight;
                r.setLocation(0, containerHeight);
            } else {
                r.setLocation(0, 0);
            }
            getLightweightSystem().getRootFigure().setBounds(r);
        } finally {
            try {
                
                reflectionMethod.invoke(getLightweightSystem(), false);
            } catch (IllegalArgumentException e) {
            	logger.error(e.getMessage());
                throwExceptionWhenReflectionIsFailed(e);
            } catch (IllegalAccessException e) {
            	logger.error(e.getMessage());
                throwExceptionWhenReflectionIsFailed(e);
            } catch (InvocationTargetException e) {
            	logger.error(e.getMessage());
                throwExceptionWhenReflectionIsFailed(e);
            }
        }
    }


}
