package com.xnx3.okex.suanfa.market.kLine;

import java.util.List;

/**
 * 购买K线计算好的返回
 * @author 管雷鸣
 *
 */
public class KBuyBean {
	
	private List<KItemBean> list; //计算好的数据集合
	private double maxPrice;	//最大价格
	private double minPrice;	//最小价格
	private double allNumber;	//总交易量，总交易的数量，比如交易的是 PMA-USDT，这里就是交易的PMA的总数量，list中所有数量的和
	private int youxiaoNumber;		//list中有效的数量，比如btc-usdt肯定就是跟list.size 一样了，有的半死不活的币，就是用这个来进行判断。list中youxiao，那么这里就加一
	
	public KBuyBean() {
		this.youxiaoNumber = 0;
	}
	
	public List<KItemBean> getList() {
		return list;
	}
	public void setList(List<KItemBean> list) {
		this.list = list;
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
	public double getAllNumber() {
		return allNumber;
	}
	public void setAllNumber(double allNumber) {
		this.allNumber = allNumber;
	}
	
	public int getYouxiaoNumber() {
		return youxiaoNumber;
	}
	public void setYouxiaoNumber(int youxiaoNumber) {
		this.youxiaoNumber = youxiaoNumber;
	}
	@Override
	public String toString() {
		return "KBuyBean [list=" + list + ", maxPrice=" + maxPrice + ", minPrice=" + minPrice + ", allNumber="
				+ allNumber + ", youxiaoNumber=" + youxiaoNumber + "]";
	}
	
}
