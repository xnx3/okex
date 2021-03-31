package com.xnx3.okex.action;

import java.util.List;

import com.xnx3.DateUtil;
import com.xnx3.exception.NotReturnValueException;
import com.xnx3.okex.api.Ticker;
import com.xnx3.okex.bean.trade.Order;
import com.xnx3.okex.util.DB;

import net.sf.json.JSONObject;

/**
 * 金额、收益方面
 * @author 管雷鸣
 *
 */
public class Money {
	
	public static void main(String[] args) {
		s();
	}
	
	public static void s(){
		//今天0点的10位时间戳
		int dayZeroTime = DateUtil.getDateZeroTime(DateUtil.timeForUnix10());
		List<Order> list = DB.getDatabase().select(Order.class, "WHERE updateTime > "+dayZeroTime+"000 ORDER BY updateTime ASC"); 
		for (int i = 0; i < list.size(); i++) {
			Order order = list.get(i);
			try {
				System.out.println(DateUtil.dateFormat(order.getUpdateTime(), "MM-dd HH:mm:ss")+",   "+order.toString());
			} catch (NotReturnValueException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 将 BTC 的值 转换为 USDT 的值
	 * @param btc
	 * @return
	 */
	public static double BtcToUsdt(double btc){
		JSONObject json = Ticker.oneHangqing("BTC-USDT");
		double usdt = json.getDouble("last");
		return btc*usdt;
	}
	
}
