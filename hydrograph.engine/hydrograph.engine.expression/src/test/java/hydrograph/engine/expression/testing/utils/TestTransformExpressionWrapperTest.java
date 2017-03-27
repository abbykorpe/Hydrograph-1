/*******************************************************************************
 * Copyright 2017 Capital One Services, LLC and Bitwise, Inc.
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

package hydrograph.engine.expression.testing.utils;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestTransformExpressionWrapperTest {

    @Test
    public void itShouldGenerateCorrectOutputForIntegerTypes() {
        Object[] outRows;
        String expression = "f1 + f2";

        // Operation Input Field Names
        String[] inputFieldNames = new String[2];
        inputFieldNames[0] = "f1";
        inputFieldNames[1] = "f2";

        // Operation Input Field Types
        Class[] inputFieldTypes = new Class[2];
        inputFieldTypes[0] = Integer.class;
        inputFieldTypes[1] = Integer.class;

        // Input Data
        List<Object[]> inputData = new ArrayList<Object[]>();
        Object[] inputRow1 = {21, 1234};
        inputData.add(inputRow1);

        outRows = TestTransformExpressionWrapper.evaluateExpression
                (expression, inputFieldNames, inputFieldTypes, inputData);

        Assert.assertEquals("1255", outRows[0].toString());
    }

    @Test
    public void itShouldGenerateCorrectOutputForStringTypes() {
        Object[] outRows;
        String expression = "f1 + f2";

        // Operation Input Field Names
        String[] inputFieldNames = new String[2];
        inputFieldNames[0] = "f1";
        inputFieldNames[1] = "f2";

        // Operation Input Field Types
        Class[] inputFieldTypes = new Class[2];
        inputFieldTypes[0] = String.class;
        inputFieldTypes[1] = String.class;

        // Input Data
        List<Object[]> inputData = new ArrayList<Object[]>();
        Object[] inputRow1 = {"21", "1234"};
        inputData.add(inputRow1);

        outRows = TestTransformExpressionWrapper.evaluateExpression
                (expression, inputFieldNames, inputFieldTypes, inputData);

        Assert.assertEquals("211234", outRows[0].toString());
    }

}
