/*******************************************************************************
 * Copyright 2016 Capital One Services, LLC and Bitwise, Inc.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/

package hydrograph.engine.jaxb.inputtypes;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import hydrograph.engine.jaxb.commontypes.ElementValueIntegerType;
import hydrograph.engine.jaxb.commontypes.ElementValueStringType;
import hydrograph.engine.jaxb.commontypes.TypeProperties;
import hydrograph.engine.jaxb.ioracle.TypeInputOracleBase;


/**
 * <p>Java class for oracle complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="oracle">
 *   &lt;complexContent>
 *     &lt;extension base="{hydrograph/engine/jaxb/ioracle}type-input-oracle-base">
 *       &lt;sequence>
 *         &lt;element name="sid" type="{hydrograph/engine/jaxb/commontypes}element-value-string-type"/>
 *         &lt;element name="hostname" type="{hydrograph/engine/jaxb/commontypes}element-value-string-type"/>
 *         &lt;element name="port" type="{hydrograph/engine/jaxb/commontypes}element-value-integer-type" minOccurs="0"/>
 *         &lt;element name="drivertype" type="{hydrograph/engine/jaxb/commontypes}element-value-string-type"/>
 *         &lt;element name="username" type="{hydrograph/engine/jaxb/commontypes}element-value-string-type"/>
 *         &lt;element name="password" type="{hydrograph/engine/jaxb/commontypes}element-value-string-type"/>
 *         &lt;element name="runtimeProperties" type="{hydrograph/engine/jaxb/commontypes}type-properties" minOccurs="0"/>
 *         &lt;element name="schemaname" type="{hydrograph/engine/jaxb/commontypes}element-value-string-type" minOccurs="0"/>
 *         &lt;choice>
 *           &lt;element name="tableName" type="{hydrograph/engine/jaxb/commontypes}element-value-string-type" minOccurs="0"/>
 *           &lt;sequence>
 *             &lt;element name="selectQuery" type="{hydrograph/engine/jaxb/commontypes}element-value-string-type"/>
 *             &lt;element name="countQuery" type="{hydrograph/engine/jaxb/commontypes}element-value-string-type" minOccurs="0"/>
 *           &lt;/sequence>
 *         &lt;/choice>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "oracle", propOrder = {
    "sid",
    "hostname",
    "port",
    "drivertype",
    "username",
    "password",
    "runtimeProperties",
    "schemaname",
    "tableName",
    "selectQuery",
    "countQuery"
})
public class Oracle
    extends TypeInputOracleBase
{

    @XmlElement(required = true)
    protected ElementValueStringType sid;
    @XmlElement(required = true)
    protected ElementValueStringType hostname;
    protected ElementValueIntegerType port;
    @XmlElement(required = true)
    protected ElementValueStringType drivertype;
    @XmlElement(required = true)
    protected ElementValueStringType username;
    @XmlElement(required = true)
    protected ElementValueStringType password;
    protected TypeProperties runtimeProperties;
    protected ElementValueStringType schemaname;
    protected ElementValueStringType tableName;
    protected ElementValueStringType selectQuery;
    protected ElementValueStringType countQuery;

    /**
     * Gets the value of the sid property.
     * 
     * @return
     *     possible object is
     *     {@link ElementValueStringType }
     *     
     */
    public ElementValueStringType getSid() {
        return sid;
    }

    /**
     * Sets the value of the sid property.
     * 
     * @param value
     *     allowed object is
     *     {@link ElementValueStringType }
     *     
     */
    public void setSid(ElementValueStringType value) {
        this.sid = value;
    }

    /**
     * Gets the value of the hostname property.
     * 
     * @return
     *     possible object is
     *     {@link ElementValueStringType }
     *     
     */
    public ElementValueStringType getHostname() {
        return hostname;
    }

    /**
     * Sets the value of the hostname property.
     * 
     * @param value
     *     allowed object is
     *     {@link ElementValueStringType }
     *     
     */
    public void setHostname(ElementValueStringType value) {
        this.hostname = value;
    }

    /**
     * Gets the value of the port property.
     * 
     * @return
     *     possible object is
     *     {@link ElementValueIntegerType }
     *     
     */
    public ElementValueIntegerType getPort() {
        return port;
    }

    /**
     * Sets the value of the port property.
     * 
     * @param value
     *     allowed object is
     *     {@link ElementValueIntegerType }
     *     
     */
    public void setPort(ElementValueIntegerType value) {
        this.port = value;
    }

    /**
     * Gets the value of the drivertype property.
     * 
     * @return
     *     possible object is
     *     {@link ElementValueStringType }
     *     
     */
    public ElementValueStringType getDrivertype() {
        return drivertype;
    }

    /**
     * Sets the value of the drivertype property.
     * 
     * @param value
     *     allowed object is
     *     {@link ElementValueStringType }
     *     
     */
    public void setDrivertype(ElementValueStringType value) {
        this.drivertype = value;
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

    /**
     * Gets the value of the schemaname property.
     * 
     * @return
     *     possible object is
     *     {@link ElementValueStringType }
     *     
     */
    public ElementValueStringType getSchemaname() {
        return schemaname;
    }

    /**
     * Sets the value of the schemaname property.
     * 
     * @param value
     *     allowed object is
     *     {@link ElementValueStringType }
     *     
     */
    public void setSchemaname(ElementValueStringType value) {
        this.schemaname = value;
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
     * Gets the value of the selectQuery property.
     * 
     * @return
     *     possible object is
     *     {@link ElementValueStringType }
     *     
     */
    public ElementValueStringType getSelectQuery() {
        return selectQuery;
    }

    /**
     * Sets the value of the selectQuery property.
     * 
     * @param value
     *     allowed object is
     *     {@link ElementValueStringType }
     *     
     */
    public void setSelectQuery(ElementValueStringType value) {
        this.selectQuery = value;
    }

    /**
     * Gets the value of the countQuery property.
     * 
     * @return
     *     possible object is
     *     {@link ElementValueStringType }
     *     
     */
    public ElementValueStringType getCountQuery() {
        return countQuery;
    }

    /**
     * Sets the value of the countQuery property.
     * 
     * @param value
     *     allowed object is
     *     {@link ElementValueStringType }
     *     
     */
    public void setCountQuery(ElementValueStringType value) {
        this.countQuery = value;
    }

}
