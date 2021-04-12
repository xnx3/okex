package com.xnx3.okex.api;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 资金
 * @author 管雷鸣
 *
 */
public class Asset {
	
	public static void main(String[] args) {
//		oneHangqing("PMA-USDK");
		balance();
	}
	
	//所有产品行情
	public static void balance() {
		JSONObject json = com.xnx3.okex.util.HttpsUtil.getLoginRequest("/api/v5/asset/balances", "");
		System.out.println(json);
		JSONArray jsonArray = json.getJSONArray("data");
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonItem = jsonArray.getJSONObject(i);
			
			
		}
	}
	
	
}
