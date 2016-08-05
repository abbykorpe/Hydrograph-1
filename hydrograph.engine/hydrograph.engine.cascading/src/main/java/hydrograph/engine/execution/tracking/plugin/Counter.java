package hydrograph.engine.execution.tracking.plugin;

import cascading.flow.FlowProcess;
import cascading.operation.BaseOperation;
import cascading.operation.Filter;
import cascading.operation.FilterCall;
import cascading.operation.OperationCall;
import cascading.tuple.Fields;
import hydrograph.engine.cascading.assembly.context.RecordFilterContext;

public class Counter extends BaseOperation<RecordFilterContext>implements Filter<RecordFilterContext> {

	@Override
	public boolean isRemove(FlowProcess flowProcess, FilterCall<RecordFilterContext> call) {
		flowProcess.increment(call.getContext().getCounterGroup(), call.getContext().getCounterName(), 1);
		return false;
	}


}