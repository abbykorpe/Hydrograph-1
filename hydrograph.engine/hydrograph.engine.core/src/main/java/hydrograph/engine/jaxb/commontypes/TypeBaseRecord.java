
package hydrograph.engine.jaxb.commontypes;

import hydrograph.engine.jaxb.igr.TypeGenerateRecordRecord;
import hydrograph.engine.jaxb.omysql.TypeMysqlRecord;
import hydrograph.engine.jaxb.ooracle.TypeOracleRecord;
import hydrograph.engine.jaxb.oredshift.TypeRedshiftRecord;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for type-base-record complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="type-base-record">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice maxOccurs="unbounded">
 *         &lt;element name="field" type="{hydrograph/engine/jaxb/commontypes}type-base-field"/>
 *         &lt;element name="record" type="{hydrograph/engine/jaxb/commontypes}type-base-record"/>
 *         &lt;element name="includeExternalSchema" type="{hydrograph/engine/jaxb/commontypes}type-external-schema" minOccurs="0"/>
 *       &lt;/choice>
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "type-base-record", namespace = "hydrograph/engine/jaxb/commontypes", propOrder = {
    "fieldOrRecordOrIncludeExternalSchema"
})
@XmlSeeAlso({
    hydrograph.engine.jaxb.itffw.TypeFixedwidthRecord.class,
    TypeGenerateRecordRecord.class,
    hydrograph.engine.jaxb.ifmixedscheme.TypeMixedRecord.class,
    hydrograph.engine.jaxb.otffw.TypeFixedwidthRecord.class,
    hydrograph.engine.jaxb.ofmixedscheme.TypeMixedRecord.class,
    TypeMysqlRecord.class,
    TypeRedshiftRecord.class,
    TypeOracleRecord.class
})
public class TypeBaseRecord {

    @XmlElements({
        @XmlElement(name = "field", type = TypeBaseField.class),
        @XmlElement(name = "includeExternalSchema", type = TypeExternalSchema.class),
        @XmlElement(name = "record", type = TypeBaseRecord.class)
    })
    protected List<Object> fieldOrRecordOrIncludeExternalSchema;
    @XmlAttribute(name = "name", required = true)
    protected String name;

    /**
     * Gets the value of the fieldOrRecordOrIncludeExternalSchema property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the fieldOrRecordOrIncludeExternalSchema property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFieldOrRecordOrIncludeExternalSchema().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TypeBaseField }
     * {@link TypeExternalSchema }
     * {@link TypeBaseRecord }
     * 
     * 
     */
    public List<Object> getFieldOrRecordOrIncludeExternalSchema() {
        if (fieldOrRecordOrIncludeExternalSchema == null) {
            fieldOrRecordOrIncludeExternalSchema = new ArrayList<Object>();
        }
        return this.fieldOrRecordOrIncludeExternalSchema;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

}
