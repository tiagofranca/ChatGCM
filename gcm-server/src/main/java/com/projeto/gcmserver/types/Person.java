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
public class Person {

	@XmlElement(required = true)
	private String name;
	@XmlElement(required = true)
	private String password;
	@XmlElement(name = "phonenumber", required = true)
	private long id;
	@XmlElement(name = "gcm-key", required = true)
	private String gcmKey;
	
	public Person() {}
	
	public Person(String name, String pass, long id, String gcmKey) {
		this.name = name;
		this.password = pass;
		this.id = id;
		this.gcmKey = gcmKey;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (!(obj instanceof Person)) return false;
		if (((Person)obj) == this) return true;
		
		return ((Person)obj).getId() == this.id;
	}

	public String getGcmKey() {
		return gcmKey;
	}

	public void setGcmKey(String gcmKey) {
		this.gcmKey = gcmKey;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
