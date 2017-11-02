//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5-2 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// �nderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2014.02.19 um 02:35:56 PM CET 
//

package org.onvif.ver10.media.wsdl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;
import org.w3c.dom.Element;

/**
 * <p>
 * Java-Klasse f�r Capabilities complex type.
 * 
 * <p>
 * Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * <complexType name="Capabilities">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="ProfileCapabilities" type="{http://www.onvif.org/ver10/media/wsdl}ProfileCapabilities"/>
 *         <element name="StreamingCapabilities" type="{http://www.onvif.org/ver10/media/wsdl}StreamingCapabilities"/>
 *         <any processContents='lax' maxOccurs="unbounded" minOccurs="0"/>
 *       </sequence>
 *       <attribute name="SnapshotUri" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       <attribute name="Rotation" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       <attribute name="VideoSourceMode" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       <attribute name="OSD" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       <anyAttribute processContents='lax'/>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Capabilities", propOrder = { "profileCapabilities", "streamingCapabilities", "any" })
public class Capabilities {

	@XmlElement(name = "ProfileCapabilities", required = true)
	protected ProfileCapabilities profileCapabilities;
	@XmlElement(name = "StreamingCapabilities", required = true)
	protected StreamingCapabilities streamingCapabilities;
	@XmlAnyElement(lax = true)
	protected List<Object> any;
	@XmlAttribute(name = "SnapshotUri")
	protected Boolean snapshotUri;
	@XmlAttribute(name = "Rotation")
	protected Boolean rotation;
	@XmlAttribute(name = "VideoSourceMode")
	protected Boolean videoSourceMode;
	@XmlAttribute(name = "OSD")
	protected Boolean osd;
	@XmlAnyAttribute
	private Map<QName, String> otherAttributes = new HashMap<QName, String>();

	/**
	 * Ruft den Wert der profileCapabilities-Eigenschaft ab.
	 * 
	 * @return possible object is {@link ProfileCapabilities }
	 * 
	 */
	public ProfileCapabilities getProfileCapabilities() {
		return profileCapabilities;
	}

	/**
	 * Legt den Wert der profileCapabilities-Eigenschaft fest.
	 * 
	 * @param value
	 *            allowed object is {@link ProfileCapabilities }
	 * 
	 */
	public void setProfileCapabilities(ProfileCapabilities value) {
		this.profileCapabilities = value;
	}

	/**
	 * Ruft den Wert der streamingCapabilities-Eigenschaft ab.
	 * 
	 * @return possible object is {@link StreamingCapabilities }
	 * 
	 */
	public StreamingCapabilities getStreamingCapabilities() {
		return streamingCapabilities;
	}

	/**
	 * Legt den Wert der streamingCapabilities-Eigenschaft fest.
	 * 
	 * @param value
	 *            allowed object is {@link StreamingCapabilities }
	 * 
	 */
	public void setStreamingCapabilities(StreamingCapabilities value) {
		this.streamingCapabilities = value;
	}

	/**
	 * Gets the value of the any property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to the returned list will be present inside the JAXB object.
	 * This is why there is not a <CODE>set</CODE> method for the any property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getAny().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link Object } {@link Element }
	 * 
	 * 
	 */
	public List<Object> getAny() {
		if (any == null) {
			any = new ArrayList<Object>();
		}
		return this.any;
	}

	/**
	 * Ruft den Wert der snapshotUri-Eigenschaft ab.
	 * 
	 * @return possible object is {@link Boolean }
	 * 
	 */
	public Boolean isSnapshotUri() {
		return snapshotUri;
	}

	/**
	 * Legt den Wert der snapshotUri-Eigenschaft fest.
	 * 
	 * @param value
	 *            allowed object is {@link Boolean }
	 * 
	 */
	public void setSnapshotUri(Boolean value) {
		this.snapshotUri = value;
	}

	/**
	 * Ruft den Wert der rotation-Eigenschaft ab.
	 * 
	 * @return possible object is {@link Boolean }
	 * 
	 */
	public Boolean isRotation() {
		return rotation;
	}

	/**
	 * Legt den Wert der rotation-Eigenschaft fest.
	 * 
	 * @param value
	 *            allowed object is {@link Boolean }
	 * 
	 */
	public void setRotation(Boolean value) {
		this.rotation = value;
	}

	/**
	 * Ruft den Wert der videoSourceMode-Eigenschaft ab.
	 * 
	 * @return possible object is {@link Boolean }
	 * 
	 */
	public Boolean isVideoSourceMode() {
		return videoSourceMode;
	}

	/**
	 * Legt den Wert der videoSourceMode-Eigenschaft fest.
	 * 
	 * @param value
	 *            allowed object is {@link Boolean }
	 * 
	 */
	public void setVideoSourceMode(Boolean value) {
		this.videoSourceMode = value;
	}

	/**
	 * Ruft den Wert der osd-Eigenschaft ab.
	 * 
	 * @return possible object is {@link Boolean }
	 * 
	 */
	public Boolean isOSD() {
		return osd;
	}

	/**
	 * Legt den Wert der osd-Eigenschaft fest.
	 * 
	 * @param value
	 *            allowed object is {@link Boolean }
	 * 
	 */
	public void setOSD(Boolean value) {
		this.osd = value;
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
