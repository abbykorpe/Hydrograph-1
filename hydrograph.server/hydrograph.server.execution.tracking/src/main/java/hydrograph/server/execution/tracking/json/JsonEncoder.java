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

package hydrograph.server.execution.tracking.json;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import com.google.gson.Gson;

import hydrograph.server.execution.tracking.server.status.datastructures.ExecutionStatus; 
 
/**
 * Encode message
 * The Class JsonEncoder.
 */
public class JsonEncoder implements Encoder.Text<ExecutionStatus> { 
 
 /**
  * Inits the.
  *
  * @param endpointConfig the endpoint config
  */
 public void init(EndpointConfig endpointConfig) { 
 } 
 
 /**
  * Destroy.
  */
 public void destroy() { 
 } 
 
 /**
  * Encode.
  *
  * @param status the status
  * @return the string
  * @throws EncodeException the encode exception
  */
 public String encode(ExecutionStatus status) throws EncodeException { 
  Gson gson = new Gson(); 
  String result = gson.toJson(status); 
  return result; 
 } 
 
}