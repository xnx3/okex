package com.xnx3.okex.suanfa.market.kLine;

import com.xnx3.okex.bean.market.Candle;

import net.sf.json.JSONObject;

/**
 * K线其中一个数值的Bean
 * @author 管雷鸣
 *
 */
public class KItemBean {
	private Candle calde;	//接口返回的原始数据
	private boolean youxiao;	//判断这组数据，有没有成交量,依据是判断成交的价格是否是大于0.5USTD。true是有效
	private double zhangdie;	//涨跌情况，涨就是 + ，跌就是-    不涨不跌就是0。 比如原本3，涨到了3.3，那这里就是 +0.3
	private double jiaoyiUsdt;	//这个时间段产生交易的金额，这里全部单位都是换算为了 USDT
	
	public Candle getCalde() {
		return calde;
	}
	public void setCalde(Candle calde) {
		this.calde = calde;
	}
	public boolean isYouxiao() {
		return youxiao;
	}
	public void setYouxiao(boolean youxiao) {
		this.youxiao = youxiao;
	}
	public double getZhangdie() {
		return zhangdie;
	}
	public void setZhangdie(double zhangdie) {
		this.zhangdie = zhangdie;
	}
	public double getJiaoyiUsdt() {
		return jiaoyiUsdt;
	}
	public void setJiaoyiUsdt(double jiaoyiUsdt) {
		this.jiaoyiUsdt = jiaoyiUsdt;
	}
	public String toString() {
		return JSONObject.fromObject(this).toString();
	}
	
}
