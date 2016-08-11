package levels;



import com.google.gson.JsonObject;

import midLevel.Connection;
import midLevel.Level;
import types.Order;
import types.Quote;

	public class ChockABlock {
	public static void main (String args[]){
		JsonObject level = Level.startLevel("chock_a_block");
		
		String venue = level.get("venues").getAsString();
		String symbol = level.get("tickers").getAsString();
		String account = level.get("account").getAsString();
		int instanceId = level.get("instanceId").getAsInt();
		System.out.println("instance id: " + instanceId);
		
		int blockTradeQty = 100000;
		int qty;
		int ask;
		int askSize;
		
		
		Quote qte = new Quote();
		Order order = new Order();
		order.setAccount(account);
		order.setVenue(venue);
		order.setSymbol(symbol);
		order.setDirection(true);
		order.setOrderType("immediate-or-cancel"); //to don't show the offer to the market.
		qte = Quote.getSimpleQuote(venue, symbol);
		
		
		//Get the target price
		int price = getTargetPrice(qte, order, instanceId);
		
		while(blockTradeQty > 0){
			ask = qte.getAsk();
			askSize = qte.getAskSize();

			System.out.println("Remain qty: " + blockTradeQty);
			
			if(ask > price && askSize > 0){
				//sleep and wait for the better price
				System.out.println("Price is not good: " + ask + " my target price: " + price);
			}
			else{
				//limit the size
				qty = blockTradeQty > askSize ? askSize : blockTradeQty;
				
				order.setQty(qty);
				order.setPrice(ask);
				
				System.out.println("Order:\tQty: " + qty + "\tPrice target: " + ask + "\n");
				
				blockTradeQty -= order.sendSuccessfulOrder();
			}

			Connection.sleep(1000);
		}

		System.out.println("\n\n");
		System.out.println("Done!");
		
		
	}
	
	private static int getTargetPrice(Quote qte, Order order, int instanceId){
		
		order.setPrice(qte.getAsk());
		order.setQty(1);
		order.setOrderType("market");
		int targetPrice = 0;
		int executedQty = 0;
		String price = "";
		JsonObject levelStatus = new JsonObject();
		
		//Force buy 1 qty to get the client target price;
		while(executedQty == 0){
			executedQty = order.sendSuccessfulOrder();
			Connection.sleep(1000);
		}
		
		
		while(targetPrice == 0){
			Connection.sleep(1500);
			levelStatus = Level.getstatusLevel(instanceId);
			
			if(levelStatus.has("flash")){
				price = levelStatus.get("flash").getAsJsonObject().get("info").getAsString();
				price = price.substring(price.length()-7).replace(".", "").replace("$", ""); //get the string for price
				targetPrice = Integer.parseInt(price); //parce to int
				
			}
			else
				System.err.println("I can't find the target price yet");
		}
		
		order.setOrderType("immediate-or-cancel"); //return the order type
		return targetPrice;
	}
	
	
}
