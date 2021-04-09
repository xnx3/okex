package com.xnx3.okex.action;

import java.util.HashMap;
import java.util.Map;

import com.xnx3.media.TTSUtil;
import com.xnx3.okex.api.Market;
import com.xnx3.okex.bean.market.Book;
import com.xnx3.okex.bean.market.PriceNumber;
import com.xnx3.okex.util.DoubleUtil;

/**
 * 暴涨暴跌检测
 * @author 管雷鸣
 */
public class BaozhangBaodie {
	//key:instId  value:当前的Book数据
	public Map<String, Book> bookMap;
	public BaozhangBaodie() {
		bookMap = new HashMap<String, Book>();
	}
	
	public static void main(String[] args) {
		
		new Thread(new Runnable() {
			public void run() {
				BaozhangBaodie jiance = new BaozhangBaodie();
				while(true){
					jiance.check("ETH-USDT", 0.0001);
					
					try {
						//每5秒检测一次
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
		
	}
	
	
	/**
	 * 暴涨检测
	 * @param instId
	 * @param baifenbi 暴跌暴涨的百分比，这里传入 0.01~0.99，  代表暴涨暴跌1%~99%。 当然也可以传入 0.001
	 */
	public void check(String instId, double baifenbi){
		//获取当前深度
		Book book = Market.books(instId, 5);
		
		//上次拉取的数据
		Book upBook = bookMap.get(instId); 
		if(upBook == null){
			//当前是第一次，还没有上次的数据，那么就只是保留数据
			bookMap.put(instId, book);
			return;
		}
		
		/*** 之前的有数据，那么将现在的跟之前的数据进行比较，看是否有跌 ***/
		
		//当前卖的
		PriceNumber sellPn = book.getAsks().get(0);
		//上次卖的
		PriceNumber sellPn_up = upBook.getAsks().get(0);
		//判断卖的是否差价比较大
		double zhangfu = (sellPn_up.getPrice() - sellPn.getPrice())/sellPn.getPrice();	//涨幅百分比
		System.out.println("涨幅："+zhangfu);
		if(zhangfu > baifenbi){
			System.out.println("暴涨！涨幅："+DoubleUtil.doubleToString(zhangfu));
			TTSUtil.speakByThread("暴涨，"+instId);
		}
		
		//当前买的
		PriceNumber buyPn = book.getBids().get(0);
		//上次买的
		PriceNumber buyPn_up = upBook.getBids().get(0);
		//计算是否暴跌
		double diefu = (buyPn_up.getPrice() - buyPn.getPrice())/buyPn.getPrice();	//跌幅百分比
		if(diefu > baifenbi){
			System.out.println("暴跌，跌幅："+DoubleUtil.doubleToString(diefu));
			TTSUtil.speakByThread("暴跌，"+instId);
		}
		
		System.out.println("卖的："+sellPn+",   买的:"+buyPn);
		
		//缓存最新数据
		bookMap.put(instId, book);
	}
	
}
