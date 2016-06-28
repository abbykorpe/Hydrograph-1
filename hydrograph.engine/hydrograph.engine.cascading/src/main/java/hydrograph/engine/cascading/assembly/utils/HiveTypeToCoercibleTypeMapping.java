package hydrograph.engine.cascading.assembly.utils;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hydrograph.engine.cascading.utilities.DataTypeCoerce;

public enum HiveTypeToCoercibleTypeMapping {

	INT(HiveType.INT), STRING(HiveType.STRING), DOUBLE(HiveType.DOUBLE), FLOAT(HiveType.FLOAT), SHORT(
			HiveType.SHORT), BOOLEAN(
					HiveType.BOOLEAN), DATE(HiveType.DATE), DECIMAL(HiveType.DECIMAL), LONG(HiveType.LONG);

	private final HiveType hiveType;

	HiveTypeToCoercibleTypeMapping(HiveType hiveType) {
		this.hiveType = hiveType;
	}

	private enum HiveType {

		INT {

			@Override
			Type getMapping(String hiveDataType) {

				return DataTypeCoerce.convertClassToCoercibleType(Integer.class, null, -999, null);
			}

		},

		STRING {
			@Override
			Type getMapping(String hiveDataType) {

				return DataTypeCoerce.convertClassToCoercibleType(String.class, null, -999, null);
			}
		},
		DOUBLE {
			@Override
			Type getMapping(String hiveDataType) {

				return DataTypeCoerce.convertClassToCoercibleType(Double.class, null, -999, null);
			}
		},
		FLOAT {
			@Override
			Type getMapping(String hiveDataType) {

				return DataTypeCoerce.convertClassToCoercibleType(Float.class, null, -999, null);
			}
		},
		SHORT {
			@Override
			Type getMapping(String hiveDataType) {

				return DataTypeCoerce.convertClassToCoercibleType(Short.class, null, -999, null);
			}
		},
		BOOLEAN {
			@Override
			Type getMapping(String hiveDataType) {

				return DataTypeCoerce.convertClassToCoercibleType(Boolean.class, null, -999, null);
			}
		},
		DATE {
			@Override
			Type getMapping(String hiveDataType) {

				return DataTypeCoerce.convertClassToCoercibleType(Date.class, "yyyy-mm-dd", -999, null);
			}
		},
		DECIMAL {
			@Override
			Type getMapping(String hiveDataType) {

				return DataTypeCoerce.convertClassToCoercibleType(BigDecimal.class, null, getScale(hiveDataType), null);
			}
		},
		LONG {
			@Override
			Type getMapping(String hiveDataType) {

				return DataTypeCoerce.convertClassToCoercibleType(Long.class, null, -999, null);
			}
		},;

		abstract Type getMapping(String hiveDataType);

		int getScale(String typeWithScale) {

			String pattern = "decimal\\((\\d+),(\\d+)\\)";
			Pattern r = Pattern.compile(pattern);

			Matcher m = r.matcher(typeWithScale);
			if (m.find()) {
				return Integer.parseInt(m.group(2));
			} else {
				return -999;
			}
		}

	}

	public Type getMappingType(String tableDescriptor) {
		return hiveType.getMapping(tableDescriptor);

	}
}
