package com.xnx3.okex.action;

import com.xnx3.okex.api.Trade;

/**
 * 循环下单，检测到订单消失后再重复进行下单
 * @author 管雷鸣
 *
 */
public class WhileCreateOrder {
	
	public static void main(String[] args) {
		double size = 1.0987;	//交易数量
		double price = 0.000000001; 	//交易价格
		String side = "sell";		//买入
		
		
		double d = 0.00000000798;
		for (int i = 0; i < 30; i++) {
			Trade.order("PMA-BTC", "sell", 1000, 0.00000000770-0.0000000001*i);
		}
		
	}
	
	public static void check(){
		
	}
	
}
