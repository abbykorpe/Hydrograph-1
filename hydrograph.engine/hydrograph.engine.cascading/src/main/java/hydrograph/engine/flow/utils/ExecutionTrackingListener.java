package hydrograph.engine.flow.utils;

import java.util.List;

import cascading.stats.StatsListener;
import hydrograph.engine.cascading.integration.RuntimeContext;
import hydrograph.engine.execution.tracking.ComponentInfo;

public interface ExecutionTrackingListener extends StatsListener {

	public void addListener(RuntimeContext runtimeContext);
	
	public List<ComponentInfo> getStatus();
	
}
