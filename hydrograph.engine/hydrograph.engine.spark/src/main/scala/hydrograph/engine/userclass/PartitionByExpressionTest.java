package hydrograph.engine.userclass;

import hydrograph.engine.transformation.userfunctions.base.CustomPartitionExpression;
import hydrograph.engine.transformation.userfunctions.base.ReusableRow;

import java.io.Serializable;
import java.util.Properties;

/**
 * Created by santlalg on 12/15/2016.
 */
public class PartitionByExpressionTest implements CustomPartitionExpression, Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public void prepare(Properties props) {

    }

    @Override
    public String getPartition(ReusableRow keys, int numOfPartitions) {
        if (keys.getField(0) == null) {
            return "out_credit";
        }
        if (keys.getString(0).trim().equalsIgnoreCase("credit")) {
            return "out_credit";
        } else if (keys.getString(0).trim().equalsIgnoreCase("debit")) {
            return "out_debit";
        } else {
            return "out_mix";
        }
    }
}
