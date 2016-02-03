package com.bitwise.app.graph.command;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.ui.actions.Clipboard;

import com.bitwise.app.graph.model.Component;
import com.bitwise.app.graph.model.Link;
import com.bitwise.app.graph.model.Model;

// TODO: Auto-generated Javadoc
/**
 * The Class ComponentCutCommand represents Cut command for Component.
 * 
 * @author Bitwise
 */
public class SubGraphCommand extends Command {
	private ArrayList list = new ArrayList();
	private List links = new ArrayList();

	private boolean wasRemoved;
 


	@Override
	public boolean canUndo() {
		return wasRemoved;
	}

	/**
	 * Adds the element.
	 * 
	 * @param node
	 *            the node
	 * @return true, if successful
	 */

	public boolean addElement(Component node) {
		if (!list.contains(node)) {

			return list.add(node);
		}
		return false;
	}
	
	public boolean addElementLink(Link link) {
		if (!links.contains(link)) {

			return links.add(link);
		}
		return false;
	}


	@Override
	public void undo() {
		Iterator<Component> it = list.iterator();
		while (it.hasNext()) {
			Component node = it.next();
			node.getParent().addChild(node);

		}

	}

	@Override
	public boolean canExecute() {
		if (list == null || list.isEmpty())
			return false;
		Iterator<Component> it = list.iterator();
		while (it.hasNext()) {
			if (!isCutNode(it.next()))
				return false;
		}
		return true;
	}

	@Override
	public void redo() {
		Iterator it = list.iterator();
		while (it.hasNext()) {
			Model node = (Model) it.next();
			if(node instanceof Component)
			wasRemoved = ((Component)node).getParent().removeChild((Component)node);
		}
	}

	@Override
	public void execute() {
		list.addAll(links);
		Clipboard.getDefault().setContents(list);
		redo();
	}

	/**
	 * Checks if is copyable node.
	 * 
	 * @param node
	 *            the node
	 * @return true, if is copyable node
	 */
	public boolean isCutNode(Component node) {
		if (node instanceof Component)
			return true;
		return false;
	}

}
