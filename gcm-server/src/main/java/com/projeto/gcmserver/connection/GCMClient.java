/**
 * 
 */
package com.projeto.gcmserver.connection;

import java.util.Arrays;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.projeto.gcmserver.graphdb.ManagedBobbyGraphDB;

/**
 * @author Tiago
 *
 */
public class GCMClient {
	
	private static final String API_KEY = "";
	
	public boolean send(SendMessage object) {
		ManagedBobbyGraphDB titan = new ManagedBobbyGraphDB();
		String[] ids = new String[object.getRegistration_ids().size()];
		
		for (int i = 0; i < object.getRegistration_ids().size(); i++) {
			ids[i] = titan.load(Long.parseLong(object.getRegistration_ids().get(i))).getGcmKey();
		}
		
		return sendMessage(object.getData(), ids);
	}

	private boolean sendMessage(Object message, String... gcmKey) {
		
		try {
			
			HttpPost post = new HttpPost("https://android.googleapis.com/gcm/send");
			
			post.setHeader("Content-Type", "application/json");
			post.setHeader("Authorization", "key=" + API_KEY);
			
			SendMessage send = new SendMessage();
			
			send.setRegistration_ids(Arrays.asList(gcmKey));
			send.setData(message);
			
			Gson json = new GsonBuilder().create();
			
			post.setEntity(new StringEntity(json.toJson(send)));
			
			HttpClient client = HttpClientBuilder.create().build();
			
			HttpResponse response = client.execute(post);
			
			return response.getStatusLine().getStatusCode() == 200;
		
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static void main(String[] args) {
		SendMessage send = new SendMessage();
		
		send.setRegistration_ids(Arrays.asList("1", "2", "3", "4"));
		send.setData("teste");
		
		Gson json = new GsonBuilder().create();
		
		System.out.println(json.toJson(send));
		
	}
}
