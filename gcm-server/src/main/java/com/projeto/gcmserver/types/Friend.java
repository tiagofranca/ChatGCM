/**
 * 
 */
package com.projeto.gcmserver.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author tfr_souza
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Friend {

	@XmlElement(name = "facebook_id",required = true)
	private long id;
	
	public Friend() {}
	
	public Friend(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}
