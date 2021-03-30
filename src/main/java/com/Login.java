package com;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xnx3.net.HttpResponse;
import com.xnx3.net.HttpsUtil;
import com.xnx3.okex.util.HmacSHA256Base64Utils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Login {
	static String domain = "https://0ecc86004b204544a55f07cc25bd4692.apig.la-south-2.huaweicloudapis.com";
	static HttpsUtil https = new HttpsUtil();
	
	public static void main(String[] args) {
		System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2,SSLv3");
		
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("OK-ACCESS-KEY", "221bd9dd-d9ab-48c4-bc7e-8a02d55b8d05");
		headers.put("OK-ACCESS-TIMESTAMP", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.000'Z'").format(new Date()));
		headers.put("OK-ACCESS-PASSPHRASE", "passphrase3o8fygfy3o8qyg4");
		
		String secretKey = "0532364D772A3708F8BD7E56B8BE65F8";
		String sign = null;
		try {
			sign = HmacSHA256Base64Utils.sign(headers.get("OK-ACCESS-TIMESTAMP"), "GET", "/api/v5/account/balance", "", "", secretKey);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		System.out.println(headers.get("OK-ACCESS-TIMESTAMP"));
		System.out.println(sign);
		headers.put("OK-ACCESS-SIGN", sign);
		
		HttpResponse hr = https.get(domain+"/api/v5/account/balance", headers);
		System.out.println(hr.getContent());
		
	}
	
	//某个产品的行情
	public static void oneHangqing(String instId) {
		HttpResponse hr = https.get(domain+"/api/v5/market/ticker?instId="+instId);
		JSONArray jsonArray = JSONObject.fromObject(hr.getContent()).getJSONArray("data");
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject json = jsonArray.getJSONObject(i);
			String askStr = json.getString("askPx");	//卖价
			String bidStr = json.getString("bidPx");	//买价
			
			double ask = Double.parseDouble(askStr);
			double bid = Double.parseDouble(bidStr);
			
			//算一下卖价跟买价差多少
			double cha = ask - bid;
			double cha_baifenbi = cha/ask;
			if(cha_baifenbi > 0.15) {
				System.out.println("============="+cha_baifenbi+",    "+json.getString("instId"));
			}else {
				System.out.println(cha_baifenbi+",    "+json.getString("instId"));
			}
			
		}
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
				System.out.println("============="+cha_baifenbi+",    "+json.getString("instId"));
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
