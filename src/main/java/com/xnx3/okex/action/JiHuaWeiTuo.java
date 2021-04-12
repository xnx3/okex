package com.xnx3.okex.action;

import java.util.List;

import com.xnx3.media.TTSUtil;
import com.xnx3.okex.api.Market;
import com.xnx3.okex.api.Trade;
import com.xnx3.okex.bean.market.Book;
import com.xnx3.okex.bean.market.PriceNumber;
import com.xnx3.okex.bean.trade.Jihuaweituo;
import com.xnx3.okex.util.DB;
import com.xnx3.swing.DialogUtil;

import net.sf.json.JSONObject;

/**
 * 计划委托
 * @author 管雷鸣
 *
 */
public class JiHuaWeiTuo {
	
	/**
	 * 根据id获取jihuaweituo数据表的数据
	 * @param id jihuaweituo 数据表的id
	 * @return 如果找不到，返回null
	 */
	public static Jihuaweituo getJihuaweituoById(String id){
		List<Jihuaweituo> list = DB.getDatabase().select(Jihuaweituo.class, "WHERE id = '"+id+"'");
		if(list.size() == 0){
			DialogUtil.showMessageDialog("您要编辑的计划委托记录不存在！");
			return null;
		}
		
		return list.get(0);
	}
	
	/**
	 * 根据jihuaweituo开启一个线程运行
	 * @param jihuaweituo
	 */
	public static void runThread(Jihuaweituo jihuaweituo){
		new Thread(new Runnable() {
			public void run() {
				while(true){
					//拉取当前最新价格
					Book book = Market.books(jihuaweituo.getInstId(), 5);
					
					if(jihuaweituo.getSide().equals("buy")){
						//买入
						//判断当前买的最高价
						PriceNumber pn = book.getBids().get(0);
						if(pn.getPrice() <= jihuaweituo.getPrice()){
							//价格合适，自动买入
							
							TTSUtil.speakByThread("自动委托，"+jihuaweituo.getInstId()+"，价格:"+jihuaweituo.getPrice()+",已自动下单完毕");
							String orderId = Trade.createOrder(jihuaweituo.getInstId(), jihuaweituo.getSide(), jihuaweituo.getSize(), jihuaweituo.getPrice());
							if(orderId == null || orderId.length() < 2){
								//创建订单失败
							}else{
								//创建订单成功后，跟踪，15分钟内没有成交，那么自动撤销
								new Thread(new Runnable() {
									public void run() {
										try {
											Thread.sleep(15*60*1000);
										} catch (InterruptedException e) {
											e.printStackTrace();
										}
										
										//如果未成交自动取消委托
										JSONObject orderJson = Trade.order(jihuaweituo.getInstId(), orderId);
										if(!orderJson.getString("state").equals("filled")){
											//只要没有完全成交，那都撤销订单
											Trade.cancelOrder(jihuaweituo.getInstId(), orderId);
											TTSUtil.speakByThread(jihuaweituo.getInstId()+"超时未成交，已自动撤销委托。单价："+jihuaweituo.getPrice());
										}
									}
								}).start();
							}
							return;
						}
					}else{
						//卖出
						
					}
					
					
					//每3秒监控一次
					try {
						Thread.sleep(3*1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
			}
		}).start();
	}
	
}
