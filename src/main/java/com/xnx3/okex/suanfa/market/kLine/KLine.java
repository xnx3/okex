package com.xnx3.okex.suanfa.market.kLine;

import java.util.ArrayList;
import java.util.List;

import com.xnx3.DateUtil;
import com.xnx3.exception.NotReturnValueException;
import com.xnx3.okex.Global;
import com.xnx3.okex.action.Money;
import com.xnx3.okex.api.Market;
import com.xnx3.okex.api.Ticker;
import com.xnx3.okex.bean.market.Candle;
import com.xnx3.okex.util.DoubleUtil;
import com.xnx3.okex.util.InstUtil;

import net.sf.json.JSONArray;

/**
 * K线算法
 * @author 管雷鸣
 *
 */
public class KLine {
	
	public static String printLine(double value){
		int v = (int) (value*100);
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < v; i++) {
			sb.append(".");
		}
		return sb.toString();
	}
	
	public static void main(String[] args) {
//		String instId = "PMA-BTC";
//		String instId = "HYC-USDT";
//		String instId = "XPO-USDT";
//		String instId = "DOGE-USDT";
//		KBuyBean kBuyBean = executeBuy("KCASH-USDT",60);	//不大对
		
//		KBuyBean kBuyBean = executeBuy("XRP-USDT",30);		//待观察，波动不明显
		
//		KBuyBean kBuyBean = executeBuy("PLG-USDT",15);		//2月份暴涨的，有风险
//		KBuyBean kBuyBean = executeBuy("INT-USDT",60);		//涨了，待观察
//		KBuyBean kBuyBean = executeBuy("ACT-USDT",30);		//波动规律不明显
		

//		KBuyBean kBuyBean = executeBuy("XSR-USDT",30);		//波动没什么规律，但降了后要买进
		
//		KBuyBean kBuyBean = executeBuy("XUC-USDT",30);
		KBuyBean kBuyBean = executeBuy("PMA-BTC",1);
//		KBuyBean kBuyBean = executeBuy("TOPC-USDT",15);		//利润率最大 5%
//		KBuyBean kBuyBean = executeBuy("ROAD-USDT",15);		//波动算正常，但还没到最低点,等最低点
//		KBuyBean kBuyBean = executeBuy("DNA-USDT",30);		//波动没什么规律，等最低点
		
		System.out.println("有效数："+kBuyBean.getYouxiaoNumber());
		for (int i = 0; i < kBuyBean.getList().size(); i++) {
			KItemBean item = kBuyBean.getList().get(i);
			if(item.isYouxiao()){
				System.out.println(item.toString());
			}
		}
		
		//计算这100次数据中，价格高峰跟低谷的间隔数
//		double priceJiange = kBuyBean.getMaxPrice() - kBuyBean.getMinPrice();
//		System.out.println("priceJiange:"+priceJiange);
		
		//以高峰跟低谷的中间数值，二分法来判断，这100个数是否是均匀分布的
//		double erfen_xia = 0;//下半部分
//		double erfen_shang = 0;//上半部分
//		double center = (kBuyBean.getMaxPrice() + kBuyBean.getMinPrice())/2;	//最大数跟最小数中间的单价数值
//		for (int i = 0; i < kBuyBean.getList().size(); i++) {
//			KItemBean item = kBuyBean.getList().get(i);
//			if(item.getAvgPrice() > center){
//				//这个数在中间数之上
//				erfen_shang ++;
//			}else{
//				erfen_xia ++;
//			}
//		}
//		if(erfen_xia > 30){
//			System.out.println("没戏了");
//		}
//		System.out.println("erfen_shang："+erfen_shang+",  erfen_xia:"+erfen_xia);
		
		
//		for (int i = 0; i < kBuyBean.getList().size(); i++) {
//			KItemBean item = kBuyBean.getList().get(i);
//			
//			/*** 输出柱状图 **/
//			
//			if(item.getAvgPrice() < 0){
//				//无交易数据
//			}else{
//				double priceBaifenbi = ((item.getAvgPrice() - kBuyBean.getMinPrice())/priceJiange);  
//				if(priceBaifenbi > 1){
//					System.out.println(item.toString()+", "+kBuyBean.getMaxPrice() +", "+kBuyBean.getMinPrice());
//				}
//				try {
//					System.out.println(printLine(priceBaifenbi)+" "+item.getAvgPrice()+"\t"+DateUtil.dateFormat(item.getCalde().getTime(), "hh:mm:ss")+"\t"+item.getJiaoyiUsdt()+"USDT"+", \t"+item.toString());
//				} catch (NotReturnValueException e) {
//					e.printStackTrace();
//				}
//			}
//		}
		
//		JSONArray allHangqing = Ticker.allHangqing();
//		for (int i = 0; i < allHangqing.size(); i++) {
//			String instId = allHangqing.getJSONObject(i).getString("instId");
//			String moneyName = InstUtil.getPriceName(instId);
//			if(moneyName.equals("USDK") || moneyName.equals("USDT") || moneyName.equals("BTC")){
//				isDigu(instId, 60, 0.2);
//				try {
//					Thread.sleep(300);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
//		}
		
//		isDigu("MITH-BTC");
		
	}
	
	
	/**
	 * 判断某个币是否到了低谷，可以买了
	 * @param instId 传入如 PMA-USDT
	 * @param time 传入值有 1:1分、5:5分、15、30、60
	 * 			60：1小时
	 * 			120 2小时
	 * 			240	4小时
	 * 			720	12小时
	 * 			1440 24小时
	 * @param chenggongjilv 成功几率，0.1 就比 0.1 高，不要超过1，取值0.1 ~ 1
	 * @return 适合买入了，就返回true
	 */
	public static boolean isDigu(String instId, int time, double chenggongjilv){
		KBuyBean kBuyBean = executeBuy(instId,time);
		if(kBuyBean.getYouxiaoNumber() < 15){
			//交易太少
			return false;
		}
		
		//计算这100次数据中，价格高峰跟低谷的间隔数
		double priceJiange = kBuyBean.getMaxPrice() - kBuyBean.getMinPrice();
		
		//当前最新的价格
		KItemBean item = kBuyBean.getList().get(0);
		double priceBaifenbi = ((item.getCalde().getMinPrice() - kBuyBean.getMinPrice())/priceJiange);
		
		//距离当前最久，也就是最往前的那个价格
		KItemBean startKItemBean = kBuyBean.getList().get(kBuyBean.getList().size()-1);
		double startPriceBaifenbi = ((startKItemBean.getCalde().getMinPrice() - kBuyBean.getMinPrice())/priceJiange);
		
		//三分之一处的价格
		KItemBean sanyiKItemBean = kBuyBean.getList().get(kBuyBean.getList().size()/3);
		double sanyiPriceBaifenbi = ((sanyiKItemBean.getCalde().getMinPrice() - kBuyBean.getMinPrice())/priceJiange);
		
		//三分之二处的价格
		KItemBean sanerKItemBean = kBuyBean.getList().get((kBuyBean.getList().size()/3)*2);
		double sanerPriceBaifenbi = ((sanerKItemBean.getCalde().getMinPrice() - kBuyBean.getMinPrice())/priceJiange);
//		System.out.println("--- "+startPriceBaifenbi+" - "+sanyiPriceBaifenbi+" - "+sanerPriceBaifenbi+" - "+priceBaifenbi);
		
		//判断是不是直线往下跌的
		if(startPriceBaifenbi > sanyiPriceBaifenbi && sanyiPriceBaifenbi > sanerPriceBaifenbi && sanerPriceBaifenbi > priceBaifenbi){
			//直线下跌的，不要
			return false;
		}else{
			//不是直线下跌的，才有价值
			if(priceBaifenbi < chenggongjilv){
//				System.out.println("============符合低价："+priceBaifenbi+", "+instId);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 买入点的K线分析
	 * 			
	 * @return 
	 */
	public static KBuyBean executeBuy(String instId, int time){
		KBuyBean kBuyBean = new KBuyBean();
		String moneyName = InstUtil.getPriceName(instId).toUpperCase();	//获取货币的种类，这里获取的是后面的如USTD
		
		
		//从接口取数据出来
		List<Candle> list = Market.candles(instId,intToStringTime(time));
		
		/**** 1. 计算这个阶段总的交易量，购买目标货币的交易量，如 PMA-USDK，这里计算出的量就是PMA的交易量 ****/
		double allJiaoyiliang = 0;
		for (int i = 0; i < list.size(); i++) {
			Candle c = list.get(i);
			allJiaoyiliang = allJiaoyiliang + c.getNumber();
		}
		kBuyBean.setAllNumber(allJiaoyiliang);
		
		
		/*** 2. 剔除无交易的时间区间 ***/
//		for (int i = 0; i < list.size(); i++) {
//			Candle c = list.get(i);
//			System.out.println("money: "+c.getMoney());
//		}
		
		
		/*** 3.计算波峰及低谷  ***/
		List<KItemBean> itemList = new ArrayList<KItemBean>();
		
		double currentMinPrice = 0;	//记录本次100次波动，价格最小值
		double currentMaxPrice = 0;	//记录本次100次波动数据，价格最大值
		double upPrice = 0;			//记录上一次的最小值的价格
		for (int i = 0; i < list.size(); i++) {
			KItemBean kItemBean = new KItemBean();
			
			Candle c = list.get(i);
			kItemBean.setCalde(c);
			if(i < 1){
				//第一组数据，没什么比对，就只是记录就行了
				currentMinPrice = c.getMinPrice();
				currentMaxPrice = c.getMinPrice();
				upPrice = c.getMinPrice();
				continue;
			}
			
			//判断这组数据，有没有成交量,依据是判断成交的价格是否是大于0.5USTD
			boolean youxiao = false;	//判断改组数据是否有效，有真正的交易数据。 true是有效
			double jiaoyiMoney = 0;	//交易金额，单位是USDT
			if(moneyName.equals("USDT") || moneyName.equals("USDK")){
				jiaoyiMoney = c.getMoney();
			}else if(moneyName.equals("BTC")){
				jiaoyiMoney = Money.BtcToUsdt(c.getMoney());
			}else{
				System.out.println("K线分析，交易单位"+moneyName+"未能识别, 将其默认为0");
			}
			if(jiaoyiMoney < 0.5){
				//交易量小于0.5USDT，就约等于他没有交易量
				//System.out.println("那就没有成交量");
			}else{
				youxiao = true;
			}
			kItemBean.setYouxiao(youxiao);
			kItemBean.setJiaoyiUsdt(jiaoyiMoney);
			
			
			/* 筛选buy的最大值最小值，只有有效交易量的才会被筛选中 */
			if(youxiao){
				if(kItemBean.getCalde().getMinPrice() > currentMaxPrice){
					currentMaxPrice = kItemBean.getCalde().getMinPrice();
				}else if(kItemBean.getCalde().getMinPrice() < currentMinPrice){
					currentMinPrice = kItemBean.getCalde().getMinPrice();
				}
			}
			
			/** 第二次及以后的数据，跟前面的比对，判断是降了还是升了 **/
			kItemBean.setZhangdie(c.getMinPrice() - upPrice);
			upPrice = c.getMinPrice();
			
//			try {
//				System.out.println(DateUtil.dateFormat(c.getTime(), "hh:mm")+"\t"+(float)c.getMaxPrice()+"\t"+c.getMinPrice()+"\t利润："+(c.getMaxPrice()-c.getMinPrice())/c.getMinPrice()+",   "+c.getNumber());
//			} catch (NotReturnValueException e) {
//				e.printStackTrace();
//			}
//			
			
			
			itemList.add(kItemBean);
		}
		
		/**** 计算有效数的个数 ****/
		for (int i = 0; i < itemList.size(); i++) {
			if(itemList.get(i).isYouxiao()){
				kBuyBean.setYouxiaoNumber(kBuyBean.getYouxiaoNumber()+1);
			}
		}
		
		kBuyBean.setList(itemList);
		
		kBuyBean.setMaxPrice(currentMaxPrice);
		kBuyBean.setMinPrice(currentMinPrice);
		
		return kBuyBean;
	}
	
	/**
	 * 将int分钟数转为小时的
	 * @param instId 传入如 PMA-USDT
	 * @param time 传入值有 1:1分、5:5分、15、30、60
	 * 			60：1小时
	 * 			120 2小时
	 * 			240	4小时
	 * 			720	12小时
	 * 			1440 24小时
	 */
	public static String intToStringTime(int time){
		String timeStr = "";
		switch (time) {
		case 1440:
			timeStr="24H";
			break;
		case 720:
			timeStr="12H";
			break;
		case 240:
			timeStr="4H";
			break;
		case 120:
			timeStr="2H";
			break;
		case 60:
			timeStr="1H";
			break;
		default:
			timeStr = time+"m";
			break;
		}
		return timeStr;
	}
}
