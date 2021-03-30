package com.xnx3.okex.api;

import java.util.ArrayList;
import java.util.List;

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
		ordersPending();
	}

	/**
	 * 账单流水查询，最近7天的。
	 */
	public static JSONArray ordersPending(){
		JSONObject json = com.xnx3.okex.util.HttpsUtil.getLoginRequest("/api/v5/trade/orders-pending", "instType=SPOT&type=2");
		JSONArray jsonArray = json.getJSONArray("data");
		return jsonArray;
	}
	
}
