package com;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xnx3.DateUtil;
import com.xnx3.exception.NotReturnValueException;
import com.xnx3.net.HttpResponse;
import com.xnx3.net.HttpsUtil;
import com.xnx3.okex.api.Account;
import com.xnx3.okex.bean.Bill;
import com.xnx3.okex.bean.trade.Order;
import com.xnx3.okex.util.DB;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Test {
	static String domain = "https://0ecc86004b204544a55f07cc25bd4692.apig.la-south-2.huaweicloudapis.com";
	static HttpsUtil https = new HttpsUtil();
	
	public static void main(String[] args) {
//		List<Bill> billList = Account.bills();
//		for (int i = 0; i < billList.size(); i++) {
//			Bill bill = billList.get(i);
//			try {
//				System.out.println(DateUtil.dateFormat(bill.getCreateTime(), "yy-MM-dd hh:mm:ss")+","+bill.toString());
//			} catch (NotReturnValueException e) {
//				e.printStackTrace();
//			}
//		}
		
		
//		DB.getDatabase().
		
		ResultSet rs = DB.getDatabase().select("SELECT sum(money) AS money, sum(size), instId FROM \"order\" GROUP BY instId");
		
		try {
			while(rs.next()){
//				for (int i = 1; i < rs.getMetaData().getColumnCount(); i++) {
//					System.out.println(rs.getString(i));
//				}
				System.out.println(rs.getString("money")+"\t"+rs.getString(2)+"\t"+rs.getString("instId"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
//		List<Order> list = DB.getDatabase().select(Order.class, "SELECT * FROM \"order\" GROUP BY instId");
//		for (int i = 0; i < list.size(); i++) {
//			System.out.println(list.get(i));
//		}
		
		DB.getDatabase().closeConn();
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
