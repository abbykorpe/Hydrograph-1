package hydrograph.engine.transformation.userfunctions.groupcombine;

import hydrograph.engine.transformation.userfunctions.base.GroupCombineTransformBase;
import hydrograph.engine.transformation.userfunctions.base.BufferField;
import hydrograph.engine.transformation.userfunctions.base.BufferSchema;
import hydrograph.engine.transformation.userfunctions.base.ReusableRow;

public class Max implements GroupCombineTransformBase {

    @Override
    public BufferSchema initBufferSchema() {
        BufferField sum = new BufferField.Builder("max", "Long").build();
        BufferSchema bufferSchema = new BufferSchema();
        bufferSchema.addField("max", sum);
        return bufferSchema;
    }

    @Override
    public void initialize(ReusableRow bufferRow) {
        bufferRow.setField("max", 0L);
    }

    @Override
    public void update(ReusableRow bufferRow, ReusableRow inputRow) {
        bufferRow.setField("max", ((Long) bufferRow.getField("max")) + inputRow.getLong("yearofApplying"));
    }

    @Override
    public void merge(ReusableRow bufferRow1, ReusableRow bufferRow2) {
        bufferRow1.setField("max", ((Long) bufferRow1.getField("max")) + ((Long) bufferRow2.getField("max")));
    }

    @Override
    public void evaluate(ReusableRow bufferRow, ReusableRow outRow) {
        outRow.setField("max", bufferRow.getField("max"));
    }
}
