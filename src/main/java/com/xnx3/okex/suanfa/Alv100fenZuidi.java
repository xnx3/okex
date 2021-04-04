package com.xnx3.okex.suanfa;

import com.xnx3.okex.suanfa.market.kLine.KLine;

/**
 * alv-usdt 100分钟内最低点检测
 * @author apple
 *
 */
public class Alv100fenZuidi {
	
	public static void main(String[] args) {
		boolean mairu = KLine.isDigu("ALV-USDT", 5, 0.6);
		if(mairu){
//			TTSUtil.speakByThread("发现低价，快买:"+entry.getKey());
			System.out.println("可买入------alv-usdt");
		}
	}
	
}
