package com.xnx3.okex.action;

import com.xnx3.media.TTSUtil;
import com.xnx3.okex.EyeInstId;
import com.xnx3.okex.suanfa.market.kLine.KBuyBean;
import com.xnx3.okex.suanfa.market.kLine.KItemBean;
import com.xnx3.okex.suanfa.market.kLine.KLine;

/**
 * 低价提醒
 * @author 管雷鸣
 *
 */
public class DijiaTixing {
	
	public static void main(String[] args) {
//		topc-usdt
		find("TOPC-USDT");
		if(true){
			return;
		}
		
		
		new Thread(new Runnable() {
			public void run() {
				while(true){
					
					for (int i = 0; i < EyeInstId.instIdList.size(); i++) {
						find(EyeInstId.instIdList.get(i));
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	
	public static void find(String instId){
		boolean find = KLine.isDigu(instId, 1, 0.03);
		
		KBuyBean kBuyBean = KLine.executeBuy(instId,1);
		//当前最新的价格
		KItemBean item = kBuyBean.getList().get(0);
		//上一个价格
		KItemBean upOneItem = kBuyBean.getList().get(1);
		//上2个价格
		KItemBean upTwoItem = kBuyBean.getList().get(2);
		
		//判断当前的成交量
		System.out.println(item.getCalde().getNumber());
		System.out.println("1-"+upOneItem.getCalde().getNumber());
		System.out.println("2-"+upTwoItem.getCalde().getNumber());
		
		
		if(find){
		
//			//发现最低价了，然后对比一下，看看当前最低价跟前两个的价格差，是不是暴跌的
//			KBuyBean kBuyBean = KLine.executeBuy(instId,1);
//			//当前最新的价格
//			KItemBean item = kBuyBean.getList().get(0);
//			//上一个价格
//			KItemBean upOneItem = kBuyBean.getList().get(1);
//			//上2个价格
//			KItemBean upTwoItem = kBuyBean.getList().get(2);
			
			//首先判断这是跌了
			if(item.getZhangdie() < 0){
				//在判断跌的幅度，是不是暴跌,暴跌才会有利润差
				double baifenbi = (item.getZhangdie() / item.getCalde().getMaxPrice());
				System.out.println(instId+"跌的百分比："+Math.abs(baifenbi));
				if(Math.abs(baifenbi) > 1){
					System.out.println(instId+"暴跌！！");
					
					//再判断买、卖的差价，是否超过2%
					
					
				}
			}
			
			//计算当前最新的价格跟上一个价格，是不是暴跌的，也就是是不是一下降了超过3%
			
			
//			for (int i = 0; i < kBuyBean.getList().size(); i++) {
//				KItemBean item = kBuyBean.getList().get(i);
//				if(item.isYouxiao()){
//					System.out.println(item.toString());
//				}
//			}
			
//			
//			
//			double priceBaifenbi = ((item.getCalde().getMinPrice() - kBuyBean.getMinPrice())/priceJiange);
//			
//			//距离当前最久，也就是最往前的那个价格
//			KItemBean startKItemBean = kBuyBean.getList().get(kBuyBean.getList().size()-1);
//			double startPriceBaifenbi = ((startKItemBean.getCalde().getMinPrice() - kBuyBean.getMinPrice())/priceJiange);
//			
			
//			System.out.println("------1min------发现最低价，快--"+instId);
//			TTSUtil.speakByThread(instId+"发现最低价，快买");
		}
	}
	
}
