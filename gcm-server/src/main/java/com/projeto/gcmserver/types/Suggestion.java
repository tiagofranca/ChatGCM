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
public class Suggestion {

	@XmlElement(required = true)
	private Person suggestion;
	@XmlElement(required = true)
	private int friendsCommon;
	
	public Suggestion() {}
	
	public Suggestion(Person suggestion, int friendsCommon) {
		super();
		this.suggestion = suggestion;
		this.friendsCommon = friendsCommon;
	}

	public Person getSuggestion() {
		return suggestion;
	}
	
	public void setSuggestion(Person suggestion) {
		this.suggestion = suggestion;
	}
	
	public int getFriendsCommon() {
		return friendsCommon;
	}
	
	public void setFriendsCommon(int friendsCommon) {
		this.friendsCommon = friendsCommon;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (!(obj instanceof Suggestion)) return false;
		if (obj == this) return true;
		return this.suggestion.equals(((Suggestion)obj).getSuggestion());
	}
}
