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
package hydrograph.engine.cascading.debug;

import hydrograph.engine.jaxb.debug.ViewData;

import java.util.ArrayList;
import java.util.List;

public class DebugUtils {

	/**
	 * @param listOfViewData
	 * @return listOfDebugPoints
	 */
	public static List<DebugPoint> extractDebugPoints(
			List<ViewData> listOfViewData) {
		ArrayList<DebugPoint> listOfDebugPoints = new ArrayList<DebugPoint>();
		DebugPoint debugPoint;
		for (ViewData viewData : listOfViewData) {
			debugPoint = new DebugPoint();
			debugPoint.setFromComponentId(viewData.getFromComponentId());
			debugPoint.setOutSocketId(viewData.getOutSocketId());
			if (viewData.getLimit() != null)
				debugPoint.setLimit(viewData.getLimit().getValue());
			listOfDebugPoints.add(debugPoint);
		}
		return listOfDebugPoints;
	}
}
