package com.xnx3.okex.api;

import java.util.ArrayList;
import java.util.List;

import com.xnx3.DateUtil;
import com.xnx3.exception.NotReturnValueException;
import com.xnx3.okex.Global;
import com.xnx3.okex.action.Money;
import com.xnx3.okex.bean.market.Book;
import com.xnx3.okex.bean.market.Candle;
import com.xnx3.okex.bean.market.PriceNumber;
import com.xnx3.okex.suanfa.market.kLine.KItemBean;
import com.xnx3.okex.util.DoubleUtil;
import com.xnx3.okex.util.InstUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 市场交易行情
 * @author 管雷鸣
 *
 */
public class Market {
	public static void main(String[] args) {
		Global.OKEX_DOMAIN = "https://www.okex.win";
		
//		String instId = "PMA-USDK";
		String instId = "PMA-BTC";
		String moneyName = InstUtil.getPriceName(instId).toUpperCase();	//获取货币的种类，这里获取的是后面的如USTD
		
		List<Candle> list = candles(instId,"1m");
		
		/**** 1. 计算这个阶段总的交易量，购买目标货币的交易量，如 PMA-USDK，这里计算出的量就是PMA的交易量 ****/
		double allJiaoyiliang = 0;
		for (int i = 0; i < list.size(); i++) {
			Candle c = list.get(i);
			allJiaoyiliang = allJiaoyiliang + c.getNumber();
		}
		System.out.println("总交易量："+DoubleUtil.doubleToString(allJiaoyiliang));
		
		
		/*** 2. 剔除无交易的时间区间 ***/
//		for (int i = 0; i < list.size(); i++) {
//			Candle c = list.get(i);
//			System.out.println("money: "+c.getMoney());
//		}
		
		
		/*** 3.计算波峰及低谷  ***/
		
		int change = 0;
		
		double currentMinPrice = 0;	//记录本次100次波动，价格最小值
		double currentMaxPrice = 0;	//记录本次100次波动数据，价格最大值
		double upPrice = 0;			//记录上一次的最小值的价格
		for (int i = 0; i < list.size(); i++) {
			KItemBean kItemBean = new KItemBean();
			
			Candle c = list.get(i);
			kItemBean.setCalde(c);
			if(i < 1){
				//第一组数据，没什么比对，就只是记录就行了
				System.out.println("i"+i);
				currentMinPrice = c.getMinPrice();
				currentMaxPrice = c.getMinPrice();
				upPrice = c.getMinPrice();
				continue;
			}
			
			//判断这组数据，有没有成交量,依据是判断成交的价格是否是大于0.5USTD
			boolean youxiao = false;	//判断改组数据是否有效，有真正的交易数据。 true是有效
			double jiaoyiMoney = 0;	//交易金额，单位是USDT
			if(moneyName.equals("USTD") || moneyName.equals("USDK")){
				jiaoyiMoney = c.getMoney();
			}else if(moneyName.equals("BTC")){
				jiaoyiMoney = Money.BtcToUsdt(c.getMoney());
			}else{
				System.out.println("K线分析，交易单位"+moneyName+"未能识别, 将其默认为0");
			}
			if(jiaoyiMoney < 0.5){
				//交易量小于0.5USDT，就约等于他没有交易量
				//System.out.println("那就没有成交量");
			}else{
				youxiao = true;
			}
			kItemBean.setYouxiao(youxiao);
			kItemBean.setJiaoyiUsdt(jiaoyiMoney);
			
			
			/* 筛选最大值最小值，只有有效交易量的才会被筛选中 */
			if(youxiao){
				if(c.getMinPrice() > currentMaxPrice){
					currentMaxPrice = c.getMinPrice();
				}else if(c.getMinPrice() < currentMinPrice){
					currentMinPrice = c.getMinPrice();
				}
			}
			
			
			/** 第二次及以后的数据，跟前面的比对，判断是降了还是升了 **/
			if(upPrice > c.getMinPrice()){
				//上次的buy值，大于当前数据的buy值，那就是当前buy又降了，
				upPrice = c.getMinPrice();
			}else if(upPrice < c.getMinPrice()){
				change++;
//				System.out.println("升高了"+(0-upMinPrice-c.getMinPrice()));
			}else{
				System.out.println("未变");
			}
			
//			try {
//				System.out.println(DateUtil.dateFormat(c.getTime(), "hh:mm")+"\t"+(float)c.getMaxPrice()+"\t"+c.getMinPrice()+"\t利润："+(c.getMaxPrice()-c.getMinPrice())/c.getMinPrice()+",   "+c.getNumber());
//			} catch (NotReturnValueException e) {
//				e.printStackTrace();
//			}
//			
		}
		
		System.out.println("最大值："+currentMaxPrice+", 最小值："+currentMinPrice);
//		
//		System.out.println(change);
//		
//		books("PMA-USDK", 5);
	}

	/**
	 * 获取某个币的K线
	 * @param instId 币，传入如 PMA-USDK
	 * @param bar 时间粒度，传入如： 1m/3m/5m/15m/30m/1H/2H/4H/6H/12H/1D/1W/1M/3M/6M/1Y
	 */
	public static List<Candle> candles(String instId, String bar) {
		List<Candle> list = new ArrayList<Candle>();
		if(bar.length() > 0){
			bar = "&bar="+bar;
		}
		
		JSONObject json = com.xnx3.okex.util.HttpsUtil.get("/api/v5/market/candles?instId="+instId+bar);
		JSONArray array = json.getJSONArray("data");
		for (int j = 0; j < array.size(); j++) {
			JSONArray item = array.getJSONArray(j);
			Candle candle = new Candle();
			candle.setTime(item.getLong(0));
			candle.setKaipanPrice(item.getDouble(1));
			candle.setMaxPrice(item.getDouble(2));
			candle.setMinPrice(item.getDouble(3));
			candle.setNumber(item.getDouble(5));
			candle.setMoney(item.getDouble(6));
			list.add(candle);
		}
		
		return list;
	}
	

	/**
	 * 获取某个币的买、卖深度，也就是出价的订单
	 * <p>频率 1s 10次</p>
	 * @param instId 币，传入如 PMA-USDK
	 * @param sz 深度档位数量，最大400，即买卖深度共800条。
	 */
	public static Book books(String instId, int sz) {
		Book book = new Book();
		
		String szStr = "";
		if(sz>1){
			szStr = "&sz="+sz;
		}
		
		JSONObject json = com.xnx3.okex.util.HttpsUtil.get("/api/v5/market/books?instId="+instId+szStr);
		JSONArray array = json.getJSONArray("data");
		JSONObject dataJson = array.getJSONObject(0);
		
		JSONArray asks = dataJson.getJSONArray("asks");
		for (int j = 0; j < asks.size(); j++) {
			JSONArray item = asks.getJSONArray(j);
			PriceNumber pn = new PriceNumber();
			pn.setPrice(item.getDouble(0));
			pn.setNumber(item.getDouble(1));
			pn.setOrderNumber(item.getInt(3));
			book.getAsks().add(pn);
		}
		
		JSONArray bids = dataJson.getJSONArray("bids");
		for (int j = 0; j < bids.size(); j++) {
			JSONArray item = bids.getJSONArray(j);
			PriceNumber pn = new PriceNumber();
			pn.setPrice(item.getDouble(0));
			pn.setNumber(item.getDouble(1));
			pn.setOrderNumber(item.getInt(3));
			book.getBids().add(pn);
		}
		
		return book;
	}
	
}
