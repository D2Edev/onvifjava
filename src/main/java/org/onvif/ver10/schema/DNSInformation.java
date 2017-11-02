//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5-2 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// �nderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2014.02.04 um 12:22:03 PM CET 
//

package org.onvif.ver10.schema;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.namespace.QName;

/**
 * <p>
 * Java-Klasse f�r DNSInformation complex type.
 * 
 * <p>
 * Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * <complexType name="DNSInformation">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="FromDHCP" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         <element name="SearchDomain" type="{http://www.w3.org/2001/XMLSchema}token" maxOccurs="unbounded" minOccurs="0"/>
 *         <element name="DNSFromDHCP" type="{http://www.onvif.org/ver10/schema}IPAddress" maxOccurs="unbounded" minOccurs="0"/>
 *         <element name="DNSManual" type="{http://www.onvif.org/ver10/schema}IPAddress" maxOccurs="unbounded" minOccurs="0"/>
 *         <element name="Extension" type="{http://www.onvif.org/ver10/schema}DNSInformationExtension" minOccurs="0"/>
 *       </sequence>
 *       <anyAttribute processContents='lax'/>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DNSInformation", propOrder = { "fromDHCP", "searchDomain", "dnsFromDHCP", "dnsManual", "extension" })
public class DNSInformation {

	@XmlElement(name = "FromDHCP")
	protected boolean fromDHCP;
	@XmlElement(name = "SearchDomain")
	@XmlJavaTypeAdapter(CollapsedStringAdapter.class)
	@XmlSchemaType(name = "token")
	protected List<String> searchDomain;
	@XmlElement(name = "DNSFromDHCP")
	protected List<IPAddress> dnsFromDHCP;
	@XmlElement(name = "DNSManual")
	protected List<IPAddress> dnsManual;
	@XmlElement(name = "Extension")
	protected DNSInformationExtension extension;
	@XmlAnyAttribute
	private Map<QName, String> otherAttributes = new HashMap<QName, String>();

	/**
	 * Ruft den Wert der fromDHCP-Eigenschaft ab.
	 * 
	 */
	public boolean isFromDHCP() {
		return fromDHCP;
	}

	/**
	 * Legt den Wert der fromDHCP-Eigenschaft fest.
	 * 
	 */
	public void setFromDHCP(boolean value) {
		this.fromDHCP = value;
	}

	/**
	 * Gets the value of the searchDomain property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to the returned list will be present inside the JAXB object.
	 * This is why there is not a <CODE>set</CODE> method for the searchDomain property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getSearchDomain().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link String }
	 * 
	 * 
	 */
	public List<String> getSearchDomain() {
		if (searchDomain == null) {
			searchDomain = new ArrayList<String>();
		}
		return this.searchDomain;
	}

	/**
	 * Gets the value of the dnsFromDHCP property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to the returned list will be present inside the JAXB object.
	 * This is why there is not a <CODE>set</CODE> method for the dnsFromDHCP property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getDNSFromDHCP().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link IPAddress }
	 * 
	 * 
	 */
	public List<IPAddress> getDNSFromDHCP() {
		if (dnsFromDHCP == null) {
			dnsFromDHCP = new ArrayList<IPAddress>();
		}
		return this.dnsFromDHCP;
	}

	/**
	 * Gets the value of the dnsManual property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to the returned list will be present inside the JAXB object.
	 * This is why there is not a <CODE>set</CODE> method for the dnsManual property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getDNSManual().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link IPAddress }
	 * 
	 * 
	 */
	public List<IPAddress> getDNSManual() {
		if (dnsManual == null) {
			dnsManual = new ArrayList<IPAddress>();
		}
		return this.dnsManual;
	}

	/**
	 * Ruft den Wert der extension-Eigenschaft ab.
	 * 
	 * @return possible object is {@link DNSInformationExtension }
	 * 
	 */
	public DNSInformationExtension getExtension() {
		return extension;
	}

	/**
	 * Legt den Wert der extension-Eigenschaft fest.
	 * 
	 * @param value
	 *            allowed object is {@link DNSInformationExtension }
	 * 
	 */
	public void setExtension(DNSInformationExtension value) {
		this.extension = value;
	}

	/**
	 * Gets a map that contains attributes that aren't bound to any typed property on this class.
	 * 
	 * <p>
	 * the map is keyed by the name of the attribute and the value is the string value of the attribute.
	 * 
	 * the map returned by this method is live, and you can add new attribute by updating the map directly. Because of this design, there's no setter.
	 * 
	 * 
	 * @return always non-null
	 */
	public Map<QName, String> getOtherAttributes() {
		return otherAttributes;
	}

}
