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

/**
 * 账户信息
 * @author 管雷鸣
 *
 */
public class Account {
	
	public static void main(String[] args) {
		System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2,SSLv3");
//		oneHangqing("PMA-USDK");
		balance();
		bills();
	}
	
	//所有产品行情
	public static void balance() {
		JSONObject json = com.xnx3.okex.util.HttpsUtil.getLoginRequest("/api/v5/account/balance", "");
		System.out.println(json);
		JSONArray jsonArray = json.getJSONArray("data");
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonItem = jsonArray.getJSONObject(i);
			
			
		}
	}
	
	
	public static void bills(){
		JSONObject json = com.xnx3.okex.util.HttpsUtil.getLoginRequest("/api/v5/account/bills", "");
		System.out.println(json);
	}
	
}
