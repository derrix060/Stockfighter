package levels;

import com.google.gson.JsonObject;

import midLevel.Level;
import types.Order;

public class FirstSteps {
	public static void main (String args[]){
		
		//"Brute-force" trade...
		JsonObject level = Level.startLevel("first_steps");
		
		String venue = level.get("venues").getAsString();
		String symbol = level.get("tickers").getAsString();
		String account = level.get("account").getAsString();
		
		
		Order order = new Order();
		order.setAccount(account);
		order.setVenue(venue);
		order.setSymbol(symbol);
		
		
		order.setPrice(0);
		order.setQty(100);
		order.setDirection(true);
		order.setOrderType("market");
		
		int executedQty = 0;
		
		while (executedQty == 0)
			executedQty = order.sendSuccessfulOrder();
		
		System.out.println("Done!");
	}
}
