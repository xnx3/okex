package com.xnx3.okex.util;

import com.xnx3.okex.action.PMA;
import com.xnx3.swing.LogFrame;

/**
 * 日志
 * @author 管雷鸣
 *
 */
public class Log {
	public static LogFrame log = null;
	static{
		log = new LogFrame();
		log.setSize(500, 300);
	}
	
	public static void log(String text){
		log.getTextArea().setText(text);
		log.setVisible(true);
	}
	
	public static void append(String text){
		log.appendLineForLastAndPositionLast(text);
		log.setVisible(true);
	}
}
