
package hydrograph.engine.jaxb.inputtypes;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import hydrograph.engine.jaxb.commontypes.ElementValueIntegerType;
import hydrograph.engine.jaxb.commontypes.ElementValueStringType;
import hydrograph.engine.jaxb.commontypes.TypeProperties;
import hydrograph.engine.jaxb.iredshift.TypeInputRedshiftBase;


/**
 * <p>Java class for redshift complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="redshift">
 *   &lt;complexContent>
 *     &lt;extension base="{hydrograph/engine/jaxb/iredshift}type-input-redshift-base">
 *       &lt;sequence>
 *         &lt;element name="databaseName" type="{hydrograph/engine/jaxb/commontypes}element-value-string-type"/>
 *         &lt;element name="tableName" type="{hydrograph/engine/jaxb/commontypes}element-value-string-type"/>
 *         &lt;element name="username" type="{hydrograph/engine/jaxb/commontypes}element-value-string-type"/>
 *         &lt;element name="password" type="{hydrograph/engine/jaxb/commontypes}element-value-string-type"/>
 *         &lt;element name="jdbcurl" type="{hydrograph/engine/jaxb/commontypes}element-value-string-type"/>
 *         &lt;element name="query" type="{hydrograph/engine/jaxb/commontypes}element-value-string-type" minOccurs="0"/>
 *         &lt;element name="condition" type="{hydrograph/engine/jaxb/commontypes}element-value-string-type" minOccurs="0"/>
 *         &lt;element name="batchSize" type="{hydrograph/engine/jaxb/commontypes}element-value-integer-type"/>
 *         &lt;element name="runtimeProperties" type="{hydrograph/engine/jaxb/commontypes}type-properties" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "redshift", namespace = "hydrograph/engine/jaxb/inputtypes", propOrder = {
    "databaseName",
    "tableName",
    "username",
    "password",
    "jdbcurl",
    "query",
    "condition",
    "batchSize",
    "runtimeProperties"
})
public class Redshift
    extends TypeInputRedshiftBase
{

    @XmlElement(required = true)
    protected ElementValueStringType databaseName;
    @XmlElement(required = true)
    protected ElementValueStringType tableName;
    @XmlElement(required = true)
    protected ElementValueStringType username;
    @XmlElement(required = true)
    protected ElementValueStringType password;
    @XmlElement(required = true)
    protected ElementValueStringType jdbcurl;
    protected ElementValueStringType query;
    protected ElementValueStringType condition;
    @XmlElement(required = true)
    protected ElementValueIntegerType batchSize;
    protected TypeProperties runtimeProperties;

    /**
     * Gets the value of the databaseName property.
     * 
     * @return
     *     possible object is
     *     {@link ElementValueStringType }
     *     
     */
    public ElementValueStringType getDatabaseName() {
        return databaseName;
    }

    /**
     * Sets the value of the databaseName property.
     * 
     * @param value
     *     allowed object is
     *     {@link ElementValueStringType }
     *     
     */
    public void setDatabaseName(ElementValueStringType value) {
        this.databaseName = value;
    }

    /**
     * Gets the value of the tableName property.
     * 
     * @return
     *     possible object is
     *     {@link ElementValueStringType }
     *     
     */
    public ElementValueStringType getTableName() {
        return tableName;
    }

    /**
     * Sets the value of the tableName property.
     * 
     * @param value
     *     allowed object is
     *     {@link ElementValueStringType }
     *     
     */
    public void setTableName(ElementValueStringType value) {
        this.tableName = value;
    }

    /**
     * Gets the value of the username property.
     * 
     * @return
     *     possible object is
     *     {@link ElementValueStringType }
     *     
     */
    public ElementValueStringType getUsername() {
        return username;
    }

    /**
     * Sets the value of the username property.
     * 
     * @param value
     *     allowed object is
     *     {@link ElementValueStringType }
     *     
     */
    public void setUsername(ElementValueStringType value) {
        this.username = value;
    }

    /**
     * Gets the value of the password property.
     * 
     * @return
     *     possible object is
     *     {@link ElementValueStringType }
     *     
     */
    public ElementValueStringType getPassword() {
        return password;
    }

    /**
     * Sets the value of the password property.
     * 
     * @param value
     *     allowed object is
     *     {@link ElementValueStringType }
     *     
     */
    public void setPassword(ElementValueStringType value) {
        this.password = value;
    }

    /**
     * Gets the value of the jdbcurl property.
     * 
     * @return
     *     possible object is
     *     {@link ElementValueStringType }
     *     
     */
    public ElementValueStringType getJdbcurl() {
        return jdbcurl;
    }

    /**
     * Sets the value of the jdbcurl property.
     * 
     * @param value
     *     allowed object is
     *     {@link ElementValueStringType }
     *     
     */
    public void setJdbcurl(ElementValueStringType value) {
        this.jdbcurl = value;
    }

    /**
     * Gets the value of the query property.
     * 
     * @return
     *     possible object is
     *     {@link ElementValueStringType }
     *     
     */
    public ElementValueStringType getQuery() {
        return query;
    }

    /**
     * Sets the value of the query property.
     * 
     * @param value
     *     allowed object is
     *     {@link ElementValueStringType }
     *     
     */
    public void setQuery(ElementValueStringType value) {
        this.query = value;
    }

    /**
     * Gets the value of the condition property.
     * 
     * @return
     *     possible object is
     *     {@link ElementValueStringType }
     *     
     */
    public ElementValueStringType getCondition() {
        return condition;
    }

    /**
     * Sets the value of the condition property.
     * 
     * @param value
     *     allowed object is
     *     {@link ElementValueStringType }
     *     
     */
    public void setCondition(ElementValueStringType value) {
        this.condition = value;
    }

    /**
     * Gets the value of the batchSize property.
     * 
     * @return
     *     possible object is
     *     {@link ElementValueIntegerType }
     *     
     */
    public ElementValueIntegerType getBatchSize() {
        return batchSize;
    }

    /**
     * Sets the value of the batchSize property.
     * 
     * @param value
     *     allowed object is
     *     {@link ElementValueIntegerType }
     *     
     */
    public void setBatchSize(ElementValueIntegerType value) {
        this.batchSize = value;
    }

    /**
     * Gets the value of the runtimeProperties property.
     * 
     * @return
     *     possible object is
     *     {@link TypeProperties }
     *     
     */
    public TypeProperties getRuntimeProperties() {
        return runtimeProperties;
    }

    /**
     * Sets the value of the runtimeProperties property.
     * 
     * @param value
     *     allowed object is
     *     {@link TypeProperties }
     *     
     */
    public void setRuntimeProperties(TypeProperties value) {
        this.runtimeProperties = value;
    }

}
