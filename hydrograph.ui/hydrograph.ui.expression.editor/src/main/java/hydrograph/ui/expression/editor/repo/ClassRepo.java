package hydrograph.ui.expression.editor.repo;

import hydrograph.ui.expression.editor.datastructure.ClassDetails;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.IClassFile;

public class ClassRepo {

	public static final ClassRepo INSTANCE=new ClassRepo();
	private List<ClassDetails> classList=new ArrayList<ClassDetails>();
	
	private ClassRepo(){/* Singleton */}

	public void addClass(IClassFile classFile, String jarFileName, String packageName, boolean isUserDefined) {
		classList.add(new ClassDetails(classFile, jarFileName, packageName,isUserDefined));
	}

	public List<ClassDetails> getClassList() {
		return new ArrayList<ClassDetails>(classList);
	}

	public void flusRepo() {
		classList.clear();
	}
	
	public void remove() {
		
	}
	
}
