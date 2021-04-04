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
		System.out.println(order("PMA-BTC", "buy", 1, 0.00000000455));
//		System.out.println(DoubleUtil.doubleToString(0.0000000047));
		
	}

	/**
	 * 未成交账单列表，当前挂着的订单，最近7天的。
	 */
	public static JSONArray ordersPending(){
		JSONObject json = com.xnx3.okex.util.HttpsUtil.getLoginRequest("/api/v5/trade/orders-pending", "instType=SPOT");
		JSONArray jsonArray = json.getJSONArray("data");
		return jsonArray;
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
	 * 查询某个订单的详细信息
	 * @param instId 传入如 PMA-BTC
	 * @param side 买入是buy，  卖出是 sell
	 * @param size 买入或卖出的币的数量，比如操作 PMA-BTC， 这里是PMA的数量
	 * @param price 价格，单价。 比如 PMA-BTC ，这里的单价就是购买 PMA-BTC的单价，如 0.0000000056
	 * @return 是否下单成功， true成功
	 */
	public static boolean order(String instId, String side, double size, double price){
//		{"instId":"PMA-BTC","tdMode":"cash","_feReq":true,"side":"buy","ordType":"limit","px":"0.00000000453","sz":"1"}
		
//		"instId="+instId+"&tdMode=cash&side="+side+"&ordType=limit&sz="+DoubleUtil.doubleToString(size)+"&px="+
		JSONObject json = new JSONObject();
		json.put("instId", instId);
		json.put("tdMode", "cash");
		json.put("side", side);
		json.put("ordType", "limit");
		json.put("sz", DoubleUtil.doubleToString(size));
		json.put("px", DoubleUtil.doubleToString(price));
		
		System.out.println(json.toString());
		JSONObject responseJson = com.xnx3.okex.util.HttpsUtil.postLoginRequest("/api/v5/trade/order", json.toString()); 
		JSONArray jsonArray = responseJson.getJSONArray("data");
		System.out.println(jsonArray);
		String orderid = jsonArray.getJSONObject(0).getString("ordId");
		return orderid.length() > 0;
	}
	
}
