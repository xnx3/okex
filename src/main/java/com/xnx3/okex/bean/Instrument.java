package com.xnx3.okex.bean;

import net.sf.json.JSONObject;

/**
 * instId 的信息，也就是购买的产品的基本信息
 * @author 管雷鸣
 *
 */
public class Instrument {
	private String instId;	//产品id， 如 BTC-USD-SWAP
	private double minSize;	//一次最少可以买多少个
	private double minAddSize;	//数量一次最小可以加多少个
	private double minAddPrice;	//价格一次最小可以加多少
	
	public Instrument() {
	}
	
	public Instrument(JSONObject json) {
		this.instId = json.getString("instId");
		this.minSize = json.getDouble("minSz");
		this.minAddSize = json.getDouble("lotSz");
		this.minAddPrice = json.getDouble("tickSz");
	}
	
	
	public String getInstId() {
		return instId;
	}
	public void setInstId(String instId) {
		this.instId = instId;
	}
	public double getMinSize() {
		return minSize;
	}
	public void setMinSize(double minSize) {
		this.minSize = minSize;
	}
	public double getMinAddSize() {
		return minAddSize;
	}
	public void setMinAddSize(double minAddSize) {
		this.minAddSize = minAddSize;
	}

	public double getMinAddPrice() {
		return minAddPrice;
	}

	public void setMinAddPrice(double minAddPrice) {
		this.minAddPrice = minAddPrice;
	}

	@Override
	public String toString() {
		return JSONObject.fromObject(this).toString();
	}
	
}
