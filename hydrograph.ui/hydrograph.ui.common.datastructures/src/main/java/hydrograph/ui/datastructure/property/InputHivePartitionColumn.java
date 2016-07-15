package hydrograph.ui.datastructure.property;

import hydrograph.ui.common.cloneableinterface.IDataStructure;

public class InputHivePartitionColumn  implements IDataStructure{
	InputHivePartitionColumn inputHivePartitionColumn;
	String name;
	String value;
	
	public InputHivePartitionColumn getInputHivePartitionColumn() {
		return inputHivePartitionColumn;
	}
	public void setInputHivePartitionColumn(
			InputHivePartitionColumn inputHivePartitionColumn) {
		this.inputHivePartitionColumn = inputHivePartitionColumn;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	@Override
	public Object clone() {
		InputHivePartitionColumn column= new InputHivePartitionColumn();
		//add clone related setters.
		return column;
	}
}
