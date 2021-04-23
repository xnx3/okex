package com.xnx3.okex.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xnx3.net.HttpResponse;
import com.xnx3.net.HttpUtil;
import com.xnx3.net.HttpsUtil;
import com.xnx3.okex.Global;
import com.xnx3.okex.action.OkexSet;
import com.xnx3.okex.bean.Bill;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 账户信息
 * @author 管雷鸣
 *
 */
public class Account {
	
	public static void main(String[] args) {
		
		//加载okex.config
		OkexSet.load();
		balance();
//		bills();
	}
	
	//所有产品行情
	public static JSONObject balance() {
		JSONObject json = com.xnx3.okex.util.HttpsUtil.getLoginRequest("/api/v5/account/balance", "");
//		System.out.println(json);
//		JSONArray jsonArray = json.getJSONArray("data");
//		for (int i = 0; i < jsonArray.size(); i++) {
//			JSONObject jsonItem = jsonArray.getJSONObject(i);
//			
//		}
		return json;
	}
	
	/**
	 * 账单流水查询，最近7天的。
	 */
	public static List<Bill> bills(){
		List<Bill> list = new ArrayList<Bill>();
		
		JSONObject json = com.xnx3.okex.util.HttpsUtil.getLoginRequest("/api/v5/account/bills", "instType=SPOT&type=2");
		JSONArray jsonArray = json.getJSONArray("data");
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject item = jsonArray.getJSONObject(i);
			System.out.println(item.toString());
			Bill bill = new Bill();
			bill.setBillId(item.getString("billId"));
			bill.setCreateTime(item.getLong("ts"));
			bill.setFee(item.getDouble("fee"));
			bill.setInstType(item.getString("instType"));
			bill.setOrderId(item.getString("ordId"));
			bill.setSize(item.getDouble("sz"));
			bill.setType(item.getString("type"));
			bill.setInstId(item.getString("instId"));
			bill.setSubType(item.getInt("subType"));
			bill.setCcy(item.getString("ccy"));
			list.add(bill);
		}
		return list;
	}
	
}
