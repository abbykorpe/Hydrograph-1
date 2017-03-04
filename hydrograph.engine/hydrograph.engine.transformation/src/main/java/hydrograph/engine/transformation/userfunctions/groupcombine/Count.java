package hydrograph.engine.transformation.userfunctions.groupcombine;

import hydrograph.engine.transformation.userfunctions.base.GroupCombineTransformBase;
import hydrograph.engine.transformation.userfunctions.base.BufferField;
import hydrograph.engine.transformation.userfunctions.base.BufferSchema;
import hydrograph.engine.transformation.userfunctions.base.ReusableRow;

public class Count implements GroupCombineTransformBase {

    @Override
    public BufferSchema initBufferSchema() {
        BufferField count = new BufferField.Builder("count", "Long").build();
        BufferSchema bufferSchema = new BufferSchema();
        bufferSchema.addField("count", count);
        return bufferSchema;
    }

    @Override
    public void initialize(ReusableRow bufferRow) {
        bufferRow.setField("count", 0L);
    }

    @Override
    public void update(ReusableRow bufferRow, ReusableRow inputRow) {
        bufferRow.setField("count", ((Long) bufferRow.getField("count")) + 1L);
    }

    @Override
    public void merge(ReusableRow bufferRow1, ReusableRow bufferRow2) {
        bufferRow1.setField("count", ((Long) bufferRow1.getField("count")) + ((Long) bufferRow2.getField("count")));
    }

    @Override
    public void evaluate(ReusableRow bufferRow, ReusableRow outRow) {
        outRow.setField("count", bufferRow.getField("count"));
    }
}
