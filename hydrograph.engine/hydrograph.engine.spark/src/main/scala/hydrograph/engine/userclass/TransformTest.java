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
package hydrograph.engine.userclass;

import hydrograph.engine.transformation.userfunctions.base.ReusableRow;
import hydrograph.engine.transformation.userfunctions.base.TransformBase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Properties;

public class TransformTest implements TransformBase, Serializable {

    String value = "";

    @Override
    public void prepare(Properties props, ArrayList<String> inputFields, ArrayList<String> outputFields) {
//        value = props.get("val").toString();
        value="AA";
        System.out.println("--------------------prepare-------------------------");
    }

    @Override
    public void transform(ReusableRow inputRow, ReusableRow outputRow) {
        if (inputRow.getField("name").toString().equals(value))
            outputRow.setField(0, inputRow.getField("name").toString().toUpperCase());
        else
            outputRow.setField(0, "dummy");

        System.out.println("--------------------transform-------------------------");
    }

    @Override
    public void cleanup() {
        value="";
        System.out.println("--------------------clean up-------------------------");
    }

}