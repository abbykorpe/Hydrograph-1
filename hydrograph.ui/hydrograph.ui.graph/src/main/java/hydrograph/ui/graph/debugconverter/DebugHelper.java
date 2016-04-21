package hydrograph.ui.graph.debugconverter;

import hydrograph.ui.common.util.Constants;
import hydrograph.ui.graph.model.Component;
import hydrograph.ui.graph.model.Container;
import hydrograph.ui.graph.model.Link;
import hydrograph.ui.logging.factory.LogFactory;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.slf4j.Logger;

import com.thoughtworks.xstream.XStream;

/**
 * @author Bitwise
 *
 */
public class DebugHelper {

	private static Logger logger = LogFactory.INSTANCE.getLogger(DebugHelper.class);
	public static DebugHelper INSTANCE = new DebugHelper();
	

	/**
	 * This function used to return subgraph component_Id and socket_Id
	 *
	 */
	public String getSubgraphComponent(Component component) throws CoreException{
		Container container=null;
		if(StringUtils.equalsIgnoreCase(component.getComponentName(), Constants.SUBJOB_COMPONENT)){
			String subgraphFilePath=(String) component.getProperties().get(Constants.JOB_PATH);
			if(StringUtils.isNotBlank(subgraphFilePath)){
				IPath jobPath=new Path(subgraphFilePath);
				if(jobPath.toFile().exists()){
					XStream xs = new XStream();
					container=(Container) xs.fromXML(jobPath.toFile());
					List<Link> links = null;
					for(Component component_temp:container.getChildren()){
						if(StringUtils.equalsIgnoreCase(component_temp.getComponentLabel().getLabelContents(), "OutputSubjobComponent")){
							links=component_temp.getTargetConnections();
						}
					}
					for(Link str : links){
						String sub_comp = str.getSource().getComponentLabel().getLabelContents();
						String sub_comp_port = str.getSourceTerminal();
						return sub_comp+"."+sub_comp_port;
					}
				}
				else{
					if(ResourcesPlugin.getWorkspace().getRoot().getFile(jobPath).exists()){
						XStream xs = new XStream();
					container=(Container) xs.fromXML(ResourcesPlugin.getWorkspace().getRoot().getFile(jobPath).getContents(true));
					List<Link> links = null;
					for(Component component_temp:container.getChildren()){
						if(StringUtils.equalsIgnoreCase(component_temp.getComponentLabel().getLabelContents(), "OutputSubjobComponent")){
							links=component_temp.getTargetConnections();
						}
					}for(Link str : links){
						String sub_comp = str.getSource().getComponentLabel().getLabelContents();
						String sub_comp_port = str.getSourceTerminal();
						return sub_comp+"."+sub_comp_port;
					}
					}
				}
					}
				}
		return null;
	}
}
