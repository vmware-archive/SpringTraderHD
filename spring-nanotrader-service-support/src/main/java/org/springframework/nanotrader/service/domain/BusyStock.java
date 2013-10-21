package org.springframework.nanotrader.service.domain;

public class BusyStock  {
	
	private String quotesymbol;
	
	private Integer count;
	
	public String getQuoteSymbol() {
		return quotesymbol;
	}
	
	public Integer getCount() {
		return count;
	}
	
	public void setQuoteSymbol(String quotesymbol) {
		this.quotesymbol = quotesymbol;
	}
	
	public void setCount(Integer count) {
		this.count = count;
	}
	
	@Override 
	public String toString() {
		return "BusyStock [" + "quotesymbol = " + quotesymbol +  "count = " + count + "]";
	}
	
}