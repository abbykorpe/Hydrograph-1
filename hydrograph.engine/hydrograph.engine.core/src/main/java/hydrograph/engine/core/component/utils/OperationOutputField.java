package hydrograph.engine.core.component.utils;

import java.io.Serializable;

/**
 * Created by arshadalis on 1/17/2017.
 */
public class OperationOutputField  implements Serializable{


    private String fieldName;
    private String dataType;
    private String format;
    private int scale;
    private int precision;


    public OperationOutputField(String fieldName, String dataType, String format, int scale, int precision) {

        this.fieldName = fieldName;
        this.dataType = dataType;
        this.format = format;
        this.scale = scale;
        this.precision = precision;
    }


    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public int getPrecision() {
        return precision;
    }

    public void setPrecision(int precision) {
        this.precision = precision;
    }


    @Override
    public String toString() {
        return "OperationOutputField{" +
                "fieldName='" + fieldName + '\'' +
                ", dataType='" + dataType + '\'' +
                ", format='" + format + '\'' +
                ", scale='" + scale + '\'' +
                ", precision='" + precision + '\'' +
                '}';
    }
}
