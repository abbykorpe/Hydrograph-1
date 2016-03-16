/********************************************************************************
 * Copyright 2016 Capital One Services, LLC and Bitwise, Inc.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

 
package com.bitwise.app.engine.converter;

import java.util.List;

import com.bitwise.app.engine.xpath.ComponentXpath;
import com.bitwise.app.engine.xpath.ComponentXpathConstants;
import com.bitwise.app.engine.xpath.ComponentsAttributeAndValue;
import com.bitwiseglobal.graph.commontypes.TypeBaseInSocket;
import com.bitwiseglobal.graph.commontypes.TypeOperationsComponent;
import com.bitwiseglobal.graph.commontypes.TypeOperationsOutSocket;
import com.bitwiseglobal.graph.commontypes.TypeTransformOperation;
import com.bitwiseglobal.graph.transform.TypeTransformOutSocket;

public abstract class TransformConverter extends Converter {
	
	@Override
	public void prepareForXML(){
		super.prepareForXML();
		((TypeOperationsComponent) baseComponent).getInSocket().addAll(getInSocket());
		((TypeOperationsComponent)baseComponent).getOutSocket().addAll(getOutSocket());
		((TypeOperationsComponent)baseComponent).setRuntimeProperties(getRuntimeProperties()); 
		
	}
	
	/**
	 * Returns {@link List} of classes of type {@link TypeTransformOutSocket}
	 * @return {@link List}
	 */
	protected abstract  List<TypeOperationsOutSocket> getOutSocket();
	
	/**
	 * Returns {@link List} of classes of type {@link TypeTransformOperation} 
	 * @return {@link List}
	 */
	protected abstract List<TypeTransformOperation> getOperations();
	public abstract List<TypeBaseInSocket> getInSocket();
	
	protected void getParamTag(String fieldName, ComponentXpathConstants paramXpath) {
		ComponentsAttributeAndValue tempAndValue=ComponentXpath.INSTANCE.getXpathMap().get(paramXpath.value().replace(ID, componentName));
		if(tempAndValue==null)
			ComponentXpath.INSTANCE.getXpathMap().put(
					(paramXpath.value().replace(ID, componentName)),
					new ComponentsAttributeAndValue(true,fieldName ));
		else
			tempAndValue.setNewNodeText(tempAndValue.getNewNodeText()+" "+fieldName);
	}
}
