package hydrograph.engine.core.component.utils;

/**
 * Created by santlalg on 12/12/2016.
 */
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

    /**
     * this will map java data type to specific database type like mysql,oracle,teradata,redshit
     //* @param String databaseType
     //* @param String[] fieldsDataType
     //@param int[] fieldsScale,
     //@param int[] fieldsPrecision
     *
     * return String[] of database type*/

    public static String[] createTypeMapping(String databaseType, String[] fieldsDataType, int[] fieldsScale,
                                             int[] fieldsPrecision) {
        Map<String, String> map = selectMapping(databaseType).mapping();
        String[] arr = new String[fieldsDataType.length];
        int counter = 0;
        for(int i=0;i<fieldsDataType.length;i++){
            if(fieldsDataType[i].equals("java.math.BigDecimal"))
                arr[i] = map.get(fieldsDataType[i]) + "(" + fieldsPrecision[i] + ","+ fieldsScale[i] +")";
            else
                arr[i] = map.get(fieldsDataType[i]);
        }
        return arr;
    }

    static class NoJavaTODBTypeMappingFound extends RuntimeException{
        private static final long serialVersionUID = 1L;
    }


}
