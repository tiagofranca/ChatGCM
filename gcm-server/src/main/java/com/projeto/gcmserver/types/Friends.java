/**
 * 
 */
package com.projeto.gcmserver.types;

import java.util.ArrayList;
import java.util.List;

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
public class Friends {

	@XmlElement(name = "objects", required = true)
	private List<Person> friends = new ArrayList<Person>();
	
	public Friends() {}
	
	public Friends(List<Person> friends) {
		this.friends = friends;
	}

	public List<Person> getFriends() {
		return friends;
	}

	public void setFriends(List<Person> friends) {
		this.friends = friends;
	}
}
