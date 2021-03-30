package com.xnx3.okex;

import com.xnx3.okex.thread.DomainChange;
import com.xnx3.okex.ui.MainJframe;

/**
 * 界面运行入口
 * @author 管雷鸣
 *
 */
public class Entry {
	public static void main(String[] args) {
		new DomainChange().start(); //接口切换
		
		
		MainJframe mainJframe = new MainJframe();
		mainJframe.setVisible(true);
	}
}
