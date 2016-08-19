package hydrograph.ui.graph.model;

/**
 * Execution tracking status.
 *
 * @author Bitwise
 */
public enum CompStatus {
	
	/** The blank. */
	BLANK("BLANK"),
	
	/** The pending. */
	PENDING("PENDING"), 
	
	/** The running. */
	RUNNING("RUNNING"), 
	
	/** The successful. */
	SUCCESSFUL("SUCCESSFUL"),
	
	/** The failed. */
	FAILED("FAILED")
	;
	
	/** The value. */
	private final String value;
	
	/**
	 * Instantiates a new comp status.
	 *
	 * @param v the v
	 */
	CompStatus(String v) {
        value = v;
    }

    /**
     * Value.
     *
     * @return the string
     */
    public String value() {
        return value;
    }

    /**
     * From value.
     *
     * @param v the v
     * @return the comp status
     */
    public static CompStatus fromValue(String v) {
        for (CompStatus c: CompStatus.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
}