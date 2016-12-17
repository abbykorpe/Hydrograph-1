package hydrograph.engine.flow.utils;

import hydrograph.engine.batchbreak.plugin.BatchBreakPlugin;
import hydrograph.engine.core.flowmanipulation.FlowManipulationHandler;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by gurdits on 12/15/2016.
 */
public class DefaultPluginManipulation extends FlowManipulationHandler {

    enum Plugins {
        batchBreak {
            @Override
            public String getPluginName() {
                return BatchBreakPlugin.class.getName();
            }
        };
        public abstract String getPluginName();
    }

    @Override
    public List<String> addDefaultPlugins() {
        List<String> registerdPlugins = new LinkedList<String>();
        for (Plugins plugin : Plugins.values())
            registerdPlugins.add(plugin.getPluginName());
        return registerdPlugins;
    }
}
