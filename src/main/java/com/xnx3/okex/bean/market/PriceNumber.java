package com.xnx3.okex.bean.market;

/**
 * 买、卖 的价格跟数量
 * @author 管雷鸣
 *
 */
public class PriceNumber {
	
	private double price;
	private double number;
	private int orderNumber;	//这个价格买卖的，一共有多少笔订单参于
	
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public double getNumber() {
		return number;
	}
	public void setNumber(double number) {
		this.number = number;
	}
	public int getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(int orderNumber) {
		this.orderNumber = orderNumber;
	}
	
	@Override
	public String toString() {
		return "PriceNumber [price=" + price + ", number=" + number + ", orderNumber=" + orderNumber + "]";
	}
	
}
