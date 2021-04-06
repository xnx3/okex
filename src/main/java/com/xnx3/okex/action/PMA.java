package com.xnx3.okex.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xnx3.net.HttpResponse;
import com.xnx3.net.HttpsUtil;
import com.xnx3.okex.Global;
import com.xnx3.okex.api.Market;
import com.xnx3.okex.api.Ticker;
import com.xnx3.okex.api.Trade;
import com.xnx3.okex.bean.market.Book;
import com.xnx3.okex.bean.market.PriceNumber;
import com.xnx3.okex.util.DoubleUtil;

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
	
	/**
	 * 买、卖 跟单
	 * @param instId 传入如 PMA-BTC
	 */
	public static void buy_sell(String instId){
		
		
		Book book = Market.books(instId, 400);
		
		//找到卖的最低的一个
		PriceNumber sell_zuidi = book.getAsks().get(0);
//		for (int i = 0; i < book.getAsks().size() && i < 10; i++) {
//			System.out.println(book.getAsks().get(i));
//		}
		
		//找到买的出价最高的一个
		PriceNumber buy_zuigao = book.getBids().get(0);
//		Trade.order(instId, side, size, price)
		System.out.println(buy_zuigao);
		
		
		/****** 找到价格精确到小数点后的几位 *****/
		int xiaoshudian_number = 0;
		for (int i = 0; i < book.getBids().size() && i < 20; i++) {
			String str = DoubleUtil.doubleToString(book.getBids().get(i).getPrice());
			int dian = str.indexOf(".");
			if(dian == -1){
				//没有小数点，那省事了
				continue;
			}
			
			int xiaoshudian_length = str.length() - dian - 1;
			System.out.println(xiaoshudian_length);
			if(xiaoshudian_length > xiaoshudian_number){
				xiaoshudian_number = xiaoshudian_length;
			}
		}
		System.out.println("xiaoshudian_number:"+xiaoshudian_number);
		
		/******* 找到这么多小数点的 0.0000001 以便实现加一点或减一点********/
		double change = 0;
		if(xiaoshudian_number > 0){
			//是小数
			StringBuffer sb = new StringBuffer();
			sb.append("0.");
			for (int i = 0; i < xiaoshudian_number - 1; i++) {
				sb.append("0");
			}
			sb.append("1");
			change = Double.parseDouble(sb.toString());
		}
		System.out.println("change:"+change);
		
		
		/*** 判断卖价-买家，差价多少，计算出买、卖的利润的百分比 ****/
		double chajia = sell_zuidi.getPrice() - buy_zuigao.getPrice();
		double lirun_baifenbi = chajia/buy_zuigao.getPrice();	//买入之后立马卖出的利润，如 0.04
		
		
		//计算买入的最低价，单价，且拍在最上面的
		double buyPrice = DoubleUtil.add(buy_zuigao.getPrice(), change); 
		System.out.println("jiage:"+buyPrice);
		
		//计算买入的最小数量
//		Market.
		
		
		
		if(lirun_baifenbi > 0.02){
			//如果利润率大于百分之2，那么自动买入
			//String orderId = Trade.order(instId, "buy", 1, buyPrice);
			
			//创建一个线程，跟踪这个订单
			new Thread(new Runnable() {
				public void run() {
					
				}
			}).start();
			
		}
		
		
	}
	
	public static void main(String[] args) {
//		buy_sell("PMA-BTC");
		buy_sell("XSR-USDT");
	}
	
}
