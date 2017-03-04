package hydrograph.engine.transformation.userfunctions.groupcombine;

import hydrograph.engine.transformation.userfunctions.base.GroupCombineTransformBase;
import hydrograph.engine.transformation.userfunctions.base.BufferField;
import hydrograph.engine.transformation.userfunctions.base.BufferSchema;
import hydrograph.engine.transformation.userfunctions.base.ReusableRow;

public class StringAppend implements GroupCombineTransformBase {

    @Override
    public BufferSchema initBufferSchema() {
        BufferField sum = new BufferField.Builder("stringAppend", "String").build();
        BufferSchema bufferSchema = new BufferSchema();
        bufferSchema.addField("stringAppend", sum);
        return bufferSchema;
    }

    @Override
    public void initialize(ReusableRow bufferRow) {
        bufferRow.setField("stringAppend", 0L);
    }

    @Override
    public void update(ReusableRow bufferRow, ReusableRow inputRow) {
        bufferRow.setField("stringAppend", ((Long) bufferRow.getField("stringAppend")) + inputRow.getLong("yearofApplying"));
    }

    @Override
    public void merge(ReusableRow bufferRow1, ReusableRow bufferRow2) {
        bufferRow1.setField("stringAppend", ((Long) bufferRow1.getField("stringAppend")) + ((Long) bufferRow2.getField("stringAppend")));
    }

    @Override
    public void evaluate(ReusableRow bufferRow, ReusableRow outRow) {
        outRow.setField("stringAppend", bufferRow.getField("stringAppend"));
    }
}
