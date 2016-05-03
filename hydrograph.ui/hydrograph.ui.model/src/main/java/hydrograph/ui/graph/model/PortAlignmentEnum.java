package hydrograph.ui.graph.model;


public enum PortAlignmentEnum {

	LEFT("left"), 
	RIGHT("right"), 
	BOTTOM("bottom");
	
	private final String value;
	
	PortAlignmentEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static PortAlignmentEnum fromValue(String v) {
        for (PortAlignmentEnum c: PortAlignmentEnum.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
}
