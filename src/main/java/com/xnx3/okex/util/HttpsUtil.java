package com.xnx3.okex.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.xnx3.DateUtil;
import com.xnx3.net.HttpResponse;
import com.xnx3.net.HttpUtil;
import com.xnx3.okex.Global;

import net.sf.json.JSONObject;

public class HttpsUtil {
	public static HttpUtil http;
	public static com.xnx3.net.HttpsUtil https;
	static{
		http = new HttpUtil();
	}
	
	/**
	 * 判断当前是否使用的是https的，是则是返回true
	 * @param url
	 * @return
	 */
	public static boolean isHttps(String url){
		if(url.indexOf("https://") == 0){
			return true;
		}
		
		return false;
	}
	
	/**
	 * 请求，获取接口数据，返回JSON格式
	 * @param url 传入如 /api/v5/market/ticker?instId=BTC_USDT
	 * @return
	 */
	public static JSONObject get(String url){
		HttpResponse hr;
		if(isHttps(url)){
			hr = https.get(Global.OKEX_DOMAIN+url);
		}else{
			hr = http.get(Global.OKEX_DOMAIN+url);
		}
		
		JSONObject json = JSONObject.fromObject(hr.getContent());
//		if(json.get("code")){
//			
//		}
		return json;
	}
	
	/**
	 * 登录请求
	 * @param url 传入如 "/api/v5/account/balance"
	 * @param queryString get参数，传入如 a=1&b=2
	 */
	public static JSONObject getLoginRequest(String url, String queryString){
		System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2,SSLv3");
		
		long currentTime = DateUtil.timeForUnix13();//当前北京时间
		//减去8小时的时间，取伦敦时间
		long time = currentTime - 60*60*8*1000;
//		long time = currentTime;
		
//		String timeS = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.000'Z'").format(new Date())
		Date date = new Date(time);
		String timeS = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.000'Z'").format(date);
		
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("OK-ACCESS-KEY", Global.OK_ACCESS_KEY);
		headers.put("OK-ACCESS-TIMESTAMP", timeS);
		headers.put("OK-ACCESS-PASSPHRASE", Global.OK_ACCESS_PASSPHRASE);
		
		String sign = null;
		try {
			sign = HmacSHA256Base64Utils.sign(timeS, "GET", url, queryString, "", Global.OK_ACCESS_SECRET_KEY);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		headers.put("OK-ACCESS-SIGN", sign);
		
		if(queryString != null && queryString.length() > 0){
			queryString = "?"+queryString;
		}else{
			queryString = "";
		}
		
		HttpResponse hr;
		if(isHttps(url)){
			hr = https.get(Global.OKEX_DOMAIN+url+queryString, headers);
		}else{
			hr = http.get(Global.OKEX_DOMAIN+url+queryString, null, headers);
		}
		
		if(hr.getCode() != 200){
			Log.log(hr.toString());
		}
		JSONObject json = JSONObject.fromObject(hr.getContent());
		if(!json.getString("code").equals("0")){
			Log.log("接口响应失败： \t"+json.toString());
		}
		return json;
	}
	
	public static void main(String[] args) {
		System.out.println(getLoginRequest("/api/v5/account/bills", ""));
		
	}
}
