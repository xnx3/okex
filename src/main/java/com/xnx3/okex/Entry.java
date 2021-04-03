package com.xnx3.okex;

import com.xnx3.okex.suanfa.FindZuidi;
import com.xnx3.okex.thread.DomainChange;
import com.xnx3.okex.thread.FinishOrder;
import com.xnx3.okex.thread.NotFinishOrder;
import com.xnx3.okex.ui.MainJframe;

/**
 * 界面运行入口
 * @author 管雷鸣
 *
 */
public class Entry {
	public static void main(String[] args) {
		new DomainChange().start(); //接口自动切换，自动切换为当前能正常访问通的接口
		
		MainJframe mainJframe = new MainJframe();
		mainJframe.setVisible(true);
		
		
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
		
		
		new Thread(new Runnable() {
			public void run() {
				FindZuidi.findKemai();
				try {
					//给接口切换留出点时间来
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}
}
