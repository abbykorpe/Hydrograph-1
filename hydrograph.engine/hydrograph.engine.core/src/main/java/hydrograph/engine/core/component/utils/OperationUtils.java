package hydrograph.engine.core.component.utils;


import hydrograph.engine.core.component.entity.base.OperationEntityBase;
import hydrograph.engine.core.component.entity.elements.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by gurdits on 1/5/2017.
 */
public class OperationUtils {

    public static List<String> getAllFields(List<OutSocket> outSocketsList, List<String> inputSchema) {
        List<String> outFields = new ArrayList<>();
        for (OutSocket outSocket : outSocketsList) {
            outFields.addAll(getPassThrougFields(outSocket.getPassThroughFieldsList(), inputSchema));

            for (MapField mapField : outSocket.getMapFieldsList()) {
                outFields.add(mapField.getName());
            }

            for (OperationField op : outSocket.getOperationFieldList()) {
                outFields.add(op.getName());
            }
        }
        return outFields;
    }

    public static List<String> getAllFieldsWithOperationFields(OperationEntityBase entity, List<String> inputSchema) {
        List<String> outFields = new ArrayList<>();
        outFields.addAll(inputSchema);
        if (null != entity.getOperationsList() && entity.getOperationsList().size() > 0) {
        for (Operation operation : entity.getOperationsList()) {
                for (String field : operation.getOperationOutputFields())
                    if (!inputSchema.contains(field))
                        outFields.add(field);
            }
        }
        return outFields;
    }


    public static List<String> getPassThrougFields(List<PassThroughField> passThroughFieldList, List<String>
            inputSchemaList) {
        Set<String> passThroughFields = new HashSet<>();
        Set<String> outPutpassThroughFields = new HashSet<>();
        for (PassThroughField passThrough : passThroughFieldList) {
            passThroughFields.add(passThrough.getName());
        }

        if (passThroughFields.contains("*")) {
            return inputSchemaList;
        } else {
            for (String field : passThroughFields) {
                for (String inputSchema : inputSchemaList) {
                    if (inputSchema.matches(field))
                        outPutpassThroughFields.add(inputSchema);
                }
            }
            return new ArrayList<String>(outPutpassThroughFields);
        }
    }

}
