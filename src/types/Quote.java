package types;


import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import com.google.gson.JsonObject;

import midLevel.Connection;

public class Quote {
	
	private static final DateTimeFormatter dateParser = ISODateTimeFormat.dateTimeParser().withZoneUTC();
	
	private String symbol;
	private String venue;
	private int bid;
	private int ask;
	private int bidSize;
	private int askSize;
	private int bidDepth;
	private int askDepth;
	private int last;
	private int lastSize;
	private DateTime lastTrade;
	private DateTime quoteTime;
    
    public Quote(){
    	
    }
    
    public static Quote getSimpleQuote(String venue, String symbol){
    	return toQuote(Connection.getConnectionJson(Connection.baseUrl + "/venues/" + venue + "/stocks/" + symbol + "/quote"));
    }
    
    private static Quote toQuote (JsonObject json){
    	Quote rtn = new Quote();
    	
    	if(json.get("ok").getAsBoolean()){
    		if (json.has("symbol")) rtn.setSymbol(json.get("symbol").getAsString());
    		if(json.has("venue")) rtn.setVenue(json.get("venue").getAsString());
    		if(json.has("bidSize")) rtn.setBidSize(json.get("bidSize").getAsInt());
    		if(json.has("askSize")) rtn.setAskSize(json.get("askSize").getAsInt());
    		if(json.has("ask")) rtn.setAsk(json.get("ask").getAsInt());
    		if(json.has("bid")) rtn.setBid(json.get("bid").getAsInt());
    		if(json.has("bidDepth")) rtn.setBidDepth(json.get("bidDepth").getAsInt());
    		if(json.has("askDepth")) rtn.setAskDepth(json.get("askDepth").getAsInt());
    		if(json.has("lastSize")) rtn.setLastSize(json.get("lastSize").getAsInt());
    		if(json.has("last")) rtn.setLast(json.get("last").getAsInt());
    		if(json.has("lastTrade")) rtn.setLastTrade(json.get("lastTrade").getAsString());
    		if(json.has("quoteTime")) rtn.setQuoteTime(json.get("quoteTime").getAsString());
    	}
    	else{
    		System.err.println("Cant parse the Json: " + json.toString());
    	}
    	
    	return rtn;
    }
    
    
    //Getters and Setters
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public String getVenue() {
		return venue;
	}
	public void setVenue(String venue) {
		this.venue = venue;
	}
	public int getBid() {
		return bid;
	}
	public void setBid(int bid) {
		this.bid = bid;
	}
	public int getAsk() {
		return ask;
	}
	public void setAsk(int ask) {
		this.ask = ask;
	}
	public int getBidSize() {
		return bidSize;
	}
	public void setBidSize(int bidSize) {
		this.bidSize = bidSize;
	}
	public int getAskSize() {
		return askSize;
	}
	public void setAskSize(int askSize) {
		this.askSize = askSize;
	}
	public int getBidDepth() {
		return bidDepth;
	}
	public void setBidDepth(int bidDepth) {
		this.bidDepth = bidDepth;
	}
	public int getAskDepth() {
		return askDepth;
	}
	public void setAskDepth(int askDepth) {
		this.askDepth = askDepth;
	}
	public int getLast() {
		return last;
	}
	public void setLast(int last) {
		this.last = last;
	}
	public int getLastSize() {
		return lastSize;
	}
	public void setLastSize(int lastSize) {
		this.lastSize = lastSize;
	}
	public DateTime getLastTrade() {
		return lastTrade;
	}
	public void setLastTrade(String lastTrade) {
		this.lastTrade = dateParser.parseDateTime(lastTrade);
	}
	public DateTime getQuoteTime() {
		return quoteTime;
	}
	public void setQuoteTime(String quoteTime) {
		this.quoteTime = dateParser.parseDateTime(quoteTime);
	}
    
    
}
