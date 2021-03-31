package com.xnx3.okex.thread;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.xnx3.okex.api.Trade;
import com.xnx3.okex.bean.trade.Order;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 未完成的订单数量的改变，监控订单的产生、以及订单的完成情况
 * @author 管雷鸣
 *
 */
public class NotFinishOrder extends Thread{
	//key：  order.id
	public static Map<String, Order> map = new HashMap<String, Order>();
	
	public static void main(String[] args) {
		new NotFinishOrder().start();
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
		JSONArray array = Trade.ordersPending();
		//先判断新订单，加入进来
		for (int j = 0; j < array.size(); j++) {
			Order order = new Order(array.getJSONObject(j));
			linshiMap.put(order.getId(), order);
			
			if(map.get(order.getId()) == null){
				//发现新订单，加入进map
				
				System.out.println("发现新订单，加入："+JSONObject.fromObject(order));
				
				map.put(order.getId(), order);
			}
		}
		
		
		//在判断原有的订单是否完成
		Iterator<Map.Entry<String, Order>> it = map.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry<String, Order> entry = it.next();
            if(linshiMap.get(entry.getKey()) == null){
            	//当前最新接口返回数据中找不到这个订单了，那么这个订单就是成交或者撤销了，
            	
            	System.out.println("有完成的订单，移除："+JSONObject.fromObject(entry.getValue()));
            	
            	it.remove();//使用迭代器的remove()方法删除元素
            }
        }
		
	}
}
