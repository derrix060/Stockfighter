package midLevel;

import com.google.gson.JsonObject;

public class Level {
	public static void restartLevel (int instanceId){
		System.out.println("Restarting level...");
		JsonObject json = Connection.postConnectionJson(Connection.gmUrl + "/instances/" + instanceId + "/restart");
		if(json.get("ok").getAsBoolean())
			System.out.println("Level Restarted!");
		else
			System.err.println("Error to restart level!\n" + json.get("error"));
	}
	
	public static JsonObject startLevel (String levelName){
		System.out.println("Starting level: " + levelName + "...");
		return Connection.postConnectionJson(Connection.gmUrl + "/levels/" + levelName);
	}
	
	public static void stopLevel (int instanceId){
		System.out.println("Stopping level...");
		
		if(Connection.postConnectionJson(Connection.gmUrl + "/instances/" + instanceId + "/stop").get("ok").getAsBoolean())
			System.out.println("Level Stopped!");
		else
			System.err.println("Error to stop level!");
	}
	
	public static JsonObject getstatusLevel (int instanceId){
		System.out.println("Getting status from level...");
		return Connection.getConnectionJson(Connection.gmUrl + "/instances/" + instanceId);
	}
}
