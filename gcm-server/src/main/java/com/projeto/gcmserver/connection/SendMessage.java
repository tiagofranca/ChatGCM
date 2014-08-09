/**
 * 
 */
package com.projeto.gcmserver.connection;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Tiago
 *
 */
@XmlRootElement
public class SendMessage {

	private List<String> registration_ids;
	private Object data;

	public List<String> getRegistration_ids() {
		return registration_ids;
	}

	public void setRegistration_ids(List<String> registration_ids) {
		this.registration_ids = registration_ids;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
