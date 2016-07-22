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

	private String displayName;
	private String packageName="";
	private String jarName="";
	private String cName;
	private String javaDoc;
	private List<MethodDetails> methodList = new ArrayList<MethodDetails>();

	public ClassDetails(IClassFile classFile, String jarFileName, String packageName, boolean isUserDefined) {
		Logger LOGGER = LogFactory.INSTANCE.getLogger(ClassDetails.class);
		
		LOGGER.debug("Extracting methods from "+cName);
	
		try {
			this.javaDoc = classFile.getAttachedJavadoc(null);
			intialize(classFile,jarFileName,packageName, isUserDefined);
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

	private void intialize(IClassFile classFile, String jarFileName, String packageName, boolean isUserDefined) {
		this.cName = StringUtils.removeEndIgnoreCase(classFile.getElementName(), Constants.CLASS_EXTENSION);
		displayName=cName;
		if(StringUtils.isNotBlank(jarFileName)){
			jarName=jarFileName;
		}
		if(StringUtils.isNotBlank(packageName)){
			this.packageName=packageName;
		}
		if(StringUtils.isBlank(javaDoc)){
			javaDoc=Constants.EMPTY_STRING;
		}
		if(isUserDefined){
			displayName=cName+Constants.USER_DEFINED_SUFFIX;
			updateJavaDoc(jarFileName, packageName);
		}
	}

	private void updateJavaDoc(String jarFileName, String packageName) {
		StringBuffer buffer=new StringBuffer();
		buffer.append("\n\tJar File Name :: "+jarFileName);
		buffer.append("\n\tPackage Name :: "+packageName);
		javaDoc=buffer.toString()+"\n"+javaDoc;
	}

	private boolean isMethodDepricated(IMethod iMethod) throws JavaModelException {
		for (IAnnotation annotation : iMethod.getAnnotations()) {
			if (annotation.getElementName() == "java.lang.Deprecated") {
				return true;
			}
		}
		return false;
	}

	public String getDisplayName(){
		return displayName;
	}
	
	public String getJavaDoc() {
		return javaDoc;
	}
	
	public String getJarName() {
		return jarName;
	}
	
	public String getPackageName() {
		return packageName;
	}

	public String getcName() {
		return cName;
	}

	public List<MethodDetails> getMethodList() {
		return new ArrayList<MethodDetails>(methodList);
	}

}
