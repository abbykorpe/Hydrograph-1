package hydrograph.ui.common.util;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

public class PathUtility {
	public static final PathUtility INSTANCE = new PathUtility();
	private PathUtility(){
		
	}
	public boolean isAbsolute(String filePath){
		IPath path = new Path(filePath);
		return path.isAbsolute();
	}

}
