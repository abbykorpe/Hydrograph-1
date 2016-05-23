package hydrograph.ui.propertywindow.widgets.utility;

public enum Extensions {
	JAVA("java"),
	SCHEMA("schema"),
	JOB("job");
	
private final String extension;

/**
 * @param extension
 */
private Extensions(final String extension) {
    this.extension = extension;
}

/* (non-Javadoc)
 * @see java.lang.Enum#toString()
 */
@Override
public String toString() {
    return extension;
}


}

