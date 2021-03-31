package com.xnx3.okex.api;

import java.util.ArrayList;
import java.util.List;

import com.xnx3.okex.Global;
import com.xnx3.okex.bean.Bill;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 订单
 * @author 管雷鸣
 *
 */
public class Trade {
	public static void main(String[] args) {
		Global.OKEX_DOMAIN="http://meiguo.okex.zvo.cn";
		System.out.println(ordersHistory().getJSONObject(0));
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
	
}
