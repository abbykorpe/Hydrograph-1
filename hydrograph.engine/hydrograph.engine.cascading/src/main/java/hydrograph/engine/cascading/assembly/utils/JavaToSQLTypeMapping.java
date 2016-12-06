package hydrograph.engine.cascading.assembly.utils;

import java.util.HashMap;
import java.util.Map;

public enum JavaToSQLTypeMapping {
	MySQL(){
		@Override
		Map<String, String> mapping() {
			return new HashMap<String, String>(){
				private static final long serialVersionUID = 1L;
			{
				put("java.lang.String","VARCHAR(256)");
				put("java.lang.Integer","INT");
				put("java.lang.Long","BIGINT");
				put("java.lang.Double","DOUBLE");
				put("java.lang.Float","FLOAT");
				put("java.lang.Short","SMALLINT");
				put("java.lang.Boolean","TINYINT");
				put("java.util.Date","TIMESTAMP");
				put("java.math.BigDecimal","DECIMAL");
			}};
		}
	},
	
	Redshift(){
		@Override
		Map<String, String> mapping() {
			return new HashMap<String, String>(){
				private static final long serialVersionUID = 1L;
			{
				put("java.lang.String","VARCHAR(256)");
				put("java.lang.Integer","INTEGER");
				put("java.lang.Long","BIGINT");
				put("java.lang.Double","DOUBLE PRECISION");
				put("java.lang.Float","FLOAT");
				put("java.lang.Short","SMALLINT");
				put("java.lang.Boolean","BOOLEAN");
				put("java.util.Date","TIMESTAMP");
				put("java.math.BigDecimal","DECIMAL");
			}};
		}
	},
	
	Teradata(){
		@Override
		Map<String, String> mapping() {
			return new HashMap<String, String>(){
				private static final long serialVersionUID = 1L;
			{
				put("java.lang.String","VARCHAR(256)");
				put("java.lang.Integer","INT");
				put("java.lang.Long","BIGINT");
				put("java.lang.Double","DOUBLE");
				put("java.lang.Float","FLOAT");
				put("java.lang.Short","SMALLINT");
				put("java.lang.Boolean","TINYINT");
				put("java.util.Date","TIMESTAMP");
				put("java.math.BigDecimal","DECIMAL");
			}};
		}
	},
	
	ORACLE(){
		@Override
		Map<String, String> mapping() {
			return new HashMap<String, String>(){
				private static final long serialVersionUID = 1L;
			{
				put("java.lang.String","VARCHAR(256)");
				put("java.lang.Integer","VARCHAR(256)");
				put("java.lang.Long","VARCHAR(256)");
				put("java.lang.Double","VARCHAR(256)");
				put("java.lang.Float","VARCHAR(256)");
				put("java.lang.Short","VARCHAR(256)");
				put("java.lang.Boolean","VARCHAR(256)");
				put("java.util.Date","VARCHAR(256)");
				put("java.math.BigDecimal","VARCHAR(256)");
				
			}};
		}
	}
	;	
	
	abstract Map<String,String> mapping();
	
	private static JavaToSQLTypeMapping selectMapping(String dbName) {
		for (JavaToSQLTypeMapping i : JavaToSQLTypeMapping.values()) {
			if(i.name().equalsIgnoreCase(dbName))
				return i;
		}
		throw new NoJavaTODBTypeMappingFound();
	}
	
	public static String[] createTypeMapping(String dbName,String[] dataType){
		Map<String, String> map = selectMapping(dbName).mapping();
		String[] arr = new String[dataType.length];
		int counter = 0;
		for (String key : dataType)
			arr[counter++] = map.get(key);
		return arr;
	}
	
	static class NoJavaTODBTypeMappingFound extends RuntimeException{
		private static final long serialVersionUID = 1L;
	}
}
