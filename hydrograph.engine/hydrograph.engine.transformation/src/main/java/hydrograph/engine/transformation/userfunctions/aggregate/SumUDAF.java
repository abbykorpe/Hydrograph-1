package hydrograph.engine.transformation.userfunctions.aggregate;

import hydrograph.engine.transformation.userfunctions.base.AggregatorTransformBase;
import hydrograph.engine.transformation.userfunctions.base.BufferField;
import hydrograph.engine.transformation.userfunctions.base.BufferSchema;
import hydrograph.engine.transformation.userfunctions.base.ReusableRow;

public class SumUDAF implements AggregatorTransformBase {

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
    public ReusableRow evaluate(ReusableRow bufferRow, ReusableRow outRow) {
        outRow.setField("sum", bufferRow.getField("sum"));
        return outRow;
    }
}
