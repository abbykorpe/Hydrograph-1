package hydrograph.ui.expression.editor.datastructure;

import hydrograph.ui.expression.editor.Constants;
import hydrograph.ui.expression.editor.enums.DataTypes;
import hydrograph.ui.expression.editor.util.ExpressionEditorUtil;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.swt.SWT;

/**
 * @author Bitwise
 * 
 */
public class MethodDetails {

	String methodName;
	String signature;
	String returnType;
	String placeHolder;
	String javaDoc = Constants.EMPTY_STRING;

	public MethodDetails(IMethod iMethod, String className, boolean isSourceAvailable) throws JavaModelException {
		methodName = iMethod.getElementName();
		signature = createSignature(iMethod);
		if (isSourceAvailable) {
			createFormattedJavaDoc(iMethod);
			createPlaceHolderFromSource(iMethod, className);
		} else {
			placeHolder = createDefaultPlaceHolder(iMethod, className);
		}
	}

	private void createPlaceHolderFromSource(IMethod iMethod, String className) throws JavaModelException {
		StringBuffer buffer = new StringBuffer(iMethod.getSource());
		int indexOfPlaceHolder = buffer.lastIndexOf("@see");
		if (indexOfPlaceHolder != -1 && iMethod.getParameterNames() != null && iMethod.getParameterNames().length > 0) {
			buffer = buffer.delete(0, indexOfPlaceHolder + 4);
			buffer = buffer.delete(buffer.indexOf("\n")+1, buffer.capacity());
			if(StringUtils.contains(buffer.toString(),iMethod.getElementName())){
				placeHolder = className + Constants.DOT + StringUtils.trim(buffer.toString());
			}
			else
				placeHolder = createDefaultPlaceHolder(iMethod, className);
		} else {
			placeHolder = createDefaultPlaceHolder(iMethod, className);
		}
	}

	private void createPlaceHolder(IMethod iMethod, String className) throws JavaModelException {
		placeHolder = className + Constants.DOT + signature;
		if (iMethod.getParameterNames() != null && iMethod.getParameterNames().length != 0) {

		}
	}

	

	private String createSignature(IMethod iMethod) throws JavaModelException {
		StringBuffer buffer = new StringBuffer();
		returnType = DataTypes.getDataTypefromString(ExpressionEditorUtil.INSTANCE.lastString(iMethod.getReturnType(), Constants.DOT));
		buffer.append(iMethod.getElementName() + SWT.SPACE + Constants.OPENING_BRACKET);
		if (iMethod.getParameters() != null && iMethod.getParameters().length > 0) {
			for (int index = 0; index < iMethod.getParameters().length; index++) {
				buffer.append(DataTypes.getDataTypefromString(ExpressionEditorUtil.INSTANCE.lastString(
						iMethod.getParameters()[index].getTypeSignature(), Constants.DOT)));
				buffer.append(SWT.SPACE);
				buffer.append(iMethod.getParameters()[index].getElementName());
				if (index != iMethod.getParameters().length - 1)
					buffer.append(Constants.COMMA);
			}
		}
		buffer.append(Constants.CLOSING_BRACKET);
		buffer.append(SWT.SPACE + Constants.DASH + SWT.SPACE + returnType);
		return buffer.toString();
	}

	private String formatType(String returnType) {
		if (StringUtils.equalsIgnoreCase(returnType, "V")) {
			returnType = "void";
		} else if (StringUtils.equalsIgnoreCase(returnType, "Z")) {
			returnType = "boolean";
		}
		return returnType;
	}

	private String createFormattedJavaDoc(IMethod iMethod) throws JavaModelException {
		String source = iMethod.getSource();
		if (iMethod.getJavadocRange() != null) {
			javaDoc = StringUtils.substring(source, 0, iMethod.getJavadocRange().getLength());
			javaDoc = StringUtils.replaceEachRepeatedly(javaDoc, new String[] { "/*", "*/", "*" }, new String[] {
					Constants.EMPTY_STRING, Constants.EMPTY_STRING, Constants.EMPTY_STRING });
		}
		return javaDoc;
	}

	public String getMethodName() {
		return methodName;
	}

	public String getSignature() {
		return signature;
	}

	public String getReturnType() {
		return returnType;
	}

	public String getJavaDoc() {
		return javaDoc;
	}

	public String getPlaceHolder() {
		return placeHolder;
	}

	public String createDefaultPlaceHolder(IMethod iMethod, String className) throws JavaModelException {
		StringBuffer buffer = new StringBuffer();
		buffer.append(className + Constants.DOT);
		buffer.append(iMethod.getElementName()+ Constants.OPENING_BRACKET);
		if (iMethod.getParameterNames() != null && iMethod.getParameterNames() != null)
			for (int index = 0; index < iMethod.getParameterNames().length; index++) {
				buffer.append(iMethod.getParameterNames()[index]);
				if (index != iMethod.getParameterTypes().length - 1) {
					buffer.append(Constants.COMMA + SWT.SPACE);
				}
			}
		buffer.append(Constants.CLOSING_BRACKET);
		return StringUtils.trim(buffer.toString());
	}

}
