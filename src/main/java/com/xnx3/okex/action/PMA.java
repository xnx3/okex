package com.xnx3.okex.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xnx3.net.HttpResponse;
import com.xnx3.net.HttpsUtil;
import com.xnx3.okex.Global;
import com.xnx3.okex.api.Ticker;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 用btc买pma
 * @author 管雷鸣
 *
 */
public class PMA {
	
	public static String btcToUsdk(){
		StringBuffer sb = new StringBuffer();
		
		//计算btc一个是多少USDT
		JSONObject btc_usdt_json = Ticker.oneHangqing("BTC-USDK");
		double btcusdtPrice =  btc_usdt_json.getDouble("askPx");
		//一个btc能买多少usdt，
		//就是 btcusdtPrice
		sb.append("当前btc-usdk 的价格，一个BTC是 "+btcusdtPrice+" USDK");
		
		
		JSONObject pma_btc_json = Ticker.oneHangqing("PMA-BTC");
		
		sb.append("\npma-btc 卖出:");
		sb.append("\n\t pric:"+pma_btc_json.getString("askPx")+" BTC, \t usdk pric:"+(pma_btc_json.getDouble("askPx")*btcusdtPrice));
		
		sb.append("\npma-btc 买入:");
		sb.append("\n\t pric:"+pma_btc_json.getString("bidPx")+" BTC, \t usdk pric:"+(pma_btc_json.getDouble("bidPx")*btcusdtPrice));
		
		return sb.toString();
	}
	
	public static void main(String[] args) {
		System.out.println(btcToUsdk());
	}
	
}
