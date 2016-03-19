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

 
package com.bitwise.app.common.textgrid;

import java.util.ArrayList;
import java.util.List;

public class TextGridData {
	private int numberOfColumns;
	private List<String> row;
	private List<String> columns;
	
	public TextGridData(int numberOfColumns){
		this.numberOfColumns = numberOfColumns;
		row = new ArrayList<>();
		columns = new ArrayList<>();
	}
	
	public void setData(String data,int rowIndex,int columnIndex){
		
		if(columnIndex < numberOfColumns){
			
		}
	}
}
