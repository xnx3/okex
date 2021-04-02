package com.xnx3.okex.thread;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.xnx3.DateUtil;
import com.xnx3.exception.NotReturnValueException;
import com.xnx3.okex.api.Trade;
import com.xnx3.okex.bean.trade.Order;
import com.xnx3.okex.util.DB;
import com.xnx3.okex.util.Log;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 已完成的订单数量的改变，监控订单的产生、以及订单的完成情况
 * @author 管雷鸣
 *
 */
public class FinishOrder extends Thread{
	//key：  order.id
	public static Map<String, Order> map = new HashMap<String, Order>();
	
	public static void main(String[] args) {
	}
	
	public void run() {
		while (true) {
			
			try {
				load();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			try {
				//10秒更新一次
				Thread.sleep(1000*3);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void load(){
		//当前循环使用的临时的map
		Map<String, Order> linshiMap = new HashMap<String, Order>();
		JSONArray array = Trade.ordersHistory();
		//先判断新完成的订单，加入进来
		for (int j = 0; j < array.size(); j++) {
			Order order = new Order(array.getJSONObject(j));
			linshiMap.put(order.getId(), order);
			if(map.get(order.getId()) == null){
				//发现新完成订单，加入进map
				map.put(order.getId(), order);
				
				//加入数据表
//				ResultSet rs = DB.getDatabase().select("SELECT * FROM \"order\" WHERE id = "+order.getId());
//				List<Order> list = DB.getDatabase().resultToEntity(rs, Order.class);
				List<Order> list = DB.getDatabase().select(Order.class, "WHERE id = "+order.getId());
				if(list.size() == 0){
					//还没加入过，那么加入
					DB.getDatabase().insert(order);
					

					try {
						Log.append("\n========"+DateUtil.dateFormat(order.getUpdateTime(), "MM-dd HH:mm:ss")+"======="
								+ "订单完成！"
								+ "\n\t种类："+order.getInstId()+""
								+ "\n\t价格："+order.getPrice()
								+ "\n\t数量" + order.getSize()
								+ "\n");
					} catch (NotReturnValueException e) {
						e.printStackTrace();
					}
					System.out.println("发现新完成的订单，加入："+JSONObject.fromObject(order));
					
					
					com.xnx3.media.TTSUtil.speakByThread(order.getSide()+order.getInstId()+",价格"+order.getPrice());
					
					try {
						//加点延迟
						Thread.sleep(5*1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
		}
		
	}
}
