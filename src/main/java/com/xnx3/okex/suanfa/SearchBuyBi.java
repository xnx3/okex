package com.xnx3.okex.suanfa;

import java.util.ArrayList;
import java.util.List;
import com.xnx3.okex.api.Ticker;
import com.xnx3.okex.suanfa.market.kLine.KLine;
import com.xnx3.okex.util.InstUtil;
import net.sf.json.JSONArray;

/**
 * 搜索可以直接买入的币。这里搜到的，就是代表极大几率可以直接买入的
 * @author 管雷鸣
 *
 */
public class SearchBuyBi {
	
	public static void main(String[] args) {
		buy();
	}
	
	public static void buy(){
		
		/* 第一步，搜索可买的 */
		
		//1. 找到15分钟内，可买的，其内存的是instId
		List<String> buyList_15min = new ArrayList<String>();
		
		JSONArray allHangqing = Ticker.allHangqing();
		for (int i = 0; i < allHangqing.size(); i++) {
			String instId = allHangqing.getJSONObject(i).getString("instId");
			String moneyName = InstUtil.getPriceName(instId);
			if(moneyName.equals("USDK") || moneyName.equals("USDT") || moneyName.equals("BTC")){
				boolean find = KLine.isDigu(instId, 15, 0.05);
				if(find){
					System.out.println("------15min------"+instId);
					buyList_15min.add(instId);
				}
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		System.out.println("开始搜索30min----");
		
		//找30min内可买的
		List<String> buyList_30min = new ArrayList<String>();
		for (int i = 0; i < buyList_15min.size(); i++) {
			String instId = buyList_15min.get(i);

			boolean find = KLine.isDigu(instId, 15, 0.05);
			if(find){
				System.out.println("------30min------"+instId);
				buyList_30min.add(instId);
			}
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	
	
}
