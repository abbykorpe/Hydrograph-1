package hydrograph.engine.transformation.userfunctions.base;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;

public class ListBasedReusableRow extends ReusableRow {

	private ArrayList<Comparable> values;
	private HashMap<String, Integer> fieldPos;

	public ListBasedReusableRow(LinkedHashSet<String> fields) {
		super(fields);
		values = new ArrayList<Comparable>();
		fieldPos = new HashMap<String, Integer>();
		int i = -1;
		for (String field : fields) {
			i = i + 1;
			values.add(null);
			fieldPos.put(field, new Integer(i));
		}
	}

	@Override
	protected Comparable getFieldInternal(int index) {
		return (Comparable) values.get(index);
	}

	@Override
	protected Comparable getFieldInternal(String fieldName) {
		return (Comparable) values.get(fieldPos.get(fieldName));
	}

	@Override
	protected void setFieldInternal(int index, Comparable value) {
		values.set(index, value);

	}

	@Override
	protected void setFieldInternal(String fieldName, Comparable value) {
		values.set(fieldPos.get(fieldName), value);

	}

	@Override
	public Collection<Comparable> getFields() {
		return values;
	}

}
