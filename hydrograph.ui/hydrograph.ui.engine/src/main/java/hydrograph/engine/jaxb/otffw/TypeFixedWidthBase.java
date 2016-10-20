/********************************************************************************
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
 ******************************************************************************/

package hydrograph.engine.jaxb.otffw;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import hydrograph.engine.jaxb.commontypes.TypeOutputComponent;
import hydrograph.engine.jaxb.outputtypes.TextFileFixedWidth;


/**
 * <p>Java class for type-fixed-width-base complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="type-fixed-width-base">
 *   &lt;complexContent>
 *     &lt;restriction base="{hydrograph/engine/jaxb/commontypes}type-output-component">
 *       &lt;sequence>
 *         &lt;element name="inSocket" type="{hydrograph/engine/jaxb/otffw}type-output-fixedwidth-in-socket"/>
 *         &lt;element name="overWrite" type="{hydrograph/engine/jaxb/commontypes}type-true-false" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "type-fixed-width-base")
@XmlSeeAlso({
    TextFileFixedWidth.class
})
public class TypeFixedWidthBase
    extends TypeOutputComponent
{


}
