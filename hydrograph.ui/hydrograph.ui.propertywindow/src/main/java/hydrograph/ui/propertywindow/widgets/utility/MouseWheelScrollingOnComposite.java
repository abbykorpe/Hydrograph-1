package hydrograph.ui.propertywindow.widgets.utility;

import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class MouseWheelScrollingOnComposite {
	public static  void installMouseWheelScrollRecursively( ScrolledComposite scrollable) {
		MouseWheelListener scroller = createMouseWheelScroller(scrollable);
		if (scrollable.getParent() != null)
		{	
		 scrollable.getParent().addMouseWheelListener(scroller);
		}	
		installMouseWheelScrollRecursively(scroller, scrollable);
	}

	public static   MouseWheelListener createMouseWheelScroller(final ScrolledComposite scrollable) {
		return new MouseWheelListener() {
			
			@Override
			public void mouseScrolled(MouseEvent e) {
				Point currentScroll = scrollable.getOrigin();
				scrollable.setOrigin(currentScroll.x, currentScroll.y - (e.count * 5));
			}
		};
	}

	public static  void installMouseWheelScrollRecursively(MouseWheelListener scroller, Control c) {
		c.addMouseWheelListener(scroller);
		if (c instanceof Composite) {
			Composite comp = (Composite) c;
			for (Control child : comp.getChildren()) {
				installMouseWheelScrollRecursively(scroller, child);
			}
		}
	}

}
