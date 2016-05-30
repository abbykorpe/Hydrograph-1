package hydrograph.ui.dataviewer.support;

import org.eclipse.jface.action.ContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.StatusLineLayoutData;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class StatusField extends ContributionItem {

	private static final int DEFAULT_WIDTH = 25;

	private IAction doubleClickAction;
	private CLabel label;
	private MenuManager menuManager;
	private String message;
	private final String toolTip;
	private final int widthInChars;

	public StatusField(final int widthInChars, final String message,
			final String toolTip) {
		this.widthInChars = ((widthInChars < 1) ? DEFAULT_WIDTH : widthInChars);
		this.message = message;
		this.toolTip = toolTip;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.action.ContributionItem#fill(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void fill(final Composite parent) {
		final GC gc = new GC(parent);
		gc.setFont(parent.getFont());

		final Label sep = new Label(parent, SWT.SEPARATOR);
		sep.setLayoutData(new StatusLineLayoutData());
		((StatusLineLayoutData) sep.getLayoutData()).heightHint = gc
				.getFontMetrics().getHeight();

		this.label = new CLabel(parent, SWT.SHADOW_NONE);
		this.label.setLayoutData(new StatusLineLayoutData());
		((StatusLineLayoutData) this.label.getLayoutData()).widthHint = ((gc
				.getFontMetrics().getAverageCharWidth() * this.widthInChars));

		gc.dispose();

		// cannot use same menu manager twice to create menu (weird) so have to
		// recreate each time (and this method gets called a lot)
		if (this.menuManager != null) {
			final MenuManager mm = new MenuManager();

			for (final IContributionItem item : this.menuManager.getItems()) {
				mm.add(item);
			}

			this.label.setMenu(mm.createContextMenu(this.label));
		}

		// add double-click listener
		if (this.doubleClickAction != null) {
			final CLabel accessLabel = this.label;

			this.label.addMouseListener(new MouseAdapter() {

				/**
				 * {@inheritDoc}
				 * 
				 * @see org.eclipse.swt.events.MouseAdapter#mouseDoubleClick(org.eclipse.swt.events.MouseEvent)
				 */
				@Override
				public void mouseDoubleClick(final MouseEvent e) {
					// only handle double click if there is a selected object
					/*if (!CndMessages.statusBarNoSelection.equals(accessLabel
							.getText())) {
						handleDoubleClick();
					}*/
				}
			});
		}

		// set current message and tooltip
		updateLabel();
	}

	void handleDoubleClick() {
		assert (this.doubleClickAction != null) : "double click action is null and action handler was called";
		this.doubleClickAction.run();
	}

	void setDoubleClickAction(final IAction doubleClickAction) {
		this.doubleClickAction = doubleClickAction;
	}

	void setMenuManager(final MenuManager newMenuManager) {
		this.menuManager = newMenuManager;
	}

	/**
	 * @param newMessage
	 *            the new message (can be <code>null</code> or empty)
	 */
	void setMessage(final String newMessage) {
		this.message = newMessage;
		updateLabel();
	}

	private void updateLabel() {
		if ((this.label != null) && !this.label.isDisposed()) {
			{ // update label text
				/*final String newText = (UiUtils.isEmpty(this.message) ? CndMessages.statusBarNoSelection
						: this.message);*/

				final String newText = this.message;
				
				if (newText!=null && !newText.equals(this.label.getText())) {
					this.label.setText(newText);
				}
			}

			{ // update label tooltip
//				final String newToolTip = UiUtils.ensureNotNull(this.toolTip);
				final String newToolTip = this.toolTip;
				if (newToolTip!=null && !newToolTip.equals(this.label.getToolTipText())) {
					this.label.setToolTipText(newToolTip);
				}
			}
		}
	}
}