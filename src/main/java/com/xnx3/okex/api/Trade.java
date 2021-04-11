package com.xnx3.okex.api;

import java.util.ArrayList;
import java.util.List;

import com.xnx3.DateUtil;
import com.xnx3.okex.Global;
import com.xnx3.okex.bean.Bill;
import com.xnx3.okex.util.DoubleUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 订单
 * @author 管雷鸣
 *
 */
public class Trade {
	public static void main(String[] args) {
		//System.out.println(order("PMA-BTC", "sell", 1.495, 0.00000000704));
//		System.out.println(DoubleUtil.doubleToString(0.0000000047));
//		System.out.println(cancelOrder("PMA-BTC", "299651603515207680"));
		System.out.println(order("PMA-BTC", "299635152477646848"));
	}

	/**
	 * 未成交账单列表，当前挂着的订单，最近7天的。
	 */
	public static JSONArray ordersPending(){
		JSONObject json = com.xnx3.okex.util.HttpsUtil.getLoginRequest("/api/v5/trade/orders-pending", "instType=SPOT");
		JSONArray jsonArray = json.getJSONArray("data");
		return jsonArray;
	}
	
	//获取某个订单的信息
	public static JSONObject order(String instId, String ordId) {
		JSONObject json = com.xnx3.okex.util.HttpsUtil.getLoginRequest("/api/v5/trade/order","instId="+instId+"&ordId="+ordId);
		return json.getJSONArray("data").getJSONObject(0);
	}

	/**
	 * 历史订单，已完成、且已成交的订单。最近7天的。
	 */
	public static JSONArray ordersHistory(){
		JSONObject json = com.xnx3.okex.util.HttpsUtil.getLoginRequest("/api/v5/trade/orders-history", "instType=SPOT&state=filled");
		JSONArray jsonArray = json.getJSONArray("data");
		return jsonArray;
	}
	
	
	/**
	 * 创建委托订单，也就是买入、卖出
	 * @param instId 传入如 PMA-BTC
	 * @param side 买入是buy，  卖出是 sell
	 * @param size 买入或卖出的币的数量，比如操作 PMA-BTC， 这里是PMA的数量
	 * @param price 价格，单价。 比如 PMA-BTC ，这里的单价就是购买 PMA-BTC的单价，如 0.0000000056
	 * @return 创建成功的订单id，如果没成功，那么这里返回null
	 */
	public static String createOrder(String instId, String side, double size, double price){
		return order(instId, side, size, price);
	}
	

	/**
	 * 创建委托订单，也就是买入、卖出
	 * @param instId 传入如 PMA-BTC
	 * @param side 买入是buy，  卖出是 sell
	 * @param size 买入或卖出的币的数量，比如操作 PMA-BTC， 这里是PMA的数量
	 * @param price 价格，单价。 比如 PMA-BTC ，这里的单价就是购买 PMA-BTC的单价，如 0.0000000056
	 * @return 创建成功的订单id，如果没成功，那么这里返回null
	 * @deprecated 请使用 createOrder(...)
	 */
	public static String order(String instId, String side, double size, double price){
//		{"instId":"PMA-BTC","tdMode":"cash","_feReq":true,"side":"buy","ordType":"limit","px":"0.00000000453","sz":"1"}
		
//		"instId="+instId+"&tdMode=cash&side="+side+"&ordType=limit&sz="+DoubleUtil.doubleToString(size)+"&px="+
		JSONObject json = new JSONObject();
		json.put("instId", instId);
		json.put("tdMode", "cash");
		json.put("side", side);
		json.put("ordType", "limit");
		json.put("sz", DoubleUtil.doubleToString(size));
		json.put("px", DoubleUtil.doubleToString(price));
		
		JSONObject responseJson = com.xnx3.okex.util.HttpsUtil.postLoginRequest("/api/v5/trade/order", json.toString()); 
		JSONArray jsonArray = responseJson.getJSONArray("data");
		String orderid = jsonArray.getJSONObject(0).getString("ordId");
		return orderid;
	}
	
	
	/**
	 * 撤销订单
	 * @param instId 传入如 PMA-BTC
	 * @param ordId 订单id
	 * @return true 撤销成功
	 */
	public static boolean cancelOrder(String instId, String ordId){
		JSONObject json = new JSONObject();
		json.put("instId", instId);
		json.put("ordId", ordId);
		
		JSONObject responseJson = com.xnx3.okex.util.HttpsUtil.postLoginRequest("/api/v5/trade/cancel-order", json.toString()); 
		JSONArray jsonArray = responseJson.getJSONArray("data");
		System.out.println(jsonArray);
		return jsonArray.getJSONObject(0).getString("sCode").equals("0");
	}
}
