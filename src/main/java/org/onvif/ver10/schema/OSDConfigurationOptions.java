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
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;

/**
 * <p>
 * Java-Klasse f�r OSDConfigurationOptions complex type.
 * 
 * <p>
 * Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * <complexType name="OSDConfigurationOptions">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="MaximumNumberOfOSDs" type="{http://www.onvif.org/ver10/schema}MaximumNumberOfOSDs"/>
 *         <element name="Type" type="{http://www.onvif.org/ver10/schema}OSDType" maxOccurs="unbounded"/>
 *         <element name="PositionOption" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded"/>
 *         <element name="TextOption" type="{http://www.onvif.org/ver10/schema}OSDTextOptions" minOccurs="0"/>
 *         <element name="ImageOption" type="{http://www.onvif.org/ver10/schema}OSDImgOptions" minOccurs="0"/>
 *         <element name="Extension" type="{http://www.onvif.org/ver10/schema}OSDConfigurationOptionsExtension" minOccurs="0"/>
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
@XmlType(name = "OSDConfigurationOptions", propOrder = { "maximumNumberOfOSDs", "type", "positionOption", "textOption", "imageOption", "extension" })
public class OSDConfigurationOptions {

	@XmlElement(name = "MaximumNumberOfOSDs", required = true)
	protected MaximumNumberOfOSDs maximumNumberOfOSDs;
	@XmlElement(name = "Type", required = true)
	protected List<OSDType> type;
	@XmlElement(name = "PositionOption", required = true)
	protected List<String> positionOption;
	@XmlElement(name = "TextOption")
	protected OSDTextOptions textOption;
	@XmlElement(name = "ImageOption")
	protected OSDImgOptions imageOption;
	@XmlElement(name = "Extension")
	protected OSDConfigurationOptionsExtension extension;
	@XmlAnyAttribute
	private Map<QName, String> otherAttributes = new HashMap<QName, String>();

	/**
	 * Ruft den Wert der maximumNumberOfOSDs-Eigenschaft ab.
	 * 
	 * @return possible object is {@link MaximumNumberOfOSDs }
	 * 
	 */
	public MaximumNumberOfOSDs getMaximumNumberOfOSDs() {
		return maximumNumberOfOSDs;
	}

	/**
	 * Legt den Wert der maximumNumberOfOSDs-Eigenschaft fest.
	 * 
	 * @param value
	 *            allowed object is {@link MaximumNumberOfOSDs }
	 * 
	 */
	public void setMaximumNumberOfOSDs(MaximumNumberOfOSDs value) {
		this.maximumNumberOfOSDs = value;
	}

	/**
	 * Gets the value of the type property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to the returned list will be present inside the JAXB object.
	 * This is why there is not a <CODE>set</CODE> method for the type property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getType().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link OSDType }
	 * 
	 * 
	 */
	public List<OSDType> getType() {
		if (type == null) {
			type = new ArrayList<OSDType>();
		}
		return this.type;
	}

	/**
	 * Gets the value of the positionOption property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to the returned list will be present inside the JAXB object.
	 * This is why there is not a <CODE>set</CODE> method for the positionOption property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getPositionOption().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link String }
	 * 
	 * 
	 */
	public List<String> getPositionOption() {
		if (positionOption == null) {
			positionOption = new ArrayList<String>();
		}
		return this.positionOption;
	}

	/**
	 * Ruft den Wert der textOption-Eigenschaft ab.
	 * 
	 * @return possible object is {@link OSDTextOptions }
	 * 
	 */
	public OSDTextOptions getTextOption() {
		return textOption;
	}

	/**
	 * Legt den Wert der textOption-Eigenschaft fest.
	 * 
	 * @param value
	 *            allowed object is {@link OSDTextOptions }
	 * 
	 */
	public void setTextOption(OSDTextOptions value) {
		this.textOption = value;
	}

	/**
	 * Ruft den Wert der imageOption-Eigenschaft ab.
	 * 
	 * @return possible object is {@link OSDImgOptions }
	 * 
	 */
	public OSDImgOptions getImageOption() {
		return imageOption;
	}

	/**
	 * Legt den Wert der imageOption-Eigenschaft fest.
	 * 
	 * @param value
	 *            allowed object is {@link OSDImgOptions }
	 * 
	 */
	public void setImageOption(OSDImgOptions value) {
		this.imageOption = value;
	}

	/**
	 * Ruft den Wert der extension-Eigenschaft ab.
	 * 
	 * @return possible object is {@link OSDConfigurationOptionsExtension }
	 * 
	 */
	public OSDConfigurationOptionsExtension getExtension() {
		return extension;
	}

	/**
	 * Legt den Wert der extension-Eigenschaft fest.
	 * 
	 * @param value
	 *            allowed object is {@link OSDConfigurationOptionsExtension }
	 * 
	 */
	public void setExtension(OSDConfigurationOptionsExtension value) {
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
