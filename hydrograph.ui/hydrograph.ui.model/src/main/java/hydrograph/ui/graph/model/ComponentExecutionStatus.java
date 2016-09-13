package hydrograph.ui.graph.model;


/**
 * The Enum ComponentExecutionStatus.
 * <p>
 * Stores the component execution status values
 * 
 * @author Bitwise
 */
public enum ComponentExecutionStatus {
	
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
	ComponentExecutionStatus(String v) {
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
    public static ComponentExecutionStatus fromValue(String v) {
        for (ComponentExecutionStatus c: ComponentExecutionStatus.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
}