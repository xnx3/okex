package com.xnx3.okex.suanfa;

import java.util.HashMap;
import java.util.Map;

import com.xnx3.media.TTSUtil;
import com.xnx3.okex.api.Ticker;
import com.xnx3.okex.suanfa.market.kLine.KBuyBean;
import com.xnx3.okex.suanfa.market.kLine.KLine;
import com.xnx3.okex.util.InstUtil;

import net.sf.json.JSONArray;

/**
 * 从所有币种找到当前最低
 * @author 管雷鸣
 *
 */
public class AllBiFindZuidi {
	public static void main(String[] args) {
		JSONArray allHangqing = Ticker.allHangqing();
		for (int i = 0; i < allHangqing.size(); i++) {
			String instId = allHangqing.getJSONObject(i).getString("instId");
			String moneyName = InstUtil.getPriceName(instId);
			if(moneyName.equals("USDK") || moneyName.equals("USDT") || moneyName.equals("BTC")){
				boolean find = KLine.isDigu(instId, 15, 0.1);
				if(find){
					System.out.println(instId);
				}
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
}
