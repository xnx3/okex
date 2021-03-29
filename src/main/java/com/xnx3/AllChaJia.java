package com.xnx3;

import com.xnx3.net.HttpResponse;
import com.xnx3.okex.util.HttpsUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 所有有差价的
 * @author 管雷鸣
 *
 */
public class AllChaJia {
	public static void main(String[] args) {
		allHangqing();
	}

	//所有产品行情
	public static void allHangqing() {
		JSONObject jsonObj = HttpsUtil.get("/api/v5/market/tickers?instType=SPOT");
		JSONArray jsonArray = jsonObj.getJSONArray("data");
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
	
}
