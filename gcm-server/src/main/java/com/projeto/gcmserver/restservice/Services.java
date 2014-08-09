/**
 * 
 */
package com.projeto.gcmserver.restservice;

import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.common.util.concurrent.Service;
import com.projeto.gcmserver.connection.GCMClient;
import com.projeto.gcmserver.connection.SendMessage;
import com.projeto.gcmserver.graphdb.ManagedBobbyGraphDB;
import com.projeto.gcmserver.types.Friend;
import com.projeto.gcmserver.types.Friends;
import com.projeto.gcmserver.types.Person;

/**
 * @author tfr_souza
 *
 */
@Path("/api")
public class Services {
	
	private Logger logger = Logger.getLogger(Service.class.getName());
	private ManagedBobbyGraphDB bobbyGraph = new ManagedBobbyGraphDB();
	
	@POST
	@Path("/sendMessage")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String sendMessage(SendMessage send) {
		GCMClient client = new GCMClient();
		
		String retorno = null;
		
		if (client.send(send))
			retorno = "Mensagem enviada com sucesso";
		else
			retorno = "Erro ao enviar mensagem";
		
		return retorno;
		
	}

	@POST
	@Path("/person")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String addPerson(Person person) {
		String retorno = bobbyGraph.addPerson(person.getId(), person.getPassword(), person.getName(), person.getGcmKey());
		if (retorno.contains("201"))
			logger.info("[INSERT] - Name: " + person.getName() + " | ID: " + person.getId());
		else
			logger.info("[INSERT] - " + retorno);
		return "HTTP " + retorno;
	}
	
	@POST
	@Path("/person/{key}/friend")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String addFriend(@PathParam("key") long phonenumber, Friend friend) {
		String retorno = bobbyGraph.addFriend(phonenumber, friend.getId());
		if (retorno.contains("201"))
			logger.info("[ADD FRIEND] - ID: " + phonenumber + " | ID FRIEND: " + friend.getId());
		else
			logger.info("[ADD FRIEND] - " + retorno);
		return "HTTP " + retorno;
	}
	
	@GET
	@Path("/person/{key}/friends")
	@Produces(MediaType.APPLICATION_JSON)
	public Response listFriends(@PathParam("key") long phonenumber) {
		List<Person> list = bobbyGraph.listFriends(phonenumber);		
		Friends f = new Friends();
		f.setFriends(list);
		logger.info("[FRIENDS] - Amigos do ID: " + phonenumber);
		return Response.ok(f, MediaType.APPLICATION_JSON).build();
	}
	
	@GET
	@Path("/person/{key}/friends/recommendations")
	@Produces(MediaType.APPLICATION_JSON)
	public Response recommendations(@PathParam("key") long phonenumber) {
//		List<Suggestion> suggests = bobbyGraph.recommendations(id);
		List<Person> suggests = bobbyGraph.recommendations(phonenumber);
//		Suggestions s = new Suggestions();
//		s.setRecommendations(suggests);
		Friends f = new Friends();
		f.setFriends(suggests);
		logger.info("[RECOMMENDATIONS] - Recomendacoes para o ID: " + phonenumber);
		return Response.ok(f, MediaType.APPLICATION_JSON).build();
	}
	
	@GET
	@Path("/person/{key}/remove")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String removePerson(@PathParam("key") long phonenumber) {
		String retorno = bobbyGraph.remove(phonenumber);
		if (retorno.contains("201"))
			logger.info("[REMOVE] - ID: " + phonenumber);
		else
			logger.info("[REMOVE] - " + retorno);
		return "HTTP " + retorno;
	}
	
	@GET
	@Path("/person/{key}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPerson(@PathParam("key") long phonenumber) {
		Person p = bobbyGraph.load(phonenumber);
		return Response.ok(p, MediaType.APPLICATION_JSON).build();
	}
}
