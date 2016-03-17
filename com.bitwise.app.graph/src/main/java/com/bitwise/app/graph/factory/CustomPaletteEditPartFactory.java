package com.bitwise.app.graph.factory;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.internal.ui.palette.editparts.SliderPaletteEditPart;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.ui.palette.PaletteEditPartFactory;
import org.eclipse.swt.graphics.Color;



/**
 * A factory for creating CustomPaletteEditPart objects.
 */
public class CustomPaletteEditPartFactory extends PaletteEditPartFactory {

    private Color palatteTextColor;
    
	/**
	 * Instantiates a new custom palette edit part factory.
	 * 
	 * @param palatteTextColor
	 */
    public CustomPaletteEditPartFactory(Color palatteTextColor) {
    	this.palatteTextColor = palatteTextColor;
    }
    @Override
    protected EditPart createMainPaletteEditPart(EditPart parentEditPart, Object model) {
        return new SliderPaletteEditPart((PaletteRoot)model) {
            @Override
            public IFigure createFigure() {
				IFigure figure = super.createFigure();
				figure.setForegroundColor(palatteTextColor);
				return figure;
            }
        };
    }
}