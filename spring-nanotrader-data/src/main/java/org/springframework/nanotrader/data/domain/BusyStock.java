package org.springframework.nanotrader.data.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;

@SuppressWarnings("serial")
@Entity
@Table(name = "busy_stock")
@org.hibernate.annotations.Entity(dynamicUpdate=true)
public class BusyStock implements Serializable {

	@Id
	@Column(name = "quotesymbol", length = 250)
	private String quotesymbol;
	
	@Column(name = "count")
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
