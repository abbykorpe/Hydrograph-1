package hydrograph.engine.transformation.userfunctions.groupcombine;

import hydrograph.engine.transformation.userfunctions.base.GroupCombineTransformBase;
import hydrograph.engine.transformation.userfunctions.base.BufferField;
import hydrograph.engine.transformation.userfunctions.base.BufferSchema;
import hydrograph.engine.transformation.userfunctions.base.ReusableRow;

public class Sum implements GroupCombineTransformBase {

    @Override
    public BufferSchema initBufferSchema() {
        BufferField sum = new BufferField.Builder("sum", "Long").build();
        BufferSchema bufferSchema = new BufferSchema();
        bufferSchema.addField("sum", sum);
        return bufferSchema;
    }

    @Override
    public void initialize(ReusableRow bufferRow) {
        bufferRow.setField("sum", 0L);
    }

    @Override
    public void update(ReusableRow bufferRow, ReusableRow inputRow) {
        bufferRow.setField("sum", ((Long) bufferRow.getField("sum")) + inputRow.getLong("yearofApplying"));
    }

    @Override
    public void merge(ReusableRow bufferRow1, ReusableRow bufferRow2) {
        bufferRow1.setField("sum", ((Long) bufferRow1.getField("sum")) + ((Long) bufferRow2.getField("sum")));
    }

    @Override
    public void evaluate(ReusableRow bufferRow, ReusableRow outRow) {
        outRow.setField("sum", bufferRow.getField("sum"));
    }
}
