package com.xnx3.okex.suanfa;

import com.xnx3.DateUtil;
import com.xnx3.exception.NotReturnValueException;
import com.xnx3.okex.suanfa.market.kLine.KBuyBean;
import com.xnx3.okex.suanfa.market.kLine.KItemBean;
import com.xnx3.okex.suanfa.market.kLine.KLine;

public class Zhuzhuangtu {
	
	public static void main(String[] args) {
//		KBuyBean kBuyBean = KLine.executeBuy("FAIR-USDT",5);

//		KBuyBean kBuyBean = KLine.executeBuy("PMA-BTC",5);
//		KBuyBean kBuyBean = executeBuy("PMA-BTC",30);
//		KBuyBean kBuyBean = executeBuy("TOPC-USDT",15);		//利润率最大 5%
//		KBuyBean kBuyBean = executeBuy("ROAD-USDT",15);		//波动算正常，但还没到最低点,等最低点
//		KBuyBean kBuyBean = executeBuy("DNA-USDT",30);		//波动没什么规律，等最低点
//		KBuyBean kBuyBean = KLine.executeBuy("SOC-USDT",15);
		KBuyBean kBuyBean = KLine.executeBuy("XSR-USDT",1);
		
		
		
		//计算这100次数据中，价格高峰跟低谷的间隔数
		double priceJiange = kBuyBean.getMaxPrice() - kBuyBean.getMinPrice();
		System.out.println("priceJiange:"+priceJiange);
		
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
		
		
		for (int i = 0; i < kBuyBean.getList().size(); i++) {
			KItemBean item = kBuyBean.getList().get(i);
			
			/*** 输出柱状图 **/
			
			if(item.getCalde().getMinPrice() < 0 || !item.isYouxiao()){
				//无交易数据、或无效的
			}else{
				double priceBaifenbi = ((item.getCalde().getMinPrice() - kBuyBean.getMinPrice())/priceJiange);  
				if(priceBaifenbi >= 1.001){	//double运算会有误差
					System.out.println("---"+(item.getCalde().getMinPrice() - kBuyBean.getMinPrice()));
					System.out.println(item.toString()+", "+kBuyBean.getMaxPrice() +", "+kBuyBean.getMinPrice());
				}
				try {
					System.out.println(KLine.printLine(priceBaifenbi)+" "+item.getCalde().getMinPrice()+"\t"+DateUtil.dateFormat(item.getCalde().getTime(), "hh:mm:ss")+"\t"+item.getJiaoyiUsdt()+"USDT"+", \t"+item.toString());
				} catch (NotReturnValueException e) {
					e.printStackTrace();
				}
			}
		}
		
		
		
	}
	
}
