package hydrograph.ui.graph.editor;

import java.net.URL;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.internal.navigator.resources.actions.EditActionProvider;
import org.eclipse.ui.internal.util.BundleUtility;
import org.osgi.framework.Bundle;
import org.slf4j.Logger;

import hydrograph.ui.graph.Messages;
import hydrograph.ui.logging.factory.LogFactory;

public class CustomEditActionProvider extends EditActionProvider {

	private Logger logger = LogFactory.INSTANCE.getLogger(CustomEditActionProvider.class);
	

	@SuppressWarnings("restriction")
	public void fillContextMenu(IMenuManager menu) {
		super.fillContextMenu(menu);

		ActionContributionItem pasteContribution = getPasteContribution(menu.getItems());
		menu.remove(pasteContribution);
		IAction pasteAction = new Action(Messages.PasteActionText) {
			@Override
			public void run() {
				IHandlerService handlerService = (IHandlerService) PlatformUI.getWorkbench()
						.getService(IHandlerService.class);
				try {
					JobCopyParticipant.setCopiedFilesMap(new HashMap<>());
					handlerService.executeCommand(Messages.PasteCommandId, null);
				} catch (Exception exception) {
					logger.warn("Error while pasting job files :: {}",exception.getMessage());
				}
			}
		};
		pasteAction.setAccelerator(SWT.MOD1 | 'v');
		Bundle bundle = Platform.getBundle(Messages.MenuPluginName);
		URL imagePath = BundleUtility.find(bundle, Messages.PasteImagePath);
		ImageDescriptor imageDescriptor = ImageDescriptor.createFromURL(imagePath);
		pasteAction.setImageDescriptor(imageDescriptor);
		menu.insertAfter(Messages.CopyActionId, pasteAction);
	}

	private ActionContributionItem getPasteContribution(IContributionItem[] items) {
		for (IContributionItem contributionItem : items) {
			if (StringUtils.equals(contributionItem.getId(), Messages.PasteActionId)) {
				return (ActionContributionItem) contributionItem;
			}
		}
		return null;
	}

}
