package hydrograph.ui.dataviewer.filter;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.widgets.Display;

	public class MainClass {
	public static void main(String[] args) {
		Map<String,String> fieldsAndTypes = new HashMap<>();
		fieldsAndTypes.put("firstName", "java.lang.String");
		fieldsAndTypes.put("lastName", "java.lang.String");
		fieldsAndTypes.put("age", "java.lang.Integer");
		fieldsAndTypes.put("dateOfBirth", "java.util.Date");
		
		FilterConditionsDialog test = new FilterConditionsDialog(Display.getDefault().getActiveShell());
		test.setFieldsAndTypes(fieldsAndTypes);
		test.open();
	}
}