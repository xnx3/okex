package com.xnx3.okex.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xnx3.net.HttpResponse;
import com.xnx3.net.HttpUtil;
import com.xnx3.net.HttpsUtil;
import com.xnx3.okex.Global;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Ticker {
	public static void main(String[] args) {
		System.out.println(oneHangqing("PMA-BTC"));
	}
	
	/**
	 * 某个产品的行情
	 * @param instId
	 * @return 改产品详情的json对象
	 */
	public static synchronized JSONObject oneHangqing(String instId) {
		JSONObject json = com.xnx3.okex.util.HttpsUtil.get("/api/v5/market/ticker?instId="+instId);
		if(json.get("error_msg") != null && json.getString("error_msg").length() > 0){
			System.out.println(json.getString("error_msg"));
			return null;
		}
		JSONArray jsonArray = json.getJSONArray("data");
		if(jsonArray.size() > 1){
			System.out.println("异常，jsonArray.size() > 1");
			return null;
		}
		return jsonArray.getJSONObject(0);
	}
	
	//所有产品行情
	public static JSONArray allHangqing() {
		JSONObject json = com.xnx3.okex.util.HttpsUtil.get("/api/v5/market/tickers?instType=SPOT");
		return json.getJSONArray("data");
	}
	
	/**
	 * 有利润查，可以买的币种
	 */
	public static JSONArray liruncha(){
		JSONArray resultJsonArray = new JSONArray();
		
		JSONArray jsonArray = allHangqing();
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonItem = jsonArray.getJSONObject(i);
			String instId = jsonItem.getString("instId");
			
			//判断是否是usdt、usdk的，不是就直接不看
			if(!isUsdtUsdk(instId)) {
				continue;
			}
			
			String askStr = jsonItem.getString("askPx");	//卖价
			String bidStr = jsonItem.getString("bidPx");	//买价
			
			double ask = 0;
			try {
				ask = Double.parseDouble(askStr);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(askStr);
				continue;
			}
			
			double bid = 0;
			try {
				bid = Double.parseDouble(bidStr);
			}catch (Exception e) {
				e.printStackTrace();
				System.out.println(bidStr);
				continue;
			}
			if(bid > ask) {
				//买大于卖，不合规，退出
				System.out.println("买大于卖！ "+jsonItem.getString("instId"));
				continue;
			}
			
			//算一下卖价跟买价差多少
			double cha = ask - bid;
			double cha_baifenbi = cha/ask;
			if(cha_baifenbi > 0.05) {
				jsonItem.put("lirunbaifenbi", cha_baifenbi);
				resultJsonArray.add(jsonItem);
//				System.out.println("============="+cha_baifenbi+",    "+jsonItem.getString("instId")+", \t\t"+bid);
			}else {
				//System.out.println(cha_baifenbi+",    "+json.getString("instId")+"  "+json.getString("instType"));
			}
		}
		
		return resultJsonArray;
	}
	
	/**
	 * 判断instId是USDT、USDK交易，是则是true
	 */
	public static boolean isUsdtUsdk(String instId) {
		if(instId == null) {
			return false;
		}
		String[] s = instId.split("-");
		if(s.length == 2 && (s[1].equals("USDT") || s[1].equals("USDK") || s[1].equals("BTC"))) {
			return true;
		}
		return false;
	}
	
	
	public static void isBuy() {
		
	}
}
