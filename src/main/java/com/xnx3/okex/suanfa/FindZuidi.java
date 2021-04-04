package com.xnx3.okex.suanfa;

import java.util.HashMap;
import java.util.Map;

import com.xnx3.media.TTSUtil;
import com.xnx3.okex.suanfa.market.kLine.KBuyBean;
import com.xnx3.okex.suanfa.market.kLine.KLine;

public class FindZuidi {
	public static void main(String[] args) {
		
		
//		KBuyBean kBuyBean = executeBuy("PMA-BTC",30);
//		KBuyBean kBuyBean = executeBuy("TOPC-USDT",15);		//利润率最大 5%
//		KBuyBean kBuyBean = executeBuy("ROAD-USDT",15);		//波动算正常，但还没到最低点,等最低点
//		KBuyBean kBuyBean = executeBuy("DNA-USDT",30);		//波动没什么规律，等最低点
		findKemai();
		
	}
	
	public static void findKemai(){
		//key instId，   value 时间段
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("XUC-USDT", 30);
		map.put("PMA-BTC", 30);
		map.put("TOPC-USDT", 15);
		map.put("ROAD-USDT", 15);
		map.put("DNA-USDT", 30);
		map.put("KCASH-USDT", 30);
		map.put("XRP-USDT", 30);
		map.put("PLG-USDT", 30);
		map.put("INT-USDT", 60);
		map.put("ACT-USDT", 30);
		map.put("XSR-USDT", 30);//波动没什么规律，但降了后要买进
		
		for (Map.Entry<String, Integer> entry : map.entrySet()) {
			boolean mairu = KLine.isDigu(entry.getKey(), entry.getValue(), 0.1);
			System.out.println(entry.getKey());
			if(mairu){
//				TTSUtil.speakByThread("发现低价，快买:"+entry.getKey());
				System.out.println("可买入------"+entry.getKey());
			}
			
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
}
