package hydrograph.ui.expression.editor.datastructure;

import hydrograph.ui.expression.editor.Constants;
import hydrograph.ui.logging.factory.LogFactory;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.slf4j.Logger;

public class ClassDetails {

	private String cName;
	private String javaDoc;
	private List<MethodDetails> methodList = new ArrayList<MethodDetails>();

	public ClassDetails(IClassFile classFile, boolean isUserDefined) {
		Logger LOGGER = LogFactory.INSTANCE.getLogger(ClassDetails.class);
		
		intialize(classFile, isUserDefined);
		LOGGER.debug("Extracting methods from "+cName);
	
		try {
			this.javaDoc = classFile.getAttachedJavadoc(null);
			for (IJavaElement iJavaElement : classFile.getChildren()) {
				if (iJavaElement instanceof IType) {
					IType iType = (IType) iJavaElement;
					for (IMethod iMethod : iType.getMethods()) {
						if (iMethod.isConstructor() || iMethod.isMainMethod() || isMethodDepricated(iMethod)) {
							continue;
						} else {
							if (Flags.isPublic(iMethod.getFlags()) && Flags.isStatic(iMethod.getFlags())) {
								if (StringUtils.isBlank(iMethod.getSource())) {
									methodList.add(new MethodDetails(iMethod,cName, false));
								} else
									methodList.add(new MethodDetails(iMethod,cName, true));
							}
						}
					}
				}
			}
		} catch (JavaModelException e) {
			LOGGER.error("Error occured while fetching methods from class"+cName);
		}
	}

	private void intialize(IClassFile classFile, boolean isUserDefined) {
		this.cName = StringUtils.removeEndIgnoreCase(classFile.getElementName(), Constants.CLASS_EXTENSION);
		if(isUserDefined){
			cName=cName+Constants.USER_DEFINED_SUFFIX;
		}
	}

	private boolean isMethodDepricated(IMethod iMethod) throws JavaModelException {
		for (IAnnotation annotation : iMethod.getAnnotations()) {
			if (annotation.getElementName() == "java.lang.Deprecated") {
				return true;
			}
		}
		return false;
	}

	public String getJavaDoc() {
		return javaDoc;
	}

	public String getcName() {
		return cName;
	}

	public List<MethodDetails> getMethodList() {
		return new ArrayList<MethodDetails>(methodList);
	}

}
