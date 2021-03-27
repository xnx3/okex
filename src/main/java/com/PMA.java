package com;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xnx3.net.HttpResponse;
import com.xnx3.net.HttpsUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class PMA {
	static String domain = "https://0ecc86004b204544a55f07cc25bd4692.apig.la-south-2.huaweicloudapis.com";
	static HttpsUtil https = new HttpsUtil();
	
	static double pmabtc = 0;	
	
	public static void main(String[] args) {
		System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2,SSLv3");
		double pmabtc = oneHangqing("PMA-BTC"); //pma一个值多少btc
		double btc_usdk = oneHangqing("BTC-USDK");	//btc一个值多少usdk
		
		System.out.println((1/pmabtc)/btc_usdk);
		
	}
	
	//某个产品一个的价钱
	public static double oneHangqing(String instId) {
		HttpResponse hr = https.get(domain+"/api/v5/market/ticker?instId="+instId);
		JSONArray jsonArray = JSONObject.fromObject(hr.getContent()).getJSONArray("data");
			JSONObject json = jsonArray.getJSONObject(0);
			String askStr = json.getString("askPx");	//卖价
			String bidStr = json.getString("bidPx");	//买价
			
			double ask = Double.parseDouble(askStr);
			double bid = Double.parseDouble(bidStr);
			System.out.println(json.toString());
			
			//算一下平均数
			double pingjunshu = (ask+bid)/2;
			System.out.println("pingjunshu:" +pingjunshu);
			return pingjunshu;
			
//			//算一下卖价跟买价差多少
//			double cha = ask - bid;
//			double cha_baifenbi = cha/ask;
//			if(cha_baifenbi > 0.15) {
//				System.out.println("============="+cha_baifenbi+",    "+json.getString("instId"));
//			}else {
//				System.out.println(cha_baifenbi+",    "+json.getString("instId"));
//			}
//			
	}
	
	//所有产品行情
	public static void allHangqing() {
		HttpResponse hr = https.get(domain+"/api/v5/market/tickers?instType=SPOT");
		JSONArray jsonArray = JSONObject.fromObject(hr.getContent()).getJSONArray("data");
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject json = jsonArray.getJSONObject(i);
			//System.out.println(json.get("instId"));
//			try {
//				Thread.sleep(500);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//			oneHangqing(json.getString("instId"));
			String instId = json.getString("instId");
			
			//判断是否是usdt、usdk的，不是就直接不看
			if(!isUsdtUsdk(instId)) {
				continue;
			}
			
			String askStr = json.getString("askPx");	//卖价
			String bidStr = json.getString("bidPx");	//买价
			
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
				System.out.println("买大于卖！ "+json.getString("instId"));
				continue;
			}
			
			//算一下卖价跟买价差多少
			double cha = ask - bid;
			double cha_baifenbi = cha/ask;
			if(cha_baifenbi > 0.10) {
				System.out.println("============="+cha_baifenbi+",    "+json.getString("instId")+", \t\t"+bid);
			}else {
				//System.out.println(cha_baifenbi+",    "+json.getString("instId")+"  "+json.getString("instType"));
			}
			
		}
	}
	
	/**
	 * 判断instId是USDT、USDK交易，是则是true
	 */
	public static boolean isUsdtUsdk(String instId) {
		if(instId == null) {
			return false;
		}
		String[] s = instId.split("-");
		if(s.length == 2 && (s[1].equals("USDT") || s[1].equals("USDK"))) {
			return true;
		}
		return false;
	}
	
	
	public static void isBuy() {
		
	}
}
