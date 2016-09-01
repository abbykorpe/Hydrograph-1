package hydrograph.ui.expression.editor.javasourceviewerconfiguration;

import org.eclipse.jface.bindings.Trigger;
import org.eclipse.jface.bindings.TriggerSequence;
import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.jface.bindings.keys.ParseException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.keys.IBindingService;

/**
 * created by hwang on Jan 5, 2016 Detailled comment
 *
 */
public class HotKeyUtil {

    public static String contentAssist = "org.eclipse.ui.edit.text.contentAssist.proposals";

    public static KeyStroke getHotKey(String commondID) throws ParseException {
        IBindingService bindingService = (IBindingService) PlatformUI.getWorkbench().getService(IBindingService.class);
        if (bindingService != null) {
            TriggerSequence trigger = bindingService.getBestActiveBindingFor(commondID);
            if (trigger != null) {
                Trigger[] tiggers = trigger.getTriggers();
                if (tiggers.length > 0) {
                    Trigger tigger = tiggers[0];
                    if (tigger instanceof KeyStroke) {
                        return (KeyStroke) tigger;
                    }
                }
            }
        }
        return KeyStroke.getInstance("Ctrl+Space");
    }
}
