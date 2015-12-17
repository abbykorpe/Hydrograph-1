package com.bitwise.app.common.interfaces.tooltip;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;

import com.bitwise.app.tooltip.tooltips.ComponentTooltip;

public interface ComponentCanvas {
	public Control getCanvasControl();
	public void issueToolTip(ComponentTooltip componentTooltip,Rectangle componentBounds);
	public ComponentTooltip getComponentTooltip();
}
