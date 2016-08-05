package hydrograph.engine.execution.tracking.listener;

import cascading.stats.CascadingStats;
import cascading.stats.CascadingStats.Status;
import cascading.stats.StatsListener;
import hydrograph.engine.execution.tracking.JobInfo;
import hydrograph.engine.execution.tracking.JobInfo.ElementGraphNotFoundException;

public class ComponentStatsListener implements StatsListener {

	@Override
	public void notify(CascadingStats stats, Status fromStatus, Status toStatus) {
		try {
			JobInfo.getInstance().storeComponentStats(stats);
		} catch (ElementGraphNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

}
