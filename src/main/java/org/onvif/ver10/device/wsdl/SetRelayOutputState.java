//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5-2 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// �nderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2014.02.04 um 12:22:03 PM CET 
//

package org.onvif.ver10.device.wsdl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.onvif.ver10.schema.RelayLogicalState;

/**
 * <p>
 * Java-Klasse f�r anonymous complex type.
 * 
 * <p>
 * Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * <complexType>
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="RelayOutputToken" type="{http://www.onvif.org/ver10/schema}ReferenceToken"/>
 *         <element name="LogicalState" type="{http://www.onvif.org/ver10/schema}RelayLogicalState"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "relayOutputToken", "logicalState" })
@XmlRootElement(name = "SetRelayOutputState")
public class SetRelayOutputState {

	@XmlElement(name = "RelayOutputToken", required = true)
	protected String relayOutputToken;
	@XmlElement(name = "LogicalState", required = true)
	protected RelayLogicalState logicalState;

	/**
	 * Ruft den Wert der relayOutputToken-Eigenschaft ab.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getRelayOutputToken() {
		return relayOutputToken;
	}

	/**
	 * Legt den Wert der relayOutputToken-Eigenschaft fest.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setRelayOutputToken(String value) {
		this.relayOutputToken = value;
	}

	/**
	 * Ruft den Wert der logicalState-Eigenschaft ab.
	 * 
	 * @return possible object is {@link RelayLogicalState }
	 * 
	 */
	public RelayLogicalState getLogicalState() {
		return logicalState;
	}

	/**
	 * Legt den Wert der logicalState-Eigenschaft fest.
	 * 
	 * @param value
	 *            allowed object is {@link RelayLogicalState }
	 * 
	 */
	public void setLogicalState(RelayLogicalState value) {
		this.logicalState = value;
	}

}
