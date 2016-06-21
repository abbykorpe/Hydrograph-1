package hydrograph.engine.expression.antlr.custom.visitor;

public enum ClassToDataTypeConversion {

	String {
		@Override
		public String getValue(String fieldName) {
			return "String " + fieldName + "=null;";
		}

	},
	Integer {
		@Override
		public String getValue(String fieldName) {
			return "int " + fieldName + "=0;";
		}

	},
	Float {
		@Override
		public String getValue(String fieldName) {
			return "float " + fieldName + "=1.0;";
		}

	},
	Double {
		@Override
		public String getValue(String fieldName) {
			return "double " + fieldName + "=1.0;";
		}

	},
	Long {
		@Override
		public String getValue(String fieldName) {
			return "long " + fieldName + "=1;";
		}

	},
	Date {
		@Override
		public String getValue(String fieldName) {
			return "Date " + fieldName + "=null;";
		}

	},
	BigDecimal {
		@Override
		public String getValue(String fieldName) {
			return "BigDecimal " + fieldName + "=null;";
		}

	},
	Short {
		@Override
		public String getValue(String fieldName) {
			return "short " + fieldName + "=1;";
		}

	},
	Boolean {
		@Override
		public String getValue(String fieldName) {
			return "boolean" + fieldName + "=true;";
		}

	};

	private String expr;

	public abstract String getValue(String fieldName);

	public String get() {
		return expr;
	}
}
