/**
 * 
 */
package com.projeto.gcmserver.types;

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
public class Suggestions {
	
	@XmlElement(required = true)
	private List<Suggestion> recommendations;

	public List<Suggestion> getRecommendations() {
		return recommendations;
	}

	public void setRecommendations(List<Suggestion> recommendations) {
		this.recommendations = recommendations;
	}
}
