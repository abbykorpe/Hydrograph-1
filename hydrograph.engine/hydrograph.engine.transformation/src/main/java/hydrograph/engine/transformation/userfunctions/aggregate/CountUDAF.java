package hydrograph.engine.transformation.userfunctions.aggregate;

import hydrograph.engine.transformation.userfunctions.base.AggregatorTransformBase;
import hydrograph.engine.transformation.userfunctions.base.BufferField;
import hydrograph.engine.transformation.userfunctions.base.BufferSchema;
import hydrograph.engine.transformation.userfunctions.base.ReusableRow;

public class CountUDAF implements AggregatorTransformBase {

    @Override
    public BufferSchema initBufferSchema() {
        BufferField count = new BufferField.Builder("count", "Long").build();
        BufferField sum = new BufferField.Builder("sum", "Long").build();
        BufferSchema bufferSchema = new BufferSchema();
        bufferSchema.addField("count", count);
        bufferSchema.addField("sum", sum);
        return bufferSchema;
    }

    @Override
    public void initialize(ReusableRow bufferRow) {
        bufferRow.setField("count", 0L);
        bufferRow.setField("sum", 0L);
    }

    @Override
    public void update(ReusableRow bufferRow, ReusableRow inputRow) {
        bufferRow.setField("count", ((Long) bufferRow.getField("count")) + 1L);
        bufferRow.setField("sum", ((Long) bufferRow.getField("sum")) + inputRow.getInteger("yearofApplying"));
    }

    @Override
    public void merge(ReusableRow bufferRow1, ReusableRow bufferRow2) {
        bufferRow1.setField("count", ((Long) bufferRow1.getField("count")) + ((Long) bufferRow2.getField("count")));
        bufferRow1.setField("sum", ((Long) bufferRow1.getField("sum")) + ((Long) bufferRow2.getField("sum")));
    }

    @Override
    public ReusableRow evaluate(ReusableRow bufferRow, ReusableRow outRow) {
        outRow.setField("sum", bufferRow.getField("sum"));
        outRow.setField("count", bufferRow.getField("count"));
        return outRow;
    }
}
