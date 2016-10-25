package hydrograph.ui.graph.controller;

import org.eclipse.swt.widgets.Text;

import hydrograph.ui.graph.figure.StickyNoteFigure;

import org.eclipse.jface.viewers.CellEditor;

import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gef.tools.CellEditorLocator;


final public class LabelCellEditorLocator
	implements CellEditorLocator
{

private StickyNoteFigure stickyNote;

public LabelCellEditorLocator(StickyNoteFigure stickyNote) {
	setLabel(stickyNote);
}

@Override
public void relocate(CellEditor celleditor) {
	Text text = (Text)celleditor.getControl();
	Rectangle rect = stickyNote.getClientArea();
	stickyNote.translateToAbsolute(rect);
	org.eclipse.swt.graphics.Rectangle trim = text.computeTrim(0, 0, 0, 0);
	rect.translate(trim.x, trim.y);
	rect.width += trim.width;
	rect.height += trim.height;
	text.setBounds(rect.x, rect.y, rect.width, rect.height);
}

/**
 * Returns the stickyNote figure.
 */
protected StickyNoteFigure getLabel() {
	return stickyNote;
}

/**
 * Sets the Sticky note figure.
 * @param stickyNote The stickyNote to set
 */
protected void setLabel(StickyNoteFigure stickyNote) {
	this.stickyNote = stickyNote;
}

}