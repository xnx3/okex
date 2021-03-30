package com.xnx3.okex.api;

import java.util.ArrayList;
import java.util.List;

import com.xnx3.DateUtil;
import com.xnx3.exception.NotReturnValueException;
import com.xnx3.okex.bean.market.Book;
import com.xnx3.okex.bean.market.Candle;
import com.xnx3.okex.bean.market.PriceNumber;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 市场交易行情
 * @author 管雷鸣
 *
 */
public class Market {
	public static void main(String[] args) {
		List<Candle> list = candles("PMA-USDK","1m");
		
		double upMinPrice = 0;	//上一次的min价格
		int change = 0;
		
		for (int i = 0; i < list.size(); i++) {
			Candle c = list.get(i);
			if(i < 2){
				System.out.println("i"+i);
				upMinPrice = c.getMinPrice();
				continue;
			}
			
			//判断是降了还是升了
			if(upMinPrice > c.getMinPrice()){
				change--;
				System.out.println("降低了"+(upMinPrice-c.getMinPrice()));
			}else if(upMinPrice < c.getMinPrice()){
				change++;
				System.out.println("升高了"+(0-upMinPrice-c.getMinPrice()));
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
		
		System.out.println(change);
		
		books("PMA-USDK", 5);
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
	 * 获取某个币的K线
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
			System.out.println(pn);
			book.getAsks().add(pn);
		}
		
		JSONArray bids = dataJson.getJSONArray("bids");
		for (int j = 0; j < bids.size(); j++) {
			JSONArray item = bids.getJSONArray(j);
			PriceNumber pn = new PriceNumber();
			pn.setPrice(item.getDouble(0));
			pn.setNumber(item.getDouble(1));
			pn.setOrderNumber(item.getInt(3));
			System.out.println(pn);
			book.getBids().add(pn);
		}
		
		return book;
	}
	
}
