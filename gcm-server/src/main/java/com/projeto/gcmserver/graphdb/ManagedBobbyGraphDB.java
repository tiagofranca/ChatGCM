/**
 * 
 */
package com.projeto.gcmserver.graphdb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.projeto.gcmserver.types.Person;
import com.projeto.gcmserver.types.Suggestion;
import com.thinkaurelius.titan.core.TitanGraph;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Query.Compare;
import com.tinkerpop.blueprints.Vertex;

/**
 * @author tfr_souza
 *
 */
public class ManagedBobbyGraphDB {

	private TitanGraph bobbyGraph;
	
	public ManagedBobbyGraphDB() {
		bobbyGraph = SimpleBobbyGraphDBFactory.getInstance();
	}
	
	public String addPerson(long phonenumber, String pass, String name, String gcmkey) {
		Person exists = load(phonenumber);
		if (phonenumber < 0) return "INVALID ID: NEGATIVE";
		if (exists.getId() == -1) {
			Vertex person = bobbyGraph.addVertex(null);
			person.setProperty("phonenumber", phonenumber);
			person.setProperty("name", name);
			person.setProperty("password", pass);
			person.setProperty("gcmkey", gcmkey);
			bobbyGraph.commit();
			return "CREATED 201";
		} else {
			return "ID ALREADY EXISTS";
		}
	}
	
	public String addFriend(long id, long idFriend) {
		Vertex person = loadBlockedVertex(id);
		Vertex personFriend = loadBlockedVertex(idFriend);
		
		if (person == null) return "PERSON NOT FOUND";
		if (personFriend == null) return "FRIEND NOT FOUND";
		
		person.addEdge("knows", personFriend);
		personFriend.addEdge("knows", person);		
		bobbyGraph.commit();
		return "CREATED 201";
	}
	
	public List<Person> listFriends(long id) {
		Vertex person = loadBlockedVertex(id);
		List<Person> list = new ArrayList<Person>();
//		Vertex friend = null;
		Person personF = null;
		
		if (person == null) return list;
		
//		for (Edge edge : person.query().direction(Direction.OUT).labels("knows").edges()) {
		for (Vertex friend : person.getVertices(Direction.OUT, "knows")) {
//			friend = edge.getVertex(Direction.IN);
			personF = new Person((String)friend.getProperty("name"), "", (Long)friend.getProperty("phonenumber"), (String)friend.getProperty("gcmkey"));
			list.add(personF);
		}
		
		bobbyGraph.commit();
		
		return list;
	}
	
	public List<Person> recommendations(long id) {
//		List<Suggestion> listSuggests = new ArrayList<Suggestion>();
		List<Person> listSuggests = new ArrayList<Person>();
		List<Person> listSuggestsTemp = new ArrayList<Person>();
		List<Person> friends = listFriends(id);
		Map<Long, Integer> suggests = new HashMap<Long, Integer>();
		Vertex person = loadBlockedVertex(id);
		Person fSuggest = null;
		
		Person personSB = null;
		for (Vertex suggest : person.getVertices(Direction.OUT, "suggested")) {
			personSB = new Person((String)suggest.getProperty("name"), "", (Long)suggest.getProperty("phonenumber"), (String)suggest.getProperty("gcmkey"));
			listSuggestsTemp.add(personSB);
		}
		
		for (Vertex friend : person.getVertices(Direction.OUT, "knows")) {
			for (Vertex friendSuggest : friend.getVertices(Direction.OUT, "knows")) {
				fSuggest = new Person((String)friendSuggest.getProperty("name"), "", (Long)friendSuggest.getProperty("phonenumber"), (String)friendSuggest.getProperty("gcmkey"));
				if (!friends.contains(fSuggest)) {
					if (suggests.get(fSuggest.getId()) == null) {
						if (((Long)person.getProperty("phonenumber")).equals(fSuggest.getId()) || listSuggestsTemp.contains(fSuggest)) {
							suggests.put(fSuggest.getId(), 1);
							continue;
						}
						suggests.put(fSuggest.getId(), 1);
						person.addEdge("suggested", friendSuggest);
					} else {
						Integer newSuggest = suggests.get(fSuggest.getId()) + 1;
						suggests.put(fSuggest.getId(), newSuggest);
					}
				}
			}
		}
		
		bobbyGraph.commit();
		
		Person personS = null;
		Suggestion suggested = null;
		for (Vertex suggest : person.getVertices(Direction.OUT, "suggested")) {
			personS = new Person((String)suggest.getProperty("name"), "", (Long)suggest.getProperty("phonenumber"), (String)suggest.getProperty("gcmkey"));
//			suggested = new Suggestion(personS, suggests.get(personS.getId()));
			listSuggests.add(personS);
		}
		
		return listSuggests;
	}
	
	public Person load(long id) {
		Person person = null;
		if (bobbyGraph.query().has("phonenumber", Compare.EQUAL, id).vertices().iterator().hasNext()) {
			Vertex v = bobbyGraph.query().has("phonenumber", Compare.EQUAL, id).vertices().iterator().next();
			person = new Person((String)v.getProperty("name"), "", (Long)v.getProperty("phonenumber"), (String)v.getProperty("gcmkey"));
		} else {
			person = new Person("pessoa nao encontrada", "", -1, "");
		}
		bobbyGraph.commit();
		return person;
	}
	
	public String remove(long id) {
		Vertex personRemove = loadUnblockedVertex(id);
		
		if (personRemove == null) return "ID NOT FOUND";
		
		for (Edge edge : personRemove.getEdges(Direction.OUT, "knows", "suggested")) {
			bobbyGraph.removeEdge(edge);
		}
		
		bobbyGraph.removeVertex(personRemove);
		
		bobbyGraph.commit();
		
		return "REMOVED 201";
	}
	
	private Vertex loadBlockedVertex(long id) {
		Iterator<Vertex> vertexIt = bobbyGraph.query().has("phonenumber", Compare.EQUAL, id).vertices().iterator();
		return vertexIt.hasNext() ? vertexIt.next() : null;
	}
	
	private Vertex loadUnblockedVertex(long id) {
		Iterator<Vertex> vertexIt = bobbyGraph.query().has("phonenumber", Compare.EQUAL, id).vertices().iterator();
		bobbyGraph.commit();
		return vertexIt.hasNext() ? vertexIt.next() : null;
	}
	
	public void close() {
		if (bobbyGraph != null && bobbyGraph.isOpen()) bobbyGraph.shutdown();
	}
}
