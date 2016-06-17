package hydrograph.ui.dataviewer.support;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public enum SortDataType {
	STRING {
		@Override
		protected int compareTo(String cell_1, String cell_2, String dateFormat) {
			return cell_1.compareTo(cell_2);
		}
		@Override
		public String getDataType() {
			return String.class.getName();
		}
	},
	INTEGER {
		@Override
		protected int compareTo(String cell_1, String cell_2, String dateFormat) {
			return Integer.valueOf(cell_1).compareTo(Integer.valueOf(cell_2));
		}
		@Override
		public String getDataType() {
			return Integer.class.getName();
		}
	},
	DOUBLE {
		@Override
		protected int compareTo(String cell_1, String cell_2, String dateFormat) {
			return Double.valueOf(cell_1).compareTo(Double.valueOf(cell_2));
		}
		@Override
		public String getDataType() {
			return Double.class.getName();
		}
	},
	FLOAT {
		@Override
		protected int compareTo(String cell_1, String cell_2, String dateFormat) {
			return Float.valueOf(cell_1).compareTo(Float.valueOf(cell_2));
		}
		@Override
		public String getDataType() {
			return Float.class.getName();
		}
	},
	SHORT {
		@Override
		protected int compareTo(String cell_1, String cell_2, String dateFormat) {
			return Short.valueOf(cell_1).compareTo(Short.valueOf(cell_2));
		}
		@Override
		public String getDataType() {
			return Short.class.getName();
		}
	},
	BOOLEAN {
		@Override
		protected int compareTo(String cell_1, String cell_2, String dateFormat) {
			return Boolean.valueOf(cell_1).compareTo(Boolean.valueOf(cell_2));
		}
		@Override
		public String getDataType() {
			return Boolean.class.getName();
		}
	},
	DATE {
		@Override
		protected int compareTo(String cell_1, String cell_2, String dateFormat) {
			DateFormat format = new SimpleDateFormat(dateFormat);
			Date date1 = null;
			Date date2 = null;
			try {
				date1 = format.parse(cell_1);
				date2 = format.parse(cell_2);
			} catch (ParseException e) {
				// TODO : add logger
				// throw new exception;
			}
			return date1.compareTo(date2);
		}
		
		@Override
		public String getDataType() {
			return Date.class.getName();
		}
	},
	BIGDECIMAL {
		@Override
		protected int compareTo(String cell_1, String cell_2, String dateFormat) {
			return new BigDecimal(cell_1).compareTo(new BigDecimal(cell_2));
		}
		@Override
		public String getDataType() {
			return BigDecimal.class.getName();
		}
	},
	LONG {
		@Override
		protected int compareTo(String cell_1, String cell_2, String dateFormat) {
			return Long.valueOf(cell_1).compareTo(Long.valueOf(cell_2));
		}
		@Override
		public String getDataType() {
			return Long.class.getName();
		}
	};
	protected abstract int compareTo(String cell_1, String cell_2, String dateFormat);
	public abstract String getDataType();
}
