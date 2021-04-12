package com.xnx3.okex.bean.trade;

/**
 * 计划委托
 * @author 管雷鸣
 *
 */
public class Jihuaweituo {
	private String id;	//唯一id编号，只是当前软件的而已。
	private String instId;	//币，如 PMA-BTC
	private String side;	//买卖， buy是买，  sell是卖
	private double price;	//单价
	private double size;	//数量
	private int validtime;	//有效期，时间戳，有效期到这个时间，过了有效期后，计划委托将自动停止
	private int runstate;	//运行状态， 1正在运行；  0已停止
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getInstId() {
		return instId;
	}
	public void setInstId(String instId) {
		this.instId = instId;
	}
	public String getSide() {
		return side;
	}
	public void setSide(String side) {
		this.side = side;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public double getSize() {
		return size;
	}
	public void setSize(double size) {
		this.size = size;
	}
	public int getValidtime() {
		return validtime;
	}
	public void setValidtime(int validtime) {
		this.validtime = validtime;
	}
	public int getRunstate() {
		return runstate;
	}
	public void setRunstate(int runstate) {
		this.runstate = runstate;
	}
	@Override
	public String toString() {
		return "Jihuaweituo [instId=" + instId + ", side=" + side + ", price=" + price + ", size=" + size
				+ ", validtime=" + validtime + ", runstate=" + runstate + "]";
	}
	
}
