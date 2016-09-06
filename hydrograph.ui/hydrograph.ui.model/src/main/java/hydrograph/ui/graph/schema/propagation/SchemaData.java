package hydrograph.ui.graph.schema.propagation;

import hydrograph.ui.datastructure.property.ComponentsOutputSchema;
import hydrograph.ui.datastructure.property.FixedWidthGridRow;
import hydrograph.ui.graph.model.Component;
import hydrograph.ui.graph.model.Link;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class SchemaData {

	
	public static Map<String, List<FixedWidthGridRow>> getInputSchema(Component component){
		if(component!=null){
		TreeMap<String, List<FixedWidthGridRow>>  inputSchemaMap =new TreeMap<>();component.getTargetConnections();
		for(Link link:component.getTargetConnections()){
			ComponentsOutputSchema componentsOutputSchema=SchemaPropagation.INSTANCE.getComponentsOutputSchema(link);
			//inputSchemaMap.put(link.getTargetTerminal(),componentsOutputSchema.getFixedWidthGridRowsOutputFields());
			inputSchemaMap.put(link.getTargetTerminal(),componentsOutputSchema.getFixedWidthGridRowsOutputFields());
		}
		return inputSchemaMap;
		}
		else{
			return null;
		}
	}
}


