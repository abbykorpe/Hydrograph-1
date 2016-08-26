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

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

import com.google.gson.Gson;

import hydrograph.server.execution.tracking.server.status.datastructures.ExecutionStatus; 
 
/**
 * The Class JsonDecoder.
 */
public class JsonDecoder implements Decoder.Text<ExecutionStatus> { 
 
 /**
  * Destroy.
  */
 public void destroy() { 
 
 } 
 
 /**
  * Decode the message.
  *
  * @param msg the msg
  * @return the execution status
  * @throws DecodeException the decode exception
  */
 public ExecutionStatus decode(String msg) throws DecodeException { 
  Gson gson = new Gson(); 
  ExecutionStatus result  = gson.fromJson(msg, ExecutionStatus.class); 
  return result; 
 } 
 
/**
 * Inits the.
 *
 * @param config the config
 */
public void init(EndpointConfig config) {

}

/**
 * Will decode.
 *
 * @param s the s
 * @return true, if successful
 * @see javax.websocket.Decoder.Text#willDecode(java.lang.String)
 */
public boolean willDecode(String s) {
    return true;
} 
 
}