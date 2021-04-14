package com.xnx3.okex;

import com.xnx3.okex.action.OkexSet;
import com.xnx3.okex.bean.trade.Jihuaweituo;
import com.xnx3.okex.suanfa.FindZuidi;
import com.xnx3.okex.thread.DomainChange;
import com.xnx3.okex.thread.FinishOrder;
import com.xnx3.okex.thread.NotFinishOrder;
import com.xnx3.okex.ui.MainJframe;
import com.xnx3.okex.util.DB;
import com.xnx3.swing.DialogUtil;

/**
 * 界面运行入口
 * @author 管雷鸣
 *
 */
public class Entry {
	public static void main(String[] args) {
		MainJframe mainJframe = new MainJframe();
		mainJframe.setVisible(true);
		
		new DomainChange().start(); //接口自动切换，自动切换为当前能正常访问通的接口
		
		//加载okex.config
		OkexSet.load();
		//判断okex接口是否设置，如果未设置，给出提醒
		if(Global.OK_ACCESS_KEY == null || Global.OK_ACCESS_KEY.length() < 10){
			DialogUtil.showMessageDialog("请先设置 Okex参数");
			return;
		}
		System.out.println("Global.OK_ACCESS_KEY  "+Global.OK_ACCESS_KEY);
		System.out.println("Global.OK_ACCESS_PASSPHRASE  "+Global.OK_ACCESS_PASSPHRASE);
		System.out.println("Global.OK_ACCESS_SECRET_KEY  "+Global.OK_ACCESS_SECRET_KEY);
		
		//初始化计划委托功能，因为是刚打开，所有计划委托肯定都是未开启的。将所有计划委托设置为未开启
		DB.getDatabase().update("update jihuaweituo set runstate = 0");
		
		new Thread(new Runnable() {
			public void run() {
				try {
					//给接口切换留出点时间来
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
//				new NotFinishOrder().start(); //进行中的订单监控
				new FinishOrder().start(); //订单完成监控
			}
		}).start();
		
		
//		new Thread(new Runnable() {
//			public void run() {
//				FindZuidi.findKemai();
//				try {
//					//给接口切换留出点时间来
//					Thread.sleep(5000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		}).start();
	}
}
