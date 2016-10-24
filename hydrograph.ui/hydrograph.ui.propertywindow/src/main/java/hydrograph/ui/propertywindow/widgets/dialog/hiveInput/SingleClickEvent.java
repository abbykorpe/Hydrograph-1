package hydrograph.ui.propertywindow.widgets.dialog.hiveInput;

import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;

class SingleClickEvent extends MouseAdapter {

	public static final int LEFT_BUTTON = 1;
	private final Runnable action;
	private static Boolean armed;

	SingleClickEvent(Runnable action) {
		this.action = action;
	}

	@Override
	public void mouseUp(MouseEvent event) {
		if (armed && inRange(event)) {
			action.run();
		}
		armed = false;
	}

	public void mouseDown(MouseEvent event) {
		if (event.button == LEFT_BUTTON) {
			armed = true;
		}
	}

	static boolean inRange(MouseEvent event) {
		Point size = ((Control) event.widget).getSize();
		return event.x >= 0 && event.x <= size.x && event.y >= 0 && event.y <= size.y;
	}
}
