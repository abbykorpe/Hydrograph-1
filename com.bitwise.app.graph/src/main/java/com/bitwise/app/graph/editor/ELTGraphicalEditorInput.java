package com.bitwise.app.graph.editor;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

// TODO: Auto-generated Javadoc
/**
 * The Class ETLGraphicalEditorInput.
 */
public class ELTGraphicalEditorInput implements IEditorInput {

	private String name = null;

	/**
	 * Instantiates a new ETL graphical editor input.
	 * 
	 * @param name
	 *            the name
	 */
	public ELTGraphicalEditorInput(String name) {
		this.name = name;
	}

	@Override
	public Object getAdapter(Class adapter) {
		return null;
	}

	@Override
	public boolean exists() {
		return (this.name != null);
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof ELTGraphicalEditorInput))
			return false;
		return ((ELTGraphicalEditorInput) o).getName().equals(getName());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}
	
	@Override
	public ImageDescriptor getImageDescriptor() {
		return ImageDescriptor.getMissingImageDescriptor();
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getToolTipText() {
		return this.name;
	}

}
