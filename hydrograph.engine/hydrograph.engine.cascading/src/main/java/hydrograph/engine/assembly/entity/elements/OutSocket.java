/*******************************************************************************
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
 *******************************************************************************/
package hydrograph.engine.assembly.entity.elements;

import java.util.Arrays;
import java.util.List;

/**
 * This is a POJO which holds the information for one OutSocket in any of the
 * components Type like
 * {@link hydrograph.engine.cascading.assembly.InputFileDelimitedAssembly Input}
 * , {@link hydrograph.engine.cascading.assembly.CloneAssembly Clone} etc. The
 * object of this class is supposed to be used in the entity classes for all
 * type of components
 * 
 * @author gurdits
 *
 */
public class OutSocket {

	private String socketId;
	private String socketType = "out";
	private List<MapField> mapFieldsList;
	private List<OperationField> operationFieldsList;
	private String copyOfInSocketId;
	private List<PassThroughField> passThroughFieldsList;

	/**
	 * @param socketId
	 */
	public OutSocket(String socketId) {
		this.socketId = socketId;
	}

	/**
	 * @param socketId
	 * @param socketType
	 */
	public OutSocket(String socketId, String socketType) {
		this.socketId = socketId;
		this.socketType = socketType;
	}

	public String getCopyOfInSocketId() {
		return copyOfInSocketId;
	}

	public void setCopyOfInSocketId(String copyOfInSocketId) {
		this.copyOfInSocketId = copyOfInSocketId;
	}

	/**
	 * @return mapFieldsList
	 */
	public List<MapField> getMapFieldsList() {
		return mapFieldsList;
	}

	/**
	 * @param mapFieldsList
	 */
	public void setMapFieldsList(List<MapField> mapFieldsList) {
		this.mapFieldsList = mapFieldsList;
	}

	/**
	 * @return passThroughFieldsList
	 */
	public List<PassThroughField> getPassThroughFieldsList() {
		return passThroughFieldsList;
	}

	/**
	 * @param passThroughFieldsList
	 */
	public void setPassThroughFieldsList(
			List<PassThroughField> passThroughFieldsList) {
		this.passThroughFieldsList = passThroughFieldsList;
	}

	/**
	 * @return the socketId
	 */
	public String getSocketId() {
		return socketId;
	}

	/**
	 * @return the socketType
	 */
	public String getSocketType() {
		return socketType;
	}

	/**
	 * @param socketType
	 *            the socketType to set
	 */
	public void setSocketType(String socketType) {
		this.socketType = socketType;
	}

	/**
	 * @return the operationField
	 */
	public List<OperationField> getOperationFieldList() {
		// TODO Auto-generated method stub
		return operationFieldsList;
	}

	/**
	 * @param operationFieldsList
	 *            the operationField to set
	 */
	public void setOperationFieldList(List<OperationField> operationFieldsList) {
		// TODO Auto-generated method stub
		this.operationFieldsList = operationFieldsList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {

		StringBuilder str = new StringBuilder();
		str.append("socket id: " + socketId + " | socket type: " + socketType
				+ " | ");

		str.append("map fields: ");
		if (mapFieldsList != null) {
			str.append(Arrays.toString(mapFieldsList.toArray()));
		}

		str.append(" | operation fields: ");
		if (operationFieldsList != null) {
			str.append(Arrays.toString(operationFieldsList.toArray()));
		}

		str.append(" | pass through fields: ");
		if (passThroughFieldsList != null) {
			str.append(Arrays.toString(passThroughFieldsList.toArray()));
		}

		str.append(" | copy of in socket: " + copyOfInSocketId);

		return str.toString();
	}
}
