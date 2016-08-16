package hydrograph.ui.graph.model;


public enum CompStatus {
	BLANK("BLANK"),
	PENDING("PENDING"), 
	RUNNING("RUNNING"), 
	SUCCESSFUL("SUCCESSFUL"),
	FAILED("FAILED")
	;
	private final String value;
	
	CompStatus(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static CompStatus fromValue(String v) {
        for (CompStatus c: CompStatus.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
}