package com.bitwise.app.graph.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.ui.actions.Clipboard;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;

import com.bitwise.app.common.util.LogFactory;
import com.bitwise.app.graph.editor.ELTGraphicalEditor;
import com.bitwise.app.graph.model.Component;
import com.bitwise.app.graph.model.Link;
import com.bitwise.app.graph.model.Model;

// TODO: Auto-generated Javadoc
/**
 * The Class ComponentPasteCommand.
 */
public class ComponentPasteCommand extends Command {
	private static final Logger log = LogFactory.INSTANCE.getLogger(ComponentPasteCommand.class);
	private int pasteCounter=0;
	private Map list = new HashMap();

	@Override
	public boolean canExecute() {
		List bList = (ArrayList) Clipboard.getDefault().getContents();
		if (bList == null || bList.isEmpty())
			return false;
		Iterator it = bList.iterator();
		while (it.hasNext()) {
			Model node = (Model) it.next();
			if (isPastableNode(node)) {
				list.put(node, null);
			}
		}

		return true;
	}

	@Override
	public void execute() {
		if (!canExecute())
			return;
		Iterator<Component> it = list.keySet().iterator();
		while (it.hasNext()) {
       	Component node = it.next();                              
			try {
				IWorkbenchPage page = PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getActivePage();
					Component clonedComponent =  node.clone();
					clonedComponent.setParent(((ELTGraphicalEditor) page.getActiveEditor()).getContainer());
					Point location = node.getLocation();
					int incrementedLocation = pasteCounter * 20;
					clonedComponent.setLocation(new Point(location.getCopy().x + incrementedLocation,
						location.getCopy().y + incrementedLocation));
					list.put(node,clonedComponent);
			
			} catch (CloneNotSupportedException e) {
				log.error("Object could not cloned", e);
				
			}
		}
		redo();
	}

	@Override
	public void redo() {
		Iterator<Component> it = list.values().iterator();
		while (it.hasNext()) {
			Component node = it.next();
			if (isPastableNode(node)) {
				node.getParent().addChild(node);
			}
		}
	}

	@Override
	public boolean canUndo() {
		return !(list.isEmpty());
	}

	@Override
	public void undo() {
		Iterator<Component> it = list.values().iterator();
		while (it.hasNext()) {
			Component node = it.next();
			if (isPastableNode(node)) {
				node.getParent().removeChild(node);
			}
		}
	}

	/**
	 * Checks if is pastable node.
	 * 
	 * @param node
	 *            the node
	 * @return true, if is pastable node
	 */
	public boolean isPastableNode(Model node) {
		if (node instanceof Component)
			return true;
		if (node instanceof Link)
			return true;
		return false;
	}

	public int getPasteCounter() {
		return pasteCounter;
	}

	public void setPasteCounter(int pasteCounter) {
		this.pasteCounter = pasteCounter;
	}

}
