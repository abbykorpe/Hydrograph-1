package hydrograph.engine.transformation.userfunctions.groupcombine;

import hydrograph.engine.transformation.userfunctions.base.GroupCombineTransformBase;
import hydrograph.engine.transformation.userfunctions.base.BufferField;
import hydrograph.engine.transformation.userfunctions.base.BufferSchema;
import hydrograph.engine.transformation.userfunctions.base.ReusableRow;

public class Min implements GroupCombineTransformBase {

    @Override
    public BufferSchema initBufferSchema() {
        BufferField sum = new BufferField.Builder("min", "Long").build();
        BufferSchema bufferSchema = new BufferSchema();
        bufferSchema.addField("min", sum);
        return bufferSchema;
    }

    @Override
    public void initialize(ReusableRow bufferRow) {
        bufferRow.setField("min", 0L);
    }

    @Override
    public void update(ReusableRow bufferRow, ReusableRow inputRow) {
        bufferRow.setField("min", ((Long) bufferRow.getField("min")) + inputRow.getLong("yearofApplying"));
    }

    @Override
    public void merge(ReusableRow bufferRow1, ReusableRow bufferRow2) {
        bufferRow1.setField("min", ((Long) bufferRow1.getField("min")) + ((Long) bufferRow2.getField("min")));
    }

    @Override
    public void evaluate(ReusableRow bufferRow, ReusableRow outRow) {
        outRow.setField("min", bufferRow.getField("min"));
    }
}
