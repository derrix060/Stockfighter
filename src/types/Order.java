package types;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;


import com.google.gson.Gson;
import com.google.gson.JsonObject;

import midLevel.Connection;

public class Order {
	private String account;
	private String venue;
	private String symbol;
	private int price;
	private int qty;
	private String direction;
	private String orderType;
	
	
	public Order(){
	}
	
	private JsonObject sendOrder (){
		String url = Connection.baseUrl + "/venues/" + getVenue() + "/stocks/" + getSymbol() + "/orders";
		return Connection.postConnectionJson(url, toJson());
	}
	
	public int sendSuccessfulOrder(){
		JsonObject json = sendOrder();
		int id = json.has("id")?json.get("id").getAsInt():0;
		boolean isOpen = true;
		int limit = 0;
		
		while (isOpen && limit < 100){
			isOpen = isOpen(id);
			Connection.sleep(100);
			limit ++;
		}
		
		if (limit > 100){
			System.err.println("Error: Order not sended!");
			return 0;
		}
		else
			System.out.println("Order executed! " + json);
		
		if (json.has("totalFilled")) return json.get("totalFilled").getAsInt();
		return 0;
	}
	
	public int sendTimedOrder(int timeout){
		int inicialTime = 0;
		JsonObject json = sendOrder();
		int id = json.get("id").getAsInt();
		
		while(inicialTime < timeout){
			if (!isOpen(id)){
				//Order executed
				json = status(id);
				return json.get("totalFilled").getAsInt();
			}
			
			try {
				Thread.sleep(10>timeout?timeout:10);
				inicialTime += 10;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		//time out -> Cancel order
		cancelOrder(id);
		json = status(id);
		return json.get("totalFilled").getAsInt();
	}
	
	private JsonObject status(int id){
		return Connection.getConnectionJson(Connection.baseUrl + "/venues/" + getVenue() + "/stocks/" + getSymbol() + "/orders/" + id);
	}
	
	private void cancelOrder(int id){
		String url = Connection.baseUrl + "/venues/" + getVenue() + "/stocks/" + getSymbol() + "/orders/" + id;
		JsonObject json = Connection.responseToJson(Connection.getConnectionResponse(url).delete());
		
		System.out.println("Order cancelled: " + id + " Qty: " + json.get("qty").getAsInt());
	}
	
	private boolean isOpen(int id){
		JsonObject json = status(id);
		
		if(json.get("ok").getAsBoolean()){
			return json.get("open").getAsBoolean();
		}
		
		//on error
		return false;
	}
	
	public Entity<String> toJson(){
		return Entity.entity(new Gson().toJson(this), MediaType.APPLICATION_JSON);
	}
	public String toJsonString(){
		return new Gson().toJson(this);
	}
	
	
	//Getters and Setters
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getVenue() {
		return venue;
	}
	public void setVenue(String venue) {
		this.venue = venue;
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public int getQty() {
		return qty;
	}
	public void setQty(int qty) {
		this.qty = qty;
	}
	public String direction() {
		return direction;
	}
	public void setDirection(boolean buy) {
		this.direction = buy?"buy":"sell";
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	
	
}
