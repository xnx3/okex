package com.xnx3.okex.bean.market;

/**
 * K线波动
 * @author 管雷鸣
 *
 */
public class Candle {
	private long time; //13位时间戳
	private double kaipanPrice;	//开盘价格
	private double maxPrice;	//最高价格
	private double minPrice;	//最低价格
	private double number;	//交易量
	private double money;	//交易金额
	
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public double getKaipanPrice() {
		return kaipanPrice;
	}
	public void setKaipanPrice(double kaipanPrice) {
		this.kaipanPrice = kaipanPrice;
	}
	public double getMaxPrice() {
		return maxPrice;
	}
	public void setMaxPrice(double maxPrice) {
		this.maxPrice = maxPrice;
	}
	public double getMinPrice() {
		return minPrice;
	}
	public void setMinPrice(double minPrice) {
		this.minPrice = minPrice;
	}
	public double getNumber() {
		return number;
	}
	public void setNumber(double number) {
		this.number = number;
	}
	public double getMoney() {
		return money;
	}
	public void setMoney(double money) {
		this.money = money;
	}
	
	
}
