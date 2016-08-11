package midLevel;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Connection {
	private final static String apiKey = "22825fc34dca960946db517afd51333cd1661af6";
	public final static String baseUrl = "https://api.stockfighter.io/ob/api";
	public final static String gmUrl = "https://www.stockfighter.io/gm";
	
	
	
	public static JsonObject getConnectionJson(String url){
		return responseToJson(getConnectionResponse(url).get());
	}
	
	public static JsonObject postConnectionJson(String url){
		ResteasyClient client = new ResteasyClientBuilder().build();
		ResteasyWebTarget target = client.target(url);
		Response resp = target.request().header("X-Starfighter-Authorization", Connection.apiKey).buildPost(null).invoke();
		return responseToJson(resp);
	}
	public static <T> JsonObject postConnectionJson(String url, Entity<T> post){
		ResteasyClient client = new ResteasyClientBuilder().build();
		ResteasyWebTarget target = client.target(url);
		Response resp = target.request().header("X-Starfighter-Authorization", Connection.apiKey).post(post);
		return responseToJson(resp);
	}
	
	public static boolean isOnline(){
		JsonObject obj = getConnectionJson(Connection.baseUrl + "/heartbeat");
		return obj.get("ok").getAsBoolean();
	}
	
	public static Builder getConnectionResponse(String url){
		ResteasyClient client = new ResteasyClientBuilder().build();
		ResteasyWebTarget target = client.target(url);
		return target.request().header("X-Starfighter-Authorization", Connection.apiKey);
	}
	
	public static JsonObject responseToJson (Response resp){
		return new JsonParser().parse(resp.readEntity(String.class)).getAsJsonObject();
	}
	
	public static void sleep(int time){
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
