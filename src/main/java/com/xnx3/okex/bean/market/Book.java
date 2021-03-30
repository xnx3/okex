package com.xnx3.okex.bean.market;

import java.util.ArrayList;
import java.util.List;

/**
 * market/books接口的返回值
 * @author 管雷鸣
 *
 */
public class Book {
	private List<PriceNumber> asks;
	private List<PriceNumber> bids; //
	
	public Book() {
		this.asks = new ArrayList<PriceNumber>();
		this.bids = new ArrayList<PriceNumber>();
	}
	
	public List<PriceNumber> getAsks() {
		return asks;
	}
	public void setAsks(List<PriceNumber> asks) {
		this.asks = asks;
	}
	public List<PriceNumber> getBids() {
		return bids;
	}
	public void setBids(List<PriceNumber> bids) {
		this.bids = bids;
	}
	
}
