/********************************************************************************
 * Copyright 2016 Capital One Services, LLC and Bitwise, Inc.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package hydrograph.ui.graph.factory;

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