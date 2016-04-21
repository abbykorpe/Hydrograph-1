package hydrograph.ui.graph.model;

public enum PortTypeEnum {

	IN("in"), 
	OUT("out"), 
	UNUSED("unused")
	;
	private final String value;
	
	PortTypeEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static PortTypeEnum fromValue(String v) {
        for (PortTypeEnum c: PortTypeEnum.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
}
