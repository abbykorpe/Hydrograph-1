package hydrograph.engine.transformation.userfunctions.base;

import java.util.LinkedHashMap;

public class BufferSchema {

    LinkedHashMap<String, BufferField> fields = new LinkedHashMap<String, BufferField>();

    public LinkedHashMap<String, BufferField> getSchema() {
        return fields;
    }

    public void addField(String fName, BufferField bufferField) {
        fields.put(fName, bufferField);
    }

}
